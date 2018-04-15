package com.example.roy.oracle.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.roy.oracle.Activities.MainActivity;
import com.example.roy.oracle.R;
import com.example.roy.oracle.fragments.TrendItemFragment;

import java.util.List;

public class FavoriteAdapter extends BaseAdapter {
    private List<String> favorites, urls, numbers, users;
    private Activity activity;
    private Context context;

    public FavoriteAdapter(FragmentActivity activity, Context context, List<String> favorites, List<String> urls, List<String> numbers, List<String> users) {
        this.activity = activity;
        this.context = context;
        this.favorites = favorites;
        this.urls = urls;
        this.numbers = numbers;
        this.users = users;
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public Object getItem(int i) {
        return favorites.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View view1 = activity.getLayoutInflater().inflate(R.layout.favorited_item, null);
        String s = favorites.get(i);
        String user = users.get(i);
        String number = numbers.get(i);
        TextView text = view1.findViewById(R.id.text);
        text.setText(s);
        TextView username = view1.findViewById(R.id.username);
        username.setText(user);
        TextView num = view1.findViewById(R.id.number);
        num.setText(number);

        final Animation animScrollDown = AnimationUtils.loadAnimation(context, R.anim.slide_from_bottom);
        final Animation animScrollUp = AnimationUtils.loadAnimation(context, R.anim.slide_down);

        ListView list = activity.findViewById(R.id.list);
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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TrendItemFragment search = new TrendItemFragment(urls.get(i), 1);
                ((MainActivity) activity).changeFragment(search);
            }
        });

        return view1;
    }
}
