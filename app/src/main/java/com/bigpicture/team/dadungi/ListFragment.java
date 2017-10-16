package com.bigpicture.team.dadungi;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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


public class ListFragment extends Fragment {

    ListView listView;
    MyListAdapter myListAdapter;
    ArrayList<EnterpriseInfoItem> list_itemArrayList = new ArrayList<EnterpriseInfoItem>();

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
        setListView(si);

        return v;
    }
    public void setListView(SearchItem si){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<EnterpriseInfoItem>> call = remoteService.listEnterpriseInfo("any","any","'%"+si.getName()+"%'");
        call.enqueue(new Callback<ArrayList<EnterpriseInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<EnterpriseInfoItem>> call,
                                   Response<ArrayList<EnterpriseInfoItem>> response) {
                ArrayList<EnterpriseInfoItem> list = response.body();

                if (list == null) {
                    list = new ArrayList<>();
                }

                if (response.isSuccessful()) {
                    list_itemArrayList = list;
                    myListAdapter = new MyListAdapter(getActivity(),list_itemArrayList);
                    listView.setAdapter(myListAdapter);
                } else {}
            }

            @Override
            public void onFailure(Call<ArrayList<EnterpriseInfoItem>> call, Throwable t) {
            }
        });
    }

}
