package com.example.roy.oracle.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roy.oracle.Activities.MainActivity;
import com.example.roy.oracle.Connection.Connection;
import com.example.roy.oracle.Connection.MessageParse;
import com.example.roy.oracle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

import static com.example.roy.oracle.Connection.Constants.URL;
import static com.example.roy.oracle.Connection.Constants.URL_TWEETS;

public class TweetsCollectingFragment extends Fragment {
    private TextView start, text;
    private EditText keywords;

    public TweetsCollectingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_tweets_collecting, container, false);
        start = view.findViewById(R.id.start);
        text = view.findViewById(R.id.text);
        keywords = view.findViewById(R.id.keywords);
        final Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String words = keywords.getText().toString();
                if(!words.isEmpty()) {

                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.success_popup);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    ImageView ok = (ImageView) dialog.findViewById(R.id.popup_ok);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                    dialog.show();

                    FormBody.Builder formBuilder = new FormBody.Builder();
                    Log.e("WORDS", words);
                    Connection apiCall = new Connection(formBuilder, URL_TWEETS + "/" + words, new MessageParse() {
                        @Override
                        public void parseResponse(String response) {
                            Log.e("RESPONSE 2", response);
                        }

                    }, getActivity(), "GET", 0);
                    apiCall.execute();

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Fragment currentShowingFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frame);
                            fragmentTransaction.hide(currentShowingFragment);
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.frame, new PreprocessingFragment());
                            fragmentTransaction.addToBackStack("");
                            fragmentTransaction.commit();

                            ((MainActivity)getActivity()).loadHomeFragment();
                        }
                    });
                }
                else {
                    keywords.startAnimation(shake);
                    text.startAnimation(shake);
                }
            }
        });
        return view;
    }


}
