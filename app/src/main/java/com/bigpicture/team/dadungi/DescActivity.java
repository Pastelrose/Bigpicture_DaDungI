package com.bigpicture.team.dadungi;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigpicture.team.dadungi.adapter.MyListAdapter;
import com.bigpicture.team.dadungi.item.EnterpriseInfoItem;
import com.bigpicture.team.dadungi.item.SearchItem;
import com.bigpicture.team.dadungi.remote.RemoteService;
import com.bigpicture.team.dadungi.remote.ServiceGenerator;
import com.github.kimkevin.cachepot.CachePot;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescActivity extends AppCompatActivity {

    //툴바 적용이 안됨
    EnterpriseInfoItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EnterpriseInfoItem ei = CachePot.getInstance().pop(EnterpriseInfoItem.class);

        searchInfo(ei);

        setContentView(R.layout.activity_desc);
    }

    public void searchInfo(EnterpriseInfoItem ei){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<EnterpriseInfoItem> call = remoteService.selectEnterpriseInfo(ei.getSeq());
        call.enqueue(new Callback<EnterpriseInfoItem>() {
            @Override
            public void onResponse(Call<EnterpriseInfoItem> call,
                                   Response<EnterpriseInfoItem> response) {
                EnterpriseInfoItem enterItem = response.body();

                if (response.isSuccessful()) {
                    item = enterItem;
                    setView();
                } else {}
            }

            @Override
            public void onFailure(Call<EnterpriseInfoItem> call, Throwable t) {
            }
        });
    }

    public void setView(){
        ((TextView) findViewById(R.id.coopName)).setText(item.getName());
        ((TextView) findViewById(R.id.coopType)).setText(item.getType());
        ((TextView) findViewById(R.id.tel)).setText(item.getTel());
        ((TextView) findViewById(R.id.benDesc)).setText(item.getBef());
        ((TextView) findViewById(R.id.coopAddr)).setText(item.getAddr());
    }
}
