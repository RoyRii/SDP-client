package com.example.roy.oracle.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.roy.oracle.Activities.MainActivity;
import com.example.roy.oracle.FragmentVisibility;
import com.example.roy.oracle.R;
import com.example.roy.oracle.fragments.Co_occurent_Trends_Fragment;
import com.example.roy.oracle.fragments.SingleTrendsFragment;

import java.util.List;

public class TrendsListFragment extends Fragment {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private int request;

    @SuppressLint("ValidFragment")
    public TrendsListFragment(int request) {
        this.request = request;
    }

    public TrendsListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_trends_list, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        fragmentBecameVisible();

        return view;
    }


    public void fragmentBecameVisible() {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float v, final int i2) {}

            @Override
            public void onPageSelected(final int position) {
                FragmentVisibility fragment = (FragmentVisibility) mViewPager.getAdapter().instantiateItem(mViewPager, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }

            @Override
            public void onPageScrollStateChanged(final int position) {}
        });

        tabLayout.setupWithViewPager(mViewPager);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SingleTrendsFragment tab1 = new SingleTrendsFragment(request);
                    return tab1;
                case 1:
                    Co_occurent_Trends_Fragment tab2 = new Co_occurent_Trends_Fragment(request);
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Single";
                case 1:
                    return "Co-occurrence";
            }
            return null;
        }

    }
}
