package com.max.rm.hr.ChatPkg;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.max.rm.hr.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class chatsAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<chatItemClass> list;

    public chatsAdapter(Activity activity, ArrayList<chatItemClass> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v= activity.getLayoutInflater().inflate(R.layout.allchat_row, viewGroup,false);
     //   if(!list.get(i).getLastMessage().isEmpty()) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView time = (TextView) v.findViewById(R.id.time);
            TextView messages_nums = (TextView) v.findViewById(R.id.message_num);
            TextView lastMessage = (TextView) v.findViewById(R.id.lastMessage);
            ImageView icon = (ImageView) v.findViewById(R.id.icon);
            String count= list.get(i).getCountItem();
            if(Integer.parseInt(count)>0){
                messages_nums.setVisibility(View.VISIBLE);
            messages_nums.setText(count);}
            name.setText(list.get(i).getName());
            if (!list.get(i).getTime().equals("")) {
                time.setText(getDate(Long.parseLong(list.get(i).getTime())));
            }
            //time.setText(list.get(i).getTime());
            String type = list.get(i).getType();
            if (type.equals("group")) {
                icon.setImageResource(R.drawable.group48);
            } else {
                icon.setImageResource(R.drawable.user48);

            }
            lastMessage.setText(list.get(i).getLastMessage());

        return v;
    }


    private String getDate(long time) {
//
        Long tsLong = time*1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(tsLong);
        String date = DateFormat.format("hh:mm aa", cal).toString();

    return date;}
}
