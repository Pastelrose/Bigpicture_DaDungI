package com.bigpicture.team.dadungi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class CardInfoFragment extends Fragment {

    RadioButton radioButton1, radioButton2;
    RadioGroup radioGroup;
    WebView webView;


    public static CardInfoFragment newInstance() {
        CardInfoFragment fragment = new CardInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_info, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.menu3);

        radioButton1 = (RadioButton)v.findViewById(R.id.radioButton);
        radioButton2 = (RadioButton)v.findViewById(R.id.radioButton2);
        radioButton1.setOnClickListener(radioButtonClickListener);
        radioButton2.setOnClickListener(radioButtonClickListener);
        radioGroup = (RadioGroup)v.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        webView = (WebView)v.findViewById(R.id.webView);

        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());

        showWeb(R.id.radioButton);

        return v;
    }

    RadioButton.OnClickListener radioButtonClickListener = new RadioButton.OnClickListener(){
        @Override
        public void onClick(View view) {
        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                showWeb(i);
        }
    };

    public void showWeb(int id){
        if(id==R.id.radioButton){
            webView.loadUrl("file:///android_asset/creditcard.html");
        }
        else if(id==R.id.radioButton2){
            webView.loadUrl("file:///android_asset/checkcard.html");
        }
    }

    private class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
