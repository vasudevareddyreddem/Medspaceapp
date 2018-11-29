package com.medarogya.appointment.adapters;

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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.BaseActivity;
import com.medarogya.appointment.activity.HealthReports;
import com.medarogya.appointment.activity.MyReportDownload;
import com.medarogya.appointment.activity.MyReportUpload;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.apis.MyService;
import com.medarogya.appointment.model.AcceptListPJ;
import com.medarogya.appointment.model.RegResult;
import com.medarogya.appointment.utils.MessageToast;
import com.medarogya.appointment.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Bhupi on 29-Oct-18.
 */

public class AcceptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    List<AcceptListPJ> acceptList;
    int SELECT_FILE = 1;
    int REQUEST_CAMERA = 2;
    SessionManager manager;
    MyService service;
    int hos_id_position;
    public AcceptAdapter(Context context, List<AcceptListPJ> acceptList, SessionManager manager, MyService service) {
  this.acceptList=acceptList;
  this.context=context;
  this.manager=manager;
  this.service=service;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_list_card, parent, false);
        return new AcceptHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof AcceptHolder){
            AcceptHolder acceptholder= (AcceptHolder) holder;
            acceptholder.tv_date.setText(acceptList.get(position).getDate());
            acceptholder.cardholdername.setText(acceptList.get(position).getPatinet_name());
            acceptholder.tv_hospital_name.setText(acceptList.get(position).getHospitalName());
            acceptholder.tv_time_slot.setText(acceptList.get(position).getTime());
            acceptholder.tv_dpt_name.setText(acceptList.get(position).getDepartment());
            acceptholder.btn_my_rescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,MyReportDownload. class);
                    i.putExtra("hos_id",acceptList.get(position).getHos_id());
                    context.startActivity(i);
                }
            });

            acceptholder.uploadReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(context,MyReportUpload. class);
                    i.putExtra("hos_id",acceptList.get(position).getHos_id());

                    context.startActivity(i);

                }
            });
    }}

    @Override
    public int getItemCount() {
        return acceptList.size();
    }

    public class AcceptHolder extends RecyclerView.ViewHolder{
        TextView tv_date,tv_dpt_name,tv_time_slot,tv_hospital_name,cardholdername;
        Button btn_my_rescription,uploadReport;
        public AcceptHolder(View itemView) {
            super(itemView);
            tv_date= itemView.findViewById(R.id.tv_date);
            tv_dpt_name= itemView.findViewById(R.id.tv_dpt_name);
            tv_hospital_name= itemView.findViewById(R.id.tv_hospital_name);
            tv_time_slot= itemView.findViewById(R.id.tv_time_slot);
            btn_my_rescription= itemView.findViewById(R.id.btn_my_rescription);
            uploadReport= itemView.findViewById(R.id.upload_report);
            cardholdername= itemView.findViewById(R.id.cardholdername);


        }
    }



    private void selectImage() {
        final CharSequence[] items = {"take Photos", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        ((Activity)context).startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity)context).startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), data.getData());
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                try {
                    picturePath = getFilePath(context, selectedImage);
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
                MessageToast.showToastMethod(context,file.getAbsolutePath());
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("prescription", file.getName(), mFile);

                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), manager.getSingleField(SessionManager.KEY_ID));
                RequestBody hos_id = RequestBody.create(MediaType.parse("text/plain"), acceptList.get(hos_id_position).getHos_id());
                Toast.makeText(context, hos_id+" *****", Toast.LENGTH_SHORT).show();
                Call<RegResult> call = service.uploadPrescription(fileToUpload, filename,hos_id);

                call.enqueue(new Callback<RegResult>() {
                    @Override
                    public void onResponse(Call<RegResult> call, retrofit2.Response<RegResult> response) {

                        if(!response.isSuccessful())
                        {

                        }


                        RegResult result=response.body();
                        Toast.makeText(context, ""+result.getMessage(), Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onFailure(Call<RegResult> call, Throwable t) {

                    }
                });

            }
        }
    }

}


