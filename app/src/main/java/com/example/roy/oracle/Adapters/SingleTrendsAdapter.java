package com.example.roy.oracle.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.roy.oracle.Activities.MainActivity;
import com.example.roy.oracle.Models.Trend;
import com.example.roy.oracle.R;
import com.example.roy.oracle.fragments.ChartFragment;
import com.example.roy.oracle.fragments.TrendItemFragment;

import java.util.List;

/**
 * Created by Roy on 3/26/2018.
 */

public class SingleTrendsAdapter extends BaseAdapter {
    private List<Trend> trends;
    private Activity activity;
    private Context context;

    public SingleTrendsAdapter(Activity activity, Context context, List<Trend> trends) {
        this.activity = activity;
        this.context = context;
        this.trends = trends;
    }

    @Override
    public int getCount() {
        return trends.size();
    }

    @Override
    public Object getItem(int i) {
        return trends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int i, final View view, ViewGroup viewGroup) {
        final View view1 = activity.getLayoutInflater().inflate(R.layout.trend_item, null);
        final Animation animScrollDown = AnimationUtils.loadAnimation(context, R.anim.slide_from_bottom);
        final Animation animScrollUp = AnimationUtils.loadAnimation(context, R.anim.slide_down);

        ListView list = activity.findViewById(R.id.list1);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {}

            private int lastVisibleItem = 0;
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > lastVisibleItem) {
                    // scroll down
                    view1.startAnimation(animScrollDown);
                } else if (firstVisibleItem < lastVisibleItem) {
                    // scroll up
                    view1.startAnimation(animScrollUp);
                }
                lastVisibleItem = firstVisibleItem;
            }
        });

        Trend trend = trends.get(i);
        final TextView name = view1.findViewById(R.id.name);
        TextView count = view1.findViewById(R.id.count);
        TextView image = view1.findViewById(R.id.place);
        ImageView search = view1.findViewById(R.id.search);
        ImageView sentiment = view1.findViewById(R.id.sentiment);
        name.setText(trend.getName().toString().substring(0,1).toUpperCase() + trend.getName().toString().substring(1));
        count.setText(trend.getNum());
        image.setText(String.valueOf(trend.getFlag() + 1));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrendItemFragment search = new TrendItemFragment(name.getText().toString());
                ((MainActivity) activity).changeFragment(search);
            }
        });

        sentiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChartFragment chart = new ChartFragment("single", i);
                ((MainActivity) activity).changeFragment(chart);
            }
        });

        return view1;
    }
}
