package com.example.mytrainingschedules.activities.mainactivity.premium;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.appintro.IntroActivity;
import com.example.mytrainingschedules.activities.appintro.SplashActivity;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.example.mytrainingschedules.activities.mainactivity.home.CustomAdapter;
import com.example.mytrainingschedules.activities.mainactivity.home.ViewSchedule;
import com.example.mytrainingschedules.activities.mainactivity.user.UserPageActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PremiumFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainAdapter mainAdapter;

    /*
     * getActivity() --> MainActivity
     * root          --> HomeFragment
     */

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.premium_fragment, container, false);

        tabLayout=root.findViewById(R.id.tab_layout);
        viewPager=root.findViewById(R.id.view_pager);

        mainAdapter=new MainAdapter(getChildFragmentManager());


        mainAdapter.AddFragment(new SuggestedFragment(), "Suggested");
        mainAdapter.AddFragment(new PopularFragment(), "Popular");

        viewPager.setAdapter(mainAdapter);

        tabLayout.setupWithViewPager(viewPager);


        return root;
    }

    private class MainAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
        ArrayList<String> stringArrayList=new ArrayList<>();


        public void AddFragment( Fragment fragment, String s){
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }


        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return stringArrayList.get(position);
        }
    }


}