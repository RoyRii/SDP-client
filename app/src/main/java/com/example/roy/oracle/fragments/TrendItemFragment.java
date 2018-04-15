package com.example.roy.oracle.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.roy.oracle.R;


public class TrendItemFragment extends Fragment {
    private String filter;
    private int flag=0;

    public TrendItemFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public TrendItemFragment(int flag) {
        this.flag = flag;
    }

    @SuppressLint("ValidFragment")
    public TrendItemFragment(String filter, int flag) {
        this.filter = filter;
        this.flag = flag;
    }

    @SuppressLint("ValidFragment")
    public TrendItemFragment(String filter) {
        this.filter = filter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trend_item, container, false);
        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        if(flag==0)
            webView.loadUrl("https://www.google.com/search?q=" + filter);
        else if(flag == -1) webView.loadUrl("https://3991royal.wixsite.com/mysite");
        else webView.loadUrl(filter);

        return view;
    }

}
