package com.medspaceit.appointments.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.SearchTestActivity;
import com.medspaceit.appointments.model.SelectLabTestNamePJ;
import com.medspaceit.appointments.utils.util;

import java.util.ArrayList;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagHolder> {

    ArrayList<String> positions;
    SearchTestActivity searchTestActivity;

    public TagsAdapter(SearchTestActivity searchTestActivity, ArrayList<String> positions) {
   this.searchTestActivity=searchTestActivity;
   this.positions=positions;
    }


    @NonNull
    @Override
    public TagsAdapter.TagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
            return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagsAdapter.TagHolder holder, final int position) {
                 holder.tag_txt.setText(positions.get(position));





    }

    @Override
    public int getItemCount() {
        if(positions==null)
            return 0;

        return positions.size();
    }

    public class TagHolder extends RecyclerView.ViewHolder{
        TextView tag_txt;
        public TagHolder(View itemView) {
            super(itemView);
            tag_txt=itemView.findViewById(R.id.tag_name);
    }}


}

