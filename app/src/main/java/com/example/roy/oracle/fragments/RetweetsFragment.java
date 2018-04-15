package com.example.roy.oracle.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.roy.oracle.Adapters.RetweetAdapter;
import com.example.roy.oracle.Connection.Connection;
import com.example.roy.oracle.Connection.MessageParse;
import com.example.roy.oracle.FragmentVisibility;
import com.example.roy.oracle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

import static com.example.roy.oracle.Connection.Constants.URL;

public class RetweetsFragment extends Fragment implements FragmentVisibility {
    private ListView list;
    private ProgressBar bar;

    public RetweetsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_retweets, container, false);
        list = view.findViewById(R.id.list);
        bar = view.findViewById(R.id.bar);
        fragmentBecameVisible();
        return view;
    }


    @Override
    public void fragmentBecameVisible() {
        bar.setVisibility(View.VISIBLE);
        FormBody.Builder formBuilder = new FormBody.Builder();
        Connection apiCall = new Connection(formBuilder, URL + "/retweets", new MessageParse() {
            @Override
            public void parseResponse(String response) {
                Log.e("RESPONSE", response);

                try {
                    JSONObject o = new JSONObject(response);
                    JSONArray array = o.getJSONArray("res");
                    JSONArray urls_array = o.getJSONArray("urls");
                    JSONArray numbers_array = o.getJSONArray("numbers");
                    JSONArray users_array = o.getJSONArray("users");
                    List<String> retweets = new ArrayList<>();
                    List<String> urls = new ArrayList<>();
                    List<String> numbers = new ArrayList<>();
                    List<String> users = new ArrayList<>();
                    for(int i = 0; i < array.length(); i++) {
                        String s = array.getString(i);
                        retweets.add(s);
                    }
                    for (int i = 0; i < urls_array.length(); i++) {
                        String s = urls_array.getString(i);
                        urls.add(s);
                    }
                    for(int i = 0; i < numbers_array.length(); i++) {
                        String s = numbers_array.getString(i);
                        numbers.add(s);
                    }
                    for(int i = 0; i < users_array.length(); i++) {
                        String s = users_array.getString(i);
                        users.add(s);
                    }

                    RetweetAdapter adapter = new RetweetAdapter(getActivity(), getContext(), retweets, urls, numbers, users);
                    list.setAdapter(adapter);
                    bar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), "GET", 1);
        apiCall.execute();
    }
}
