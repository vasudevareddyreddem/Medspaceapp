package com.medspaceit.appointment.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medspaceit.appointment.R;
import com.medspaceit.appointment.model.AllCardPJ;

import java.util.ArrayList;

/**
 * Created by Bhupi on 30-Oct-18.
 */

public class MyAllCardAdapter extends PagerAdapter {

    ArrayList<AllCardPJ> allCardList;
    private LayoutInflater inflater;
     Context context;

    public MyAllCardAdapter(Context context, ArrayList<AllCardPJ> allCardList) {
        this.context = context;
        this.allCardList = allCardList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return allCardList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View v = inflater.inflate(R.layout.slide_card, view, false);

        TextView holderName = (TextView) v.findViewById(R.id.card_holder_name);
        TextView cardNo = (TextView) v.findViewById(R.id.health_card_number);

        holderName.setText(allCardList.get(position).getName());
        cardNo.setText(allCardList.get(position).getCard_number());
        view.addView(v, 0);
        return v;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}