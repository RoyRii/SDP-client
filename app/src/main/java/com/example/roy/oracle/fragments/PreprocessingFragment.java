package com.example.roy.oracle.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.roy.oracle.Activities.MainActivity;
import com.example.roy.oracle.Connection.Connection;
import com.example.roy.oracle.Connection.MessageParse;
import com.example.roy.oracle.R;

import okhttp3.FormBody;

import static com.example.roy.oracle.Connection.Constants.URL;
import static com.example.roy.oracle.Connection.Constants.URL_TWEETS;

public class PreprocessingFragment extends Fragment {
    private TextView info;
    private Button check;
    private LinearLayout layout;
    private ProgressBar progressBar;

    public PreprocessingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preprocessing, container, false);
        check = view.findViewById(R.id.check);
        info = view.findViewById(R.id.info);
        layout = view.findViewById(R.id.layout);
        progressBar = view.findViewById(R.id.bar);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setAlpha(0.3f);
                progressBar.setVisibility(View.VISIBLE);

                FormBody.Builder formBuilder = new FormBody.Builder();
                Connection apiCall = new Connection(formBuilder, URL + "/info", new MessageParse() {

                    @Override
                    public void parseResponse(String response) {
                        Log.e("RESPONSE", response);

                        if (!response.equals("Error !!!")) {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.info_popup);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            ImageView ok = (ImageView) dialog.findViewById(R.id.popup_ok);
                            ImageView no = (ImageView) dialog.findViewById(R.id.popup_no);
                            TextView tweets_info = dialog.findViewById(R.id.tweets_info);
                            tweets_info.setText("You have collected " + response + " tweets so far!");
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    info.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                }
                            });

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    info.setVisibility(View.VISIBLE);
                                    info.setText("Your data is processing, please, wait, it can take some time... Thanks!");
                                    progressBar.setVisibility(View.VISIBLE);
                                    check.setVisibility(View.INVISIBLE);
                                    dialog.dismiss();


                                    FormBody.Builder formBuilder = new FormBody.Builder();
                                    Connection apiCall = new Connection(formBuilder, URL + "/process", new MessageParse() {
                                        @Override
                                        public void parseResponse(String response) {
                                            if (response.equals("done")) {
                                                info.setText("Successfully done ! Now you can check results!");
                                                progressBar.setVisibility(View.GONE);
                                                check.setVisibility(View.VISIBLE);
                                                check.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        ((MainActivity)getActivity()).changeFragmentNoButton(new HomeFragment());
                                                    }
                                                });
                                            }
                                        }
                                    }, getActivity(), "GET", 1);
                                    apiCall.execute();

                                }
                            });
                            info.setVisibility(View.INVISIBLE);
                            layout.setAlpha(1);
                            progressBar.setVisibility(View.GONE);
                            dialog.show();
                        } else {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.error_popup);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            ImageView ok = (ImageView) dialog.findViewById(R.id.popup_ok);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    info.setVisibility(View.VISIBLE);
                                    layout.setAlpha(1);
                                    progressBar.setVisibility(View.GONE);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }

                }, getActivity(), "GET", 1);
                apiCall.execute();
            }
        });
        return view;
    }
}