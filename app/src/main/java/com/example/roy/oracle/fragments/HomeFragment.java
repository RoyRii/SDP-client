package com.example.roy.oracle.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.roy.oracle.Activities.MainActivity;
import com.example.roy.oracle.Activities.SearchActivity;
import com.example.roy.oracle.Connection.Connection;
import com.example.roy.oracle.Connection.MessageParse;
import com.example.roy.oracle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

import static com.example.roy.oracle.Connection.Constants.URL;

public class HomeFragment extends Fragment {
    private Activity activity;
    private SeekBar seekBar;
    private Button trends_button;
    private TextView textView;
    private int seekBarMinimum = 0;
    private int request = 0;
    private LinearLayout layout;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        layout = view.findViewById(R.id.custom_range);
        seekBar = view.findViewById(R.id.seekbar);
        seekBar.setVisibility(View.VISIBLE);
        trends_button = view.findViewById(R.id.trends_button);
        textView = view.findViewById(R.id.textview);
        final Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

        final int[] arr = new int[]{0, 5, 10, 20};
        seekBar.setMax(arr.length-1);
        textView.setText(seekBarMinimum + " trends");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textView.setText(arr[progress]+" trends");
                request = arr[progress];
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        trends_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent i = new Intent(activity, SearchActivity.class);
//                startActivity(i);

                if (request != 0) {
                    Log.e("REQUEST", String.valueOf(request));
                    TrendsListFragment fr = new TrendsListFragment(request);
                    ((MainActivity) activity).changeFragment(fr);

                } else {
                    layout.startAnimation(shake);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
