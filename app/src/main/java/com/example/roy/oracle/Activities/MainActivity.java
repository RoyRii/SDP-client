package com.example.roy.oracle.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roy.oracle.R;
import com.example.roy.oracle.fragments.RetweetsFragment;
import com.example.roy.oracle.fragments.FavoritesFragment;
import com.example.roy.oracle.fragments.HomeFragment;
import com.example.roy.oracle.fragments.PreprocessingFragment;
import com.example.roy.oracle.fragments.TrendItemFragment;
import com.example.roy.oracle.fragments.TweetsCollectingFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    public static final String TAG_HOME = "Home";
    private static final String TAG_RETRIEVE = "Retrieving";
    private static final String TAG_PROCESS = "Processing";
    private static final String TAG_RETWEETS = "Most Retweeted";
    private static final String TAG_LIKES = "Most Favorite";
    private static final String TAG_ABOUT_US = "About us";
    private static final String TAG_LOGOUT = "Log our";
    public static String CURRENT_TAG = TAG_HOME;
    private Handler mHandler;
    public static NavigationView navigationView;
    public static MainActivity activity;

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private Fragment currentFragment;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private static ActionBar actionBar;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    private SharedPreferences activitySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        mHandler = new Handler();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences pref = getSharedPreferences("roy", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        if(!pref.contains("username")) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        Intent i = getIntent();
        String name = i.getStringExtra("username");
        if (pref.contains("logged")) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.login_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ImageView ok = (ImageView) dialog.findViewById(R.id.popup_ok);
            ImageView no = (ImageView) dialog.findViewById(R.id.popup_no);
            TextView text = dialog.findViewById(R.id.login_popup_text);
            text.setText("Good day, " + name);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putString("logged", null).commit();
                    editor.commit();
                    dialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putString("logged", null).commit();
                    dialog.dismiss();
                    TweetsCollectingFragment fr = new TweetsCollectingFragment();
                    changeFragment(fr);

                }
            });
            dialog.show();
        }

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();

        }

    }


    public void loadHomeFragment() {
        selectNavMenu();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (getSupportFragmentManager().findFragmentById(R.id.frame) != null)
                    fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame));
                Fragment fragment = getHomeFragment();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fragmentTransaction.addToBackStack(fragment.getTitle());

                fragmentTransaction.commit();

            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                currentFragment = homeFragment;
                return homeFragment;
            case 1:
               TweetsCollectingFragment tweetsCollectingFragment = new TweetsCollectingFragment();
               currentFragment = tweetsCollectingFragment;
               return tweetsCollectingFragment;
            case 2:
                // orders
                PreprocessingFragment procc = new PreprocessingFragment();
                currentFragment = procc;
                return procc;
            case 3:
                RetweetsFragment retweetsFragment = new RetweetsFragment();
                currentFragment = retweetsFragment;
                return retweetsFragment;

            case 4:
                FavoritesFragment fr = new FavoritesFragment();
                currentFragment = fr;
                return fr;
            case 5:
                TrendItemFragment info = new TrendItemFragment(-1);
                currentFragment = info;
                return info;
            case 6:
                Log.e("LOG OUT ", "---");
                SharedPreferences pref = getSharedPreferences("roy", MODE_PRIVATE);
                pref.edit().clear().commit();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();

            default:
                HomeFragment homeFragment1 = new HomeFragment();
                currentFragment = homeFragment1;
                return homeFragment1;
        }
    }


//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                } else if (id == R.id.nav_tweets) {
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_RETRIEVE;
                } else if (id == R.id.nav_process) {
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_PROCESS;
                } else if (id == R.id.nav_retweets) {
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_RETWEETS;
                } else if (id == R.id.nav_fav) {
                    navItemIndex = 4;
                    CURRENT_TAG = TAG_LIKES;
                } else if (id == R.id.nav_info) {
                    navItemIndex = 5;
                    CURRENT_TAG = TAG_ABOUT_US;
                } else if (id == R.id.nav_logout) {
                    navItemIndex = 6;
                    CURRENT_TAG = TAG_LOGOUT;
                } else navItemIndex = 0;

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }


    @SuppressLint("NewApi")
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    public void changeFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        Fragment currentShowingFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        fragmentTransaction.hide(currentShowingFragment);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
        showUpButton(true);
        loadHomeFragment();
    }

    public void changeFragmentNoButton(Fragment fragment) {

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        Fragment currentShowingFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        fragmentTransaction.hide(currentShowingFragment);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
        showUpButton(false);
        loadHomeFragment();
    }

    private void showUpButton(boolean show) {
        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if (show) {
            // Remove hamburger
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            // Show back button
            actionBar.setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            // Remove back button
            actionBar.setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            actionBarDrawerToggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

            if (backStackCount >= 1) {
                getSupportFragmentManager().popBackStack();
                // Change to hamburger icon if at bottom of stack
                if (backStackCount == 1) {
                    showUpButton(false);
                }
            } else {
                super.onBackPressed();
//                    setTitle(getResources().getString(R.string.home));

                if (backStackCount >= 1) {
                    getSupportFragmentManager().popBackStack();
//                    setTitle(getResources().getString(R.string.home));

//                onBackPressedListener.doBack();
                    // Change to hamburger icon if at bottom of stack
                    if (backStackCount == 1) {
                        showUpButton(false);
//                        setTitle(getResources().getString(R.string.home));

                    }
                }
            }

        }
    }
}
