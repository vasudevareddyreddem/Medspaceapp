package com.medspaceit.appointments.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.AcceptAdapter;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.AcceptListPJ;
import com.medspaceit.appointments.utils.MessageToast;
import com.medspaceit.appointments.utils.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
public class HealthReports extends BaseActivity {
    @BindView(R.id.acceptListRcView)
    RecyclerView acceptListRcView;
    @BindView(R.id.back)
    ImageView back;



    List<AcceptListPJ> acceptList;
     int SELECT_FILE = 1;
     int REQUEST_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_reports);
        ButterKnife.bind(this);
        back.setOnClickListener(this);

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
                            Log.e("4444444",response.toString());
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
                                         String hos_id =js.getString("hos_id");
                                         String department =js.getString("department");
                                         String hospitalName =js.getString("hos_bas_name");
                                         String patinet_name =js.getString("patinet_name");
                                         AcceptListPJ acceptListPJ=new AcceptListPJ(date,time,department,hospitalName,hos_id,patinet_name);
                                         acceptList.add(acceptListPJ);
                                     }

                                     AcceptAdapter acceptAdapter=new AcceptAdapter(HealthReports.this,acceptList,manager,service);
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


            case R.id.back:
                finish();
                break;
        }
    }
/*    private void selectImage() {
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
        intent.setType("image*//*");
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
            if(file.exists()){
                MessageToast.showToastMethod(this,file.getAbsolutePath());
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody mFile = RequestBody.create(MediaType.parse("image*//*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("prescription", file.getName(), mFile);

                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"),manager.getSingleField(SessionManager.KEY_ID));
                Call<RegResult> call = service.uploadPrescription(fileToUpload, filename);
                showDialog();
                call.enqueue(new Callback<RegResult>() {
                    @Override
                    public void onResponse(Call<RegResult> call, retrofit2.Response<RegResult> response) {
                        hideDialog();
                        if(!response.isSuccessful())
                        {
                            showToast("Server side error");
                            return;
                        }


                        RegResult result=response.body();
                        showToast(result.getMessage());


                    }

                    @Override
                    public void onFailure(Call<RegResult> call, Throwable t) {
                        hideDialog();
                    }
                });

            }
        }
    }*/

}
