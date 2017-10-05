package com.bigpicture.team.dadungi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bigpicture.team.dadungi.R;
import com.bigpicture.team.dadungi.item.EnterpriseInfoItem;

import java.util.ArrayList;

/**
 * Created by Soomin Jung on 2017-10-06.
 */

public class MyListAdapter extends BaseAdapter{
    Context context;
    ArrayList<EnterpriseInfoItem> list_itemArrayList;
    ViewHolder viewHolder;

    class ViewHolder{
        TextView coopname;
        TextView cooptype;
        TextView coopaddr;
    }

    public MyListAdapter(Context context, ArrayList<EnterpriseInfoItem> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list,null);

            viewHolder=new ViewHolder();

            viewHolder.coopname = (TextView) convertView.findViewById(R.id.coopname);
            viewHolder.cooptype = (TextView) convertView.findViewById(R.id.cooptype);
            viewHolder.coopaddr = (TextView) convertView.findViewById(R.id.coopaddr);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.coopname.setText(list_itemArrayList.get(position).getName());
        viewHolder.cooptype.setText(list_itemArrayList.get(position).getType());
        viewHolder.coopaddr.setText(list_itemArrayList.get(position).getAddr());

        return convertView;
    }

}
