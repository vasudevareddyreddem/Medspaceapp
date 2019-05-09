package com.medarogya.appointment.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;

import com.medarogya.appointment.utils.MessageToast;
import com.medarogya.appointment.utils.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyPharmacyReportUpload extends BaseActivity {

    @BindView(R.id.phar_take_frm_gal)
    TextView phar_take_frm_gal;
    @BindView(R.id.phar_take_frm_cam)
    TextView phar_take_frm_cam;
    @BindView(R.id.phar_cancel)
    TextView phar_cancel;
    @BindView(R.id.et_your_address)
    EditText et_your_address;

    @BindView(R.id.back)
    ImageView back;

    String phar_id;
    static int SELECT_FILE = 1;
    static int REQUEST_CAMERA = 2;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public static final int MULTIPLE_PERMISSIONS = 11;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pharmacy_report_upload);
        ButterKnife.bind(this);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        phar_id = b.getString("phar_id");


        phar_take_frm_gal.setOnClickListener(this);
        phar_cancel.setOnClickListener(this);
        phar_take_frm_cam.setOnClickListener(this);
        back.setOnClickListener(this);
        checkPermissions();

    }

    @Override
    public void onClick(View v) {
        address = et_your_address.getText().toString();
        manager.getSingleField(SessionManager.KEY_ID);
        Log.e("====address", address);
        Log.e("====id", manager.getSingleField(SessionManager.KEY_ID));
        Log.e("====phar_id", phar_id);
        switch (v.getId()) {

            case R.id.phar_take_frm_cam:

                if (address.equals("")) {
                    showToast("Please enter delivery address");
                } else {
                    if (checkPermissions()) {
                        cameraIntent();
                    } else {
                        checkPermissions();
                    }
                }
                break;
            case R.id.phar_take_frm_gal:
                if (address.equals("")) {
                    showToast("Please enter delivery address");
                } else {
                    if (checkPermissions()) {
                        galleryIntent();
                    } else {
                        checkPermissions();

                    }
                }
                break;
            case R.id.phar_cancel:
                finish();
                break;
            case R.id.back:
                finish();
                break;
        }

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(MyPharmacyReportUpload.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA) onCaptureImageResult(data);

        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        String picturePath = null;
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                try {
                    picturePath = getFilePath(MyPharmacyReportUpload.this, selectedImage);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
//                Log.e("Gallery Path", picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (isConnected()) {
            if (new File(picturePath).exists())
                new UploadImage().execute(picturePath);
        } else
            showToast("No Internet Connection");
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        String picturePath = null;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            picturePath = destination.getAbsolutePath();
//            Log.e("Camera Path", destination.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isConnected()) {
            if (new File(picturePath).exists())
                new UploadImage().execute(picturePath);
        } else
            showToast("No Internet Connection");
    }
//


    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    class UploadImage extends AsyncTask<String, Void, String> {
        int status;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected String doInBackground(String... files) {

            try {
                String res = postFile(files[0]);
                return res;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            hideDialog();
            if (result == null)
                return;
            try {
                JSONObject object = new JSONObject(result);
                status = object.getInt("status");
                message = object.getString("message");

                if (status==1) {
                    MessageToast.showToastMethod(MyPharmacyReportUpload.this, message);
                    finish();
                } else {
                    MessageToast.showToastMethod(MyPharmacyReportUpload.this, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private String postFile(String picturePath) {
        String responseStr = null;
        try {
            // new HttpClient
            HttpClient httpClient = new DefaultHttpClient();
            //post header
            HttpPost httpPost = new HttpPost(ApiUrl.PHARMACYUrl + ApiUrl.order_medicine);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("user_id", new StringBody(manager.getSingleField(SessionManager.KEY_ID)));
            reqEntity.addPart("phar_id", new StringBody(phar_id));
            reqEntity.addPart("address", new StringBody(address));

            if (picturePath != null && !picturePath.isEmpty()) {
                File pickedfile = new File(picturePath);
                long fileSizeInBytes = pickedfile.length();
                float fileSizeInKB = fileSizeInBytes / 1024;
                //System.out.println("/-/-/-/-/-/-  file size is " + fileSizeInKB);
                FileBody fBody = new FileBody(pickedfile);
                reqEntity.addPart("img", fBody);

            }

            httpPost.setEntity(reqEntity);
            //execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
//                Log.v("Response in change: ", responseStr);
                // you can add an if statement here and do other actions based on the response
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}
