package com.bigpicture.team.dadungi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigpicture.team.dadungi.adapter.SpinnerAdapter;
import com.bigpicture.team.dadungi.item.SearchItem;
import com.github.kimkevin.cachepot.CachePot;


public class SearchListFragment extends Fragment {

    EditText editText;
    Button button;
    int pos;
    String type, district;

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

        Spinner s1 = (Spinner) v.findViewById(R.id.typeSpinner);
        String[] types= getResources().getStringArray(R.array.types_search);

        SpinnerAdapter s1Adapter = new SpinnerAdapter(getActivity(),android.R.layout.simple_spinner_item, types);
        s1.setAdapter(s1Adapter);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                pos = position;
                type=parent.getItemAtPosition(pos).toString();
                Toast.makeText(getActivity(),parent.getItemAtPosition(pos).toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner s2 = (Spinner) v.findViewById(R.id.districtSpinner);
        final String[] districts= getResources().getStringArray(R.array.district_search);

        SpinnerAdapter s2Adapter = new SpinnerAdapter(getActivity(),android.R.layout.simple_spinner_item, districts);
        s2.setAdapter(s2Adapter);
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                pos = position;
                district =parent.getItemAtPosition(pos).toString();
                Toast.makeText(getActivity(),parent.getItemAtPosition(pos).toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name=editText.getText().toString();
                SearchItem si = new SearchItem(name,type,district);
                Toast.makeText(getActivity(),name+" "+type,Toast.LENGTH_SHORT).show();
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
