package com.bigpicture.team.dadungi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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

    EnterpriseInfoItem ei;

    public static MapInfoFragment newInstance(){
        MapInfoFragment f = new MapInfoFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_info, container, false);
        view.setClickable(true);

        textView_name = (TextView)view.findViewById(R.id.ent_name);
        textView_addr = (TextView)view.findViewById(R.id.ent_addr);
        textView_type = (TextView)view.findViewById(R.id.ent_type);
        textView_tel = (TextView)view.findViewById(R.id.ent_tel);
        textView_bef = (TextView)view.findViewById(R.id.ent_bef);

        ei = CachePot.getInstance().pop(EnterpriseInfoItem.class);
        setInfo(ei);

        textView_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setMessage("전화를 연결하시겠습니까?");

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + ei.getTel().replace(")","-")));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }

                });
                if(ei.getTel().length()>1)
                    alert.show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    void setInfo(EnterpriseInfoItem ei){
        textView_name.setText(ei.getName());
        textView_type.setText(ei.getType());
        textView_addr.setText(ei.getAddr());
        if(ei.getTel().length()>1) {
            textView_tel.setText(ei.getTel());
            textView_tel.setTextColor(Color.BLUE);
            textView_tel.setTypeface(null, Typeface.BOLD);
        }
        textView_bef.setText(ei.getBef());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
    }
}
