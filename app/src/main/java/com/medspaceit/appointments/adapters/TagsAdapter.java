package com.medspaceit.appointments.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.SearchTestActivity;
import com.medspaceit.appointments.model.Hospital;
import com.medspaceit.appointments.model.SelectLabTestNamePJ;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagHolder> {


    SearchTestActivity searchTestActivity;
    List<SelectLabTestNamePJ> selectedTestList;
    public TagsAdapter(SearchTestActivity searchTestActivity, List<SelectLabTestNamePJ> selectedTestList) {
   this.searchTestActivity=searchTestActivity;
   this.selectedTestList=selectedTestList;
    }


    @NonNull
    @Override
    public TagsAdapter.TagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
            return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagsAdapter.TagHolder holder, final int position) {
                 holder.tag_txt.setText(selectedTestList.get(position).getTestname());

    }

    @Override
    public int getItemCount() {
        if(selectedTestList==null)
            return 0;

        return selectedTestList.size();
    }

    public class TagHolder extends RecyclerView.ViewHolder{
        TextView tag_txt;
        public TagHolder(View itemView) {
            super(itemView);
            tag_txt=itemView.findViewById(R.id.tag_name);
    }}


}

