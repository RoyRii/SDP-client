package com.example.roy.oracle.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roy.oracle.Connection.Connection;
import com.example.roy.oracle.Connection.MessageParse;
import com.example.roy.oracle.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

import static com.example.roy.oracle.Connection.Constants.URL;

public class ChartFragment extends Fragment {
    private PieChart pieChart;
    private float pos, neu, neg;
    private String type;
    private int position;

    public ChartFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ChartFragment(String type, int position) {
        this.type = type;
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        pieChart = view.findViewById(R.id.chart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Sentiment Pie Chart");
        pieChart.setCenterTextSize(9);
        pieChart.setCenterTextColor(R.color.colorAccent);
        pieChart.setDrawEntryLabels(true);

        FormBody.Builder formBuilder = new FormBody.Builder();
        Connection apiCall = new Connection(formBuilder, URL + "/sentiment/" + type + "/" + position, new MessageParse() {
            @Override
            public void parseResponse(String response) {
                Log.e("RES", response);
                try {
                    JSONObject o = new JSONObject(response);
                    JSONArray array = o.getJSONArray("res");
                    double pos1 = 0, neu1 = 0, neg1 = 0;

                    if(type.equals("single")) {
                        pos1 = array.getDouble(0);
                        neu1 = array.getDouble(1);
                        neg1 = array.getDouble(2);
                    } else if(type.equals("occur")) {
                        double pos11 = array.getDouble(0);
                        double neu11 = array.getDouble(1);
                        double neg11 = array.getDouble(2);
                        double pos22 = array.getDouble(3);
                        double neu22 = array.getDouble(4);
                        double neg22 = array.getDouble(5);

                        pos1 = ((pos11 + pos22) / 2);
                        neu1 = ((neu11 + neu22) / 2);
                        neg1 = ((neg11 + neg22) / 2);
                    }

                    pos = (float) pos1;
                    neu = (float) neu1;
                    neg = (float) neg1;
                    Log.e("VAL", pos + " " + neu + " " + neg);

                    addData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), "GET", 1);
        apiCall.execute();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
            }

            @Override
            public void onNothingSelected() {
            }
        });

        return view;
    }

    private void addData() {
        List<PieEntry> yEntries = new ArrayList<>();
        List<String> xEntries = new ArrayList<>();

        Log.e("VAL2", pos + " " + neu + " " + neg);

        yEntries.add(new PieEntry(pos));
        yEntries.add(new PieEntry(neu));
        yEntries.add(new PieEntry(neg));

        xEntries.add("Positive");
        xEntries.add("Neutral");
        xEntries.add("Negative");

        PieDataSet pieData = new PieDataSet(yEntries, "Green - Positive\n" +
                "Blue - Neutral\n" +
                "Red - Negative\n");
        pieData.setSliceSpace(3);
        pieData.setValueTextSize(16);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.RED);

        pieData.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        // create pie data object
        PieData data = new PieData(pieData);
        pieChart.setData(data);
        pieChart.invalidate();
    }

}
