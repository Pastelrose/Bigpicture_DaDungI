package com.bigpicture.team.dadungi;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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


public class ListFragment extends Fragment implements AbsListView.OnScrollListener{

    ListView listView;
    MyListAdapter myListAdapter;
    ArrayList<EnterpriseInfoItem> list_itemArrayList = new ArrayList<EnterpriseInfoItem>();
    ArrayList<EnterpriseInfoItem> list;
    int OFFSET = 20;
    boolean lastItemVisibleFlag = false;
    int page = 1;
    boolean mLockListView = false;
    ProgressBar progressBar;

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        listView = (ListView)v.findViewById(R.id.my_listView);
        SearchItem si = CachePot.getInstance().pop(SearchItem.class);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);

        myListAdapter = new MyListAdapter(getActivity(),list_itemArrayList);
        listView.setAdapter(myListAdapter);
        progressBar.setVisibility(View.GONE);

        getList(si);

        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CachePot.getInstance().push(list_itemArrayList.get(position));
                Intent intent = new Intent(getActivity(),DescActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {

            progressBar.setVisibility(View.VISIBLE);

            getItem();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    public void getItem(){
        mLockListView = true;

        for(int i=page*OFFSET; i<page*OFFSET+20&&i<list.size(); i++){
            list_itemArrayList.add(i,list.get(i));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                myListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        },1000);
    }


    public void getList(SearchItem si){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<EnterpriseInfoItem>> call = remoteService.listEnterpriseInfo(si.getType(),"'%"+si.getDistrict()+"%'","'%"+si.getName()+"%'");
        call.enqueue(new Callback<ArrayList<EnterpriseInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<EnterpriseInfoItem>> call,
                                   Response<ArrayList<EnterpriseInfoItem>> response) {
                list = response.body();

                if (list == null) {
                    list = new ArrayList<>();
                }

                if (response.isSuccessful()) {
                    for(int i=0;i<OFFSET&&i<list.size();i++){
                        list_itemArrayList.add(i,list.get(i));
                    }
                    myListAdapter.notifyDataSetChanged();
                } else {}
            }

            @Override
            public void onFailure(Call<ArrayList<EnterpriseInfoItem>> call, Throwable t) {
            }
        });
    }

}
