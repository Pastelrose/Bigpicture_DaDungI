package com.bigpicture.team.dadungi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigpicture.team.dadungi.item.SearchItem;
import com.github.kimkevin.cachepot.CachePot;


public class SearchListFragment extends Fragment {

    EditText editText;
    Button button;

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    ListFragment lf;
    boolean firstSearch = false;

    public static SearchListFragment newInstance() {
        SearchListFragment fragment = new SearchListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_list, container, false);

        editText = (EditText)v.findViewById(R.id.searchbyname);
        button = (Button)v.findViewById(R.id.btnSearch);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name=editText.getText().toString();
                SearchItem si = new SearchItem(name);
                Toast.makeText(getActivity(), name,Toast.LENGTH_SHORT).show();
                CachePot.getInstance().push(si);

                fm = getChildFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                lf = new ListFragment();

                if (firstSearch) {
                    fragmentTransaction.add(R.id.fragment, lf).commit();
                    firstSearch=false;
                }else{
                    fragmentTransaction.replace(R.id.fragment, lf).commit();
                }
            }
        });

        return v;
    }
}
