package com.medarogya.appointment.Fragmant;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medarogya.appointment.R;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarrierFragment extends Fragment {
    private int REQUEST_CODE_DOC = 3;
    String docFilePath;
    TextView resumetextview;
    CheckBox show_fee_cbox;
    private static final int STORAGE_PERMISSION_CODE = 121;

    //Uri to store the image uri
    private Uri fileuri;
    String arr_data[];
    String fstate, fdistrict;
    String[] state = {"Select State", "Hospital", "MH", "TS"};
    String[] district = {"Select District"};
    String[] mp_district = {"Select District", "Doctor", "Indore", "Jabalpur", "Ratlaam"};
    String[] mh_district = {"Select District", "Pune", "Gondia", "Shirdi", "Sholapur"};
    String[] ts_district = {"Select District", "Medchal", "Kukatpally", "Secundrabad", "Kammam"};

    public CarrierFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        final View v = inflater.inflate(R.layout.fragment_carrier, container, false);
        resumetextview = v.findViewById(R.id.resumetextview);
        show_fee_cbox = v.findViewById(R.id.show_fee_cbox);
        EditText edt_car_name = v.findViewById(R.id.edt_car_name);
        EditText edt_car_email = v.findViewById(R.id.edt_car_email);
        EditText edt_car_mobile = v.findViewById(R.id.edt_car_mobile);
        EditText edt_car_experience = v.findViewById(R.id.edt_car_experience);
        final Button select_resume_btn = v.findViewById(R.id.select_resume_btn);
        final Button upload_resume_btn = v.findViewById(R.id.upload_resume_btn);
        final Spinner sp_car_category = v.findViewById(R.id.sp_car_category);
        final Spinner sp_car_subcategory = v.findViewById(R.id.sp_car_subcategory);

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, state);
        final ArrayAdapter<String> disAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, district);
        final ArrayAdapter<String> mpAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mp_district);
        final ArrayAdapter<String> mhAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mh_district);
        final ArrayAdapter<String> tsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ts_district);

        sp_car_category.setAdapter(stateAdapter);
        sp_car_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position == 0) {
                    sp_car_subcategory.setAdapter(disAdapter);
                }
                if (position == 1) {
                    sp_car_subcategory.setAdapter(mpAdapter);
                }
                if (position == 2) {
                    sp_car_subcategory.setAdapter(mhAdapter);
                }
                if (position == 3) {
                    sp_car_subcategory.setAdapter(tsAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        upload_resume_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        show_fee_cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    upload_resume_btn.setVisibility(View.VISIBLE);
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setMessage("To Upload Resume Rs 1000/- Debited From Free MEDAROGYA i-cash");
                    alertDialog.setCancelable(false);

                    alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();


            } else {
                    upload_resume_btn.setVisibility(View.GONE);
                }
            }
        });

        select_resume_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fstate = sp_car_category.getSelectedItem().toString();
                fdistrict = sp_car_subcategory.getSelectedItem().toString();
                Toast.makeText(getActivity(), fstate + "  "
                        + fdistrict, Toast.LENGTH_SHORT).show();

//                String[] mimeTypes =
//                        {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
//                                "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
//                                "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
//                                "text/plain",
//                                "application/pdf",
//                                "application/zip"};
                String[] mimeTypes =
                        {"application/msword",
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/pdf",
                        };

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                    if (mimeTypes.length > 0) {
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    }
                } else {
                    String mimeTypesStr = "";
                    for (String mimeType : mimeTypes) {
                        mimeTypesStr += mimeType + "|";
                    }
                    intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                }
                startActivityForResult(Intent.createChooser(intent, "ChooseFile"), REQUEST_CODE_DOC);

            }
        });


        return v;
    }

    public void onActivityResult(int req, int result, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(req, result, data);
        if (result == RESULT_OK) {
            fileuri = data.getData();
            docFilePath = getFileNameByUri(getActivity(), fileuri);
            resumetextview.setText(docFilePath);
            if (resumetextview.getText().toString() != null)
            {
                show_fee_cbox.setEnabled(true);
            }
           }
    }

    private String getFileNameByUri(Context context, Uri uri) {
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION}, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        } else if (uri.getScheme().compareTo("file") == 0) {
            try {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            filepath = uri.getPath();
        }
        return filepath;


    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to getActivity() block
            //Here you can explain why you need getActivity() permission
            //Explain here why you need getActivity() permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                //Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                //Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}


