package com.medarogya.appointment.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.JobListUploadResumePojo;
import com.medarogya.appointment.utils.SessionManager;

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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UploadResumeForJob extends  BaseActivity {
    ImageView img_js_upld_resume_back;
    TextView tv_select_resume_name;
    Button btn_up_select_resume,btn_upload_new_resume;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    public static final int MULTIPLE_PERMISSIONS = 18;
    File file ;
    static int SELECT_FILE = 1;
    static int REQUEST_CAMERA = 2;

    String post_id,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resume_for_job);
        Bundle b=getIntent().getExtras();
        post_id=b.getString("post_id");
        img_js_upld_resume_back=findViewById(R.id.img_js_upld_resume_back);

        tv_select_resume_name=findViewById(R.id.tv_select_resume_name);
        btn_up_select_resume=findViewById(R.id.btn_up_select_resume);
        btn_upload_new_resume=findViewById(R.id.btn_upload_new_resume);

        user_id=manager.getSingleField(SessionManager.KEY_ID);
        img_js_upld_resume_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_upload_new_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {

                    if(isConnected()) {

                        {
                            MultipartBody.Part fileToUpload;

                            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            fileToUpload = MultipartBody.Part.createFormData("resume", file.getName(), mFile);

                            RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), user_id);
                            RequestBody postid = RequestBody.create(MediaType.parse("text/plain"), post_id);
                            Call<JobListUploadResumePojo> call = serviceph.uploadResume(fileToUpload, userid,postid);
                            showDialog();
                            call.enqueue(new Callback<JobListUploadResumePojo>() {
                                @Override
                                public void onResponse(Call<JobListUploadResumePojo> call, retrofit2.Response<JobListUploadResumePojo> response) {
                                    hideDialog();
                                    if (!response.isSuccessful()) {
                                        showToast("Server side error");
                                        return;
                                    }
                                    JobListUploadResumePojo result = response.body();

                                    if (result.status == 1) {
                                        showToast(result.message);
                                        finish();
                                    } else {
                                        showToast(result.message);
                                    }

                                }

                                @Override
                                public void onFailure(Call<JobListUploadResumePojo> call, Throwable t) {
                                    hideDialog();
                                    Log.e("======>>", t.getMessage());
                                }
                            });

                        }

                    }
                    else {
                        showToast(getString(R.string.nointernet));
                    }
                }
            }
        });

        btn_up_select_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                if (checkPermissions()) {
                    selectImage();
                } else {
                    checkPermissions();
                }
            }

        });

    }
    private void DownloadResumes(String s) {

        String url = s;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setTitle("Resume");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext");

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) UploadResumeForJob.this.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(UploadResumeForJob.this, "Downloading...", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onClick(View v) {

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(UploadResumeForJob.this, p);
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

    private void selectImage() {
        final CharSequence[] items = { "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadResumeForJob.this);
        builder.setTitle("Select Resume!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {

                    case 0:
                        galleryIntent();
                        break;
                    case 1:
                        dialog.dismiss();
                        break;

                }


            }
        });
        builder.show();
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
            if (requestCode == SELECT_FILE)
                uploadImage(onSelectFromGalleryResult(data));

            else if (requestCode == REQUEST_CAMERA)
                uploadImage(onCaptureImageResult(data));

        }
    }


    @SuppressWarnings("deprecation")
    private String onSelectFromGalleryResult(Intent data) {
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
                    picturePath = getFilePath(UploadResumeForJob.this, selectedImage);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
//                Log.e("Gallery Path", picturePath);
                tv_select_resume_name.setText(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return picturePath;
    }

    private String onCaptureImageResult(Intent data) {
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
        return picturePath;
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

    private void uploadImage(final String filePath) {

        if (filePath != null || !filePath.isEmpty()) {
            file = new File(filePath);
            if (file.exists()) {
                btn_upload_new_resume.setVisibility(View.VISIBLE);

            }
        } else {
            btn_upload_new_resume.setVisibility(View.GONE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
