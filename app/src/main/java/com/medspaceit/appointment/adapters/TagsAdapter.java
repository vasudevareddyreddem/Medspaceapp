package com.medspaceit.appointment.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medspaceit.appointment.R;
import com.medspaceit.appointment.model.Hospital;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagHolder> {

    List<Hospital> list=null;
    OnRemoveListener onRemoveListener=new OnRemoveListener() {
        @Override
        public void onRemoveTag(Hospital s) {

        }
    };

    public TagsAdapter(List<Hospital> list) {
        this.list = list;
    }

    public void setList(List<Hospital> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    @NonNull
    @Override
    public TagsAdapter.TagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
            return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagsAdapter.TagHolder holder, final int position) {
                 holder.tag_txt.setText(list.get(position).getValue());
                 holder.layout.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                        Hospital s=list.remove(position);
                        onRemoveListener.onRemoveTag(s);
                        notifyDataSetChanged();

                     }
                 });
    }

    @Override
    public int getItemCount() {
        if(list==null)
            return 0;

        return list.size();
    }

    public class TagHolder extends RecyclerView.ViewHolder{
        TextView tag_txt;
        CardView layout;
        public TagHolder(View itemView) {
            super(itemView);
            layout=(CardView) itemView;
            tag_txt=itemView.findViewById(R.id.tag_name);
    }}

    public interface OnRemoveListener{
         void onRemoveTag(Hospital s);

    }

}

