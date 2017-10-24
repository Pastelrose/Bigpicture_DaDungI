package com.bigpicture.team.dadungi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigpicture.team.dadungi.item.EnterpriseInfoItem;
import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.maps.*;


public class MapInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    TextView textView_name;
    TextView textView_type;
    TextView textView_addr;
    TextView textView_tel;
    TextView textView_bef;
    //EnterpriseInfoItem item = new EnterpriseInfoItem();

    public static MapInfoFragment newInstance(){
        MapInfoFragment f = new MapInfoFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_info, container, false);
        textView_name = (TextView)view.findViewById(R.id.ent_name);
        textView_addr = (TextView)view.findViewById(R.id.ent_addr);
        textView_type = (TextView)view.findViewById(R.id.ent_type);
        textView_tel = (TextView)view.findViewById(R.id.ent_tel);
        textView_bef = (TextView)view.findViewById(R.id.ent_bef);

        EnterpriseInfoItem ei = CachePot.getInstance().pop(EnterpriseInfoItem.class);
        setInfo(ei);

        // Inflate the layout for this fragment
        return view;
    }

    void setInfo(EnterpriseInfoItem ei){
        textView_name.setText(ei.getName());
        textView_type.setText(ei.getType());
        textView_addr.setText(ei.getAddr());
        textView_tel.setText(ei.getTel());
        textView_bef.setText(ei.getBef());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
    }
}
