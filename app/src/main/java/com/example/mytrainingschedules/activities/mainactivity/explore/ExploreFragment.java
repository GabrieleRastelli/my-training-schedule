package com.example.mytrainingschedules.activities.mainactivity.explore;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.mytrainingschedules.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This fragment is the one who handles both the "SUGGESTED" and "POPULAR" fragments creation and switching.
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class ExploreFragment extends Fragment implements AdapterView.OnItemClickListener {

    private String TAG="ExploreFragment";
    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainAdapter mainAdapter;
    private GridView gridView;
    private ScheduleFilterAdapter scheduleFilterAdapter;
    private Map<Integer, Boolean> clickedFilter; /* keeps track of which are the filtering buttons clicked */
    private List<String> exerciseToDisplay; /* keeps track of which are exercises categories to display */
    private static String[] EXERCISE_LABELS={"CHEST", "ARMS", "BACK", "LEGS", "SHOULDERS","ABS"};
    private static int[] EXERCISE_DRAWABLE={R.drawable.man_doing_chest, R.drawable.man_doing_biceps, R.drawable.man_doing_back, R.drawable.man_doing_squats, R.drawable.man_doing_shoulders, R.drawable.woman_doing_core};
    /*
     * getActivity() --> MainActivity
     * root          --> HomeFragment
     */

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.premium_fragment, container, false);

        clickedFilter=new HashMap<>();
        exerciseToDisplay=new ArrayList<>();

        /* non è stato clickato nessun filtro */
        for(Integer i=0;i<6;i++) {
            clickedFilter.put(i, false);
        }

        initGui(root);

        return root;
    }

    private void initGui(View root){

        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        tabLayout=root.findViewById(R.id.tab_layout);
        viewPager=root.findViewById(R.id.view_pager);

        mainAdapter=new MainAdapter(getChildFragmentManager());


        mainAdapter.AddFragment(new SuggestedFragment(exerciseToDisplay), getString(R.string.suggested_tab_title));
        mainAdapter.AddFragment(new PopularFragment(exerciseToDisplay), getString(R.string.popular_tab_title));

        viewPager.setAdapter(mainAdapter);

        tabLayout.setupWithViewPager(viewPager);

        gridView = root.findViewById(R.id.gridFilter);
        gridView.setOnItemClickListener(this);


        scheduleFilterAdapter = new ScheduleFilterAdapter(getContext());
        gridView.setAdapter(scheduleFilterAdapter);
    }

    /**
     * This method is called when one of the filtering buttons is clicked.
     * It recreates suggested and popular fragment basing on the filters applied.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView exImage = (TextView) view.findViewById(R.id.image_ex);

        if (clickedFilter.get(position)) { /* filtro già clickato, nascondo il check */
            clickedFilter.replace(position, false);
            exImage.setBackgroundResource(EXERCISE_DRAWABLE[position]);
            exerciseToDisplay.remove(EXERCISE_LABELS[position]);
        } else { /* filtro non ancora clickato, mostro il check */
            clickedFilter.replace(position, true);
            exImage.setBackgroundResource(R.drawable.check_green);
            exerciseToDisplay.add(EXERCISE_LABELS[position]);
        }

        Log.i(TAG, "User clicked on filter: "+EXERCISE_LABELS[position]+" recreating fragments.");

        /* recreates fragment filtering exercises */
        mainAdapter=new MainAdapter(getChildFragmentManager());
        mainAdapter.AddFragment(new SuggestedFragment(exerciseToDisplay), "Suggested");
        mainAdapter.AddFragment(new PopularFragment(exerciseToDisplay), "Popular");
        viewPager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(viewPager);
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