package com.medspaceit.appointment.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.adapters.AcceptAdapter;
import com.medspaceit.appointment.adapters.StatusAdapter;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.model.AcceptListPJ;
import com.medspaceit.appointment.model.AppStatus;
import com.medspaceit.appointment.model.RegResult;
import com.medspaceit.appointment.utils.MessageToast;
import com.medspaceit.appointment.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class HealthReports extends BaseActivity {
    @BindView(R.id.acceptListRcView)
    RecyclerView acceptListRcView;
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.uploadReport)
    Button uploadReport;

    List<AcceptListPJ> acceptList;
     int SELECT_FILE = 1;
     int REQUEST_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_reports);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        uploadReport.setOnClickListener(this);
        acceptListRcView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (isConnected()) {
            showDialog();
            fetchAcceptStatusList();
        } else
            MessageToast.showToastMethod(this, "No Internet");



    }

    private void fetchAcceptStatusList() {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", manager.getSingleField(SessionManager.KEY_ID));

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.BaseUrl+ApiUrl.acceptstatuslist, new JSONObject(json),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            acceptList=new ArrayList();
                            try {
                                JSONObject job=new JSONObject(String.valueOf(response));

                                String status=job.getString("status");

                                 if(status.equals("1"))
                                 {

                                     JSONArray jsonArray=job.getJSONArray("list");
                                     for (int i = 0; i < jsonArray.length(); i++) {
                                         JSONObject js=jsonArray.getJSONObject(i);
                                         String date=js.getString("date");
                                         String time =js.getString("time");
                                         String department =js.getString("department");
                                         String hospitalName =js.getString("hos_bas_name");
                                         AcceptListPJ acceptListPJ=new AcceptListPJ(date,time,department,hospitalName);
                                         acceptList.add(acceptListPJ);
                                     }

                                     AcceptAdapter acceptAdapter=new AcceptAdapter(HealthReports.this,acceptList);
                                     acceptListRcView.setAdapter(acceptAdapter);
                                 }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadReport:

                selectImage();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    private void selectImage() {
        final CharSequence[] items = {"take Photos", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(HealthReports.this);
        builder.setTitle("Add photos !");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item){
                    case 0:
                        cameraIntent();
                        break;
                    case 1:
                        galleryIntent();
                        break;
                    case 2:
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

    private String onSelectFromGalleryResult(Intent data) {
        String picturePath=null;
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
                    picturePath = getFilePath(HealthReports.this, selectedImage);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
//                Log.e("Gallery Path", picturePath);
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
        String picturePath=null;
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
        if(filePath!=null||!filePath.isEmpty()){
            final File file=new File(filePath);
            if(file.exists()) {
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                final MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile_pic", file.getName(), mFile);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.BaseUrl+ApiUrl.uploadprescription,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(HealthReports.this,response,Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(HealthReports.this,error.toString(),Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("prescription ", fileToUpload.toString());
                        //params.put("prescription",file.toString());
                        params.put("a_u_id",manager.getSingleField(SessionManager.KEY_ID));

                        return params;
                    }

                };
                RequestQueue requestQueue=Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            }}}
}
