package com.medarogya.appointment.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.Cart;
import com.medarogya.appointment.activity.Lab;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.AllPackagePojo;
import com.medarogya.appointment.model.TestAddToCartPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bhupi on 21-Nov-18.
 */

public class AllPackageInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AllPackagePojo.List> list = new ArrayList<>();
    Lab context;
    List <String>dialogList;
    String UID,allNames="";

    public AllPackageInfoAdapter(Lab context, AllPackagePojo data, String UID) {
        this.context = context;
        this.UID = UID;
        Collections.reverse(data.list);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyPackageHolder) {
            final MyPackageHolder reportholder = (MyPackageHolder) holder;

            reportholder.txt_package_name.setText(list.get(position).testPackageName);
            reportholder.txt_lab_name.setText("Lab Name: "+list.get(position).name);
            if(list.get(position).reportsTime==null)
            {
                reportholder.txt_reports_in.setText("Reports In:");

            }
            else {
                reportholder.txt_reports_in.setText("Reports In: " + list.get(position).reportsTime);
            }
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


            }

            String str=list.get(position).percentage;
          String aaa=  str.substring(0, str.length() - 3);
            reportholder.txt_package_percentage.setText(aaa+"%Off");
            reportholder.txt_package_amount.setText("MRP: Rs." + list.get(position).amount);
            reportholder.txt_package_amount.setPaintFlags(reportholder.txt_package_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            reportholder.txt_package_discuount.setText("- Rs." + list.get(position).discount);
            if(list.get(position).accrediations==null)
            {reportholder.txt_accrediations.setText("");}
            else {
                reportholder.txt_accrediations.setText(list.get(position).accrediations.toString());
            }String amt=list.get(position).amount;
            String dis=list.get(position).discount;
            int a1=Integer.parseInt(amt);
            int a2=Integer.parseInt(dis);
            int f1=a1-a2;
            reportholder.txt_package_final_price.setText("Amount: "+"Rs." + f1);
            reportholder.btn_view_all_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialogList = new ArrayList();

                    for (int i = 0; i < list.get(position).packageTestList.size(); i++) {
                        dialogList.add(i, "\n" + list.get(position).packageTestList.get(i).testName);
                    }
                    allNames = dialogList.toString();

                    List<String> mPackages = new ArrayList<String>();
                    allNames = allNames.replace("[", "");
                    allNames = allNames.replace("]", "");

                    mPackages.add(allNames);
                    final CharSequence[] allPackage = mPackages.toArray(new String[mPackages.size()]);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setTitle("All Test Names");
                    dialogBuilder.setItems(allPackage, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                                                   }
                    });
                    AlertDialog alertDialogObject = dialogBuilder.create();
                    alertDialogObject.show();
                }
            });


           reportholder.btn_explore.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            String  lTPId= list.get(position).lTPId;
        context.showDialog();

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id",UID);
            jsonObject.put("l_t_p_id",lTPId);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.package_addtocart, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Gson gson = new Gson();
                            context.hideDialog();
                            TestAddToCartPojo data = gson.fromJson(response.toString(), TestAddToCartPojo.class);
                            context.showToast(data.message);
                            context.startActivity(new Intent(context, Cart.class));
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    context.hideDialog();
                }
            });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
});

        }
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyPackageHolder extends RecyclerView.ViewHolder {
        TextView txt_package_name, txt_package_intro, txt_test_no, txt_test_names,txt_accrediations,
                txt_package_percentage,txt_reports_in, txt_lab_name,txt_package_amount, txt_package_discuount,txt_package_final_price;
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
            txt_accrediations = itemView.findViewById(R.id.txt_accrediations);
            txt_lab_name = itemView.findViewById(R.id.txt_lab_name);
            txt_package_final_price = itemView.findViewById(R.id.txt_package_final_price);
            txt_reports_in = itemView.findViewById(R.id.txt_reports_in);

        }
    }

}
