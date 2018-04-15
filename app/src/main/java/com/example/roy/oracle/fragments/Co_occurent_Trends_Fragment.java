package com.example.roy.oracle.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.roy.oracle.Adapters.Co_occurrence_adapter;
import com.example.roy.oracle.Adapters.SingleTrendsAdapter;
import com.example.roy.oracle.Connection.Connection;
import com.example.roy.oracle.Connection.MessageParse;
import com.example.roy.oracle.FragmentVisibility;
import com.example.roy.oracle.Models.Trend;
import com.example.roy.oracle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

import static com.example.roy.oracle.Connection.Constants.URL;

public class Co_occurent_Trends_Fragment extends Fragment implements FragmentVisibility {
    private ListView list;
    private int request, visible_position, flag;
    private ProgressBar bar;
    private TextView footer_text;
    private LinearLayout footer;

    public Co_occurent_Trends_Fragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Co_occurent_Trends_Fragment(int request) {
        this.request = request;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_co_occurent__trends_, container, false);
        list = view.findViewById(R.id.list2);
        final View footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        footer = footerView.findViewById(R.id.footer_layout);
        footer_text = footerView.findViewById(R.id.footer_text);
        flag = 0;
        if(request==5) {
            list.addFooterView(footerView);
            footer_text.setText("+load 5 more...");
        }
        else if(request==10) {
            list.addFooterView(footerView);
            footer_text.setText("+load 10 more...");
        }
        bar = view.findViewById(R.id.progress);
        bar.setVisibility(View.VISIBLE);
        fragmentBecameVisible();

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_more(footerView);
            }
        });

        return view;
    }

    private void load_more(final View footerView) {
        if(request==5) {
            request = 10;
            flag = 1;
            visible_position = list.getLastVisiblePosition();
            fragmentBecameVisible();
            footer_text.setText("+load 10 more...");
        } else if(request==10) {
            request = 20;
            flag = 1;
            visible_position = list.getLastVisiblePosition();
//            list.setVisibility(View.INVISIBLE);
//            bar.setVisibility(View.VISIBLE);
            fragmentBecameVisible();
            list.removeFooterView(footerView);

//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    list.setVisibility(View.VISIBLE);
//                    bar.setVisibility(View.GONE);
//                }
//            }, 100);
        }
    }

    @Override
    public void fragmentBecameVisible() {
        FormBody.Builder formBuilder = new FormBody.Builder();
        Connection apiCall = new Connection(formBuilder, URL + "/trends", new MessageParse() {
            @Override
            public void parseResponse(String response) {
                Log.e("RESPONSE", response);
                bar.setVisibility(View.GONE);
                try {
                    JSONObject o = new JSONObject(response);
                    JSONArray array = o.getJSONArray("response");
                    Log.e("ARray", String.valueOf(array));
                    List<String> trends1 = new ArrayList<>();
                    List<String> trends_num = new ArrayList<>();
                    List<Trend> trends_list = new ArrayList<>();

                    for (int i = 2; i < 4; i++) {
                        JSONObject obj = array.getJSONObject(i);
                        Log.e("obj", String.valueOf(obj));
                        JSONArray tr_array = obj.getJSONArray("res");
                        String tr="";
                        for(int j = 0; j < tr_array.length(); j++) {
                            String tr1 = tr_array.getString(j);
                            if (i==2) {
                                if(j%2==0) {
                                    tr += tr1 + " - ";
                                }
                                else if(j%2==1) {
                                    tr += tr1;
                                    trends1.add(tr);
                                    tr = "";
                                }
                            }
                            if (i==3) {
                                trends_num.add(tr1);
                            }
                        }
                    }

                    for (int x = 0; x<request; x++) {
                        Trend trend_model = new Trend();
                        trend_model.setName(trends1.get(x));
                        trend_model.setNum(trends_num.get(x));
                        trend_model.setFlag(x);
                        trends_list.add(trend_model);
                    }

                    Co_occurrence_adapter adapter = new Co_occurrence_adapter(getActivity(), getContext(), trends_list);
                    list.setAdapter(adapter);
                    if(flag==1) {
                        list.setSelectionFromTop(visible_position-1, 0);
                        flag = 0;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), "GET", 1);
        apiCall.execute();
    }
}
