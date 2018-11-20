package com.medspaceit.appointments.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.SearchTestActivity;
import com.medspaceit.appointments.model.TestPJ;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static com.medspaceit.appointments.activity.SearchTestActivity.tag_view_ll;
import static com.medspaceit.appointments.activity.SearchTestActivity.tagsAdapter;
import static com.medspaceit.appointments.activity.SearchTestActivity.tagsAdapterList;


/**
 * Created by Bhupi on 13-Nov-18.
 */

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestHolder> {
    Context mContext;
    ArrayList<TestPJ> testList;

    private List<TestPJ> namesList;

    public TestListAdapter(Context mContext, ArrayList<TestPJ> namesList) {
        this.mContext = mContext;
        this.namesList = namesList;
        this.testList = new ArrayList();
        this.testList.addAll(namesList);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public TestListAdapter.TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alltestlistlayout, parent, false);
        return new TestHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, final int position) {
        holder.tst_cb.setText(namesList.get(position).getTestname());


        holder.tst_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String name = namesList.get(position).getTestname();
                if (isChecked) {
                    tagsAdapterList.add(name);
                    Log.e("Info ","Info "+tagsAdapterList.toString()+" size "+tagsAdapterList.size());
                    } else {

                    int length=tagsAdapterList.size();
                    for(int i=0;i<length;i++){
                        if(tagsAdapterList.get(i).equals(name))
                        {

                            tagsAdapterList.remove(name);
                            Log.e("Info "," Info "+tagsAdapterList.toString()+ i);

                            break;

                        }
                    }

                }
                if(tagsAdapterList.size()==0)
                {
                    tag_view_ll.setVisibility(View.GONE);
                }
                else {
                    tag_view_ll.setVisibility(View.VISIBLE);

                }
                tagsAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }

    public class TestHolder extends RecyclerView.ViewHolder {
        CheckBox tst_cb;

        public TestHolder(View itemView) {
            super(itemView);
            tst_cb = itemView.findViewById(R.id.tst_cb);


        }
    }


    public void filter(String charText) {
        SearchTestActivity.all_test_recycler_view.setVisibility(View.VISIBLE);

        charText = charText.toLowerCase(Locale.getDefault());
        namesList.clear();
        if (charText.length() == 0) {

            namesList.addAll(testList);
        } else {
            for (TestPJ wp : testList) {
                if (wp.getTestname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    namesList.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }


}

