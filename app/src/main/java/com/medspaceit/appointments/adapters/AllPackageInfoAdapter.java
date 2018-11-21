package com.medspaceit.appointments.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.model.AllPackagePojo;
import com.medspaceit.appointments.model.MyReportDownloadPoojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 21-Nov-18.
 */

public class AllPackageInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AllPackagePojo.List> list = new ArrayList<>();
    Context context;
    List dialogList = new ArrayList();

    public AllPackageInfoAdapter(Context context, AllPackagePojo data) {
        this.context = context;
        list.addAll(data.list);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allpackageinfolayout, parent, false);
        return new MyPackageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyPackageHolder) {
            final MyPackageHolder reportholder = (MyPackageHolder) holder;

            reportholder.txt_package_name.setText(list.get(position).testPackageName);
            reportholder.txt_package_intro.setText(list.get(position).instruction);
            reportholder.txt_test_no.setText("Includes " + list.get(position).packageTestList.size() + " Tests");
            if (list.get(position).packageTestList.size() == 1) {
                reportholder.txt_test_names.setText(list.get(position).packageTestList.get(0).testName);
            } else if (list.get(position).packageTestList.size() == 2) {
                reportholder.txt_test_names.setText(list.get(position).packageTestList.get(0).testName + "," + list.get(position).packageTestList.get(1).testName);
            } else if (list.get(position).packageTestList.size() == 3) {
                reportholder.txt_test_names.setText(list.get(position).packageTestList.get(0).testName + "," + list.get(position).packageTestList.get(1).testName + "," + list.get(position).packageTestList.get(2).testName);
            } else if (list.get(position).packageTestList.size() >= 4) {
                reportholder.txt_test_names.setVisibility(View.GONE);
                reportholder.btn_view_all_test.setVisibility(View.VISIBLE);
                for (int i = 0; i < list.get(position).packageTestList.size(); i++) {
                    dialogList.add(i, list.get(position).packageTestList.get(i).testName+"\n");
                }
            }

            reportholder.txt_package_percentage.setText(list.get(position).percentage);
            reportholder.txt_package_amount.setText("Rs." + list.get(position).amount);
            reportholder.txt_package_discuount.setText("Rs." + list.get(position).discount);
            reportholder.btn_view_all_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String allNames = dialogList.toString();
                    allNames = allNames.replace("[", "");
                    allNames = allNames.replace("]", "");
                    List<String> mAnimals = new ArrayList<String>();
                    mAnimals.add(allNames);

                    //Create sequence of items
                    final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setTitle("All Test Names");
                    dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            String selectedText = Animals[item].toString();  //Selected item in listview
                        }
                    });
                    //Create alert dialog object via builder
                    AlertDialog alertDialogObject = dialogBuilder.create();
                    //Show the dialog
                    alertDialogObject.show();
                }
            });


        }
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyPackageHolder extends RecyclerView.ViewHolder {
        TextView txt_package_name, txt_package_intro, txt_test_no, txt_test_names,
                txt_package_percentage, txt_package_amount, txt_package_discuount;
        Button btn_view_all_test, btn_explore;

        public MyPackageHolder(View itemView) {
            super(itemView);
            txt_package_name = itemView.findViewById(R.id.txt_package_name);
            txt_package_intro = itemView.findViewById(R.id.txt_package_intro);
            txt_test_no = itemView.findViewById(R.id.txt_test_no);
            txt_test_names = itemView.findViewById(R.id.txt_test_names);
            txt_package_percentage = itemView.findViewById(R.id.txt_package_percentage);
            txt_package_amount = itemView.findViewById(R.id.txt_package_amount);
            txt_package_discuount = itemView.findViewById(R.id.txt_package_discuount);
            btn_explore = itemView.findViewById(R.id.btn_explore);
            btn_view_all_test = itemView.findViewById(R.id.btn_view_all_test);

        }
    }

}
