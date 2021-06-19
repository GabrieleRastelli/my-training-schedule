package com.example.mytrainingschedules.activities.mainactivity.premium;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.mainactivity.settings.Schedule;
import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PremiumFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainAdapter mainAdapter;
    private GridView gridView;
    private ScheduleFilterAdapter scheduleFilterAdapter;
    private Map<Integer, Boolean> clickedFilter;
    private List<String> exerciseToDisplay;

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

        tabLayout=root.findViewById(R.id.tab_layout);
        viewPager=root.findViewById(R.id.view_pager);

        mainAdapter=new MainAdapter(getChildFragmentManager());


        mainAdapter.AddFragment(new SuggestedFragment(exerciseToDisplay), "Suggested");
        mainAdapter.AddFragment(new PopularFragment(exerciseToDisplay), "Popular");

        viewPager.setAdapter(mainAdapter);

        tabLayout.setupWithViewPager(viewPager);

        gridView = root.findViewById(R.id.gridFilter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView exImage = (TextView) view.findViewById(R.id.image_ex);

                switch(i) {
                    case 0:
                        if (clickedFilter.get(0)) { /* filtro già clickato, nascondo il check */
                            clickedFilter.replace(0, false);
                            exImage.setBackgroundResource(R.drawable.man_doing_chest);
                            exerciseToDisplay.remove("CHEST");
                        } else { /* filtro non ancora clickato, mostro il check */
                            clickedFilter.replace(0, true);
                            exImage.setBackgroundResource(R.drawable.check_green);
                            exerciseToDisplay.add("CHEST");
                        }

                        /* recreates fragment filtering exercises */
                        mainAdapter=new MainAdapter(getChildFragmentManager());
                        mainAdapter.AddFragment(new SuggestedFragment(exerciseToDisplay), "Suggested");
                        mainAdapter.AddFragment(new PopularFragment(exerciseToDisplay), "Popular");
                        viewPager.setAdapter(mainAdapter);
                        tabLayout.setupWithViewPager(viewPager);

                        break;
                    case 1:
                        if (clickedFilter.get(1)) { /* filtro già clickato, nascondo il check */
                            clickedFilter.replace(1, false);
                            exImage.setBackgroundResource(R.drawable.man_doing_biceps);
                            exerciseToDisplay.remove("ARMS");
                        } else { /* filtro non ancora clickato, mostro il check */
                            clickedFilter.replace(1, true);
                            exImage.setBackgroundResource(R.drawable.check_green);
                            exerciseToDisplay.add("ARMS");
                        }

                        /* recreates fragment filtering exercises */
                        mainAdapter=new MainAdapter(getChildFragmentManager());
                        mainAdapter.AddFragment(new SuggestedFragment(exerciseToDisplay), "Suggested");
                        mainAdapter.AddFragment(new PopularFragment(exerciseToDisplay), "Popular");
                        viewPager.setAdapter(mainAdapter);
                        tabLayout.setupWithViewPager(viewPager);

                        break;
                    case 2:
                        if (clickedFilter.get(2)) { /* filtro già clickato, nascondo il check */
                            clickedFilter.replace(2, false);
                            exImage.setBackgroundResource(R.drawable.man_doing_back);
                            exerciseToDisplay.remove("BACK");
                        } else { /* filtro non ancora clickato, mostro il check */
                            clickedFilter.replace(2, true);
                            exImage.setBackgroundResource(R.drawable.check_green);
                            exerciseToDisplay.add("BACK");
                        }

                        /* recreates fragment filtering exercises */
                        mainAdapter=new MainAdapter(getChildFragmentManager());
                        mainAdapter.AddFragment(new SuggestedFragment(exerciseToDisplay), "Suggested");
                        mainAdapter.AddFragment(new PopularFragment(exerciseToDisplay), "Popular");
                        viewPager.setAdapter(mainAdapter);
                        tabLayout.setupWithViewPager(viewPager);


                        break;
                    case 3:
                        if (clickedFilter.get(3)) { /* filtro già clickato, nascondo il check */
                            clickedFilter.replace(3, false);
                            exImage.setBackgroundResource(R.drawable.man_doing_squats);
                            exerciseToDisplay.remove("LEGS");
                        } else { /* filtro non ancora clickato, mostro il check */
                            clickedFilter.replace(3, true);
                            exImage.setBackgroundResource(R.drawable.check_green);
                            exerciseToDisplay.add("LEGS");
                        }

                        /* recreates fragment filtering exercises */
                        mainAdapter=new MainAdapter(getChildFragmentManager());
                        mainAdapter.AddFragment(new SuggestedFragment(exerciseToDisplay), "Suggested");
                        mainAdapter.AddFragment(new PopularFragment(exerciseToDisplay), "Popular");
                        viewPager.setAdapter(mainAdapter);
                        tabLayout.setupWithViewPager(viewPager);

                        break;
                    case 4:
                        if (clickedFilter.get(4)) { /* filtro già clickato, nascondo il check */
                            clickedFilter.replace(4, false);
                            exImage.setBackgroundResource(R.drawable.man_doing_shoulders);
                            exerciseToDisplay.remove("SHOULDERS");
                        } else { /* filtro non ancora clickato, mostro il check */
                            clickedFilter.replace(4, true);
                            exImage.setBackgroundResource(R.drawable.check_green);
                            exerciseToDisplay.add("SHOULDERS");
                        }

                        /* recreates fragment filtering exercises */
                        mainAdapter=new MainAdapter(getChildFragmentManager());
                        mainAdapter.AddFragment(new SuggestedFragment(exerciseToDisplay), "Suggested");
                        mainAdapter.AddFragment(new PopularFragment(exerciseToDisplay), "Popular");
                        viewPager.setAdapter(mainAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                        break;
                    case 5:
                        if (clickedFilter.get(5)) { /* filtro già clickato, nascondo il check */
                            clickedFilter.replace(5, false);
                            exImage.setBackgroundResource(R.drawable.woman_doing_core);
                            exerciseToDisplay.remove("FREE");
                        } else { /* filtro non ancora clickato, mostro il check */
                            clickedFilter.replace(5, true);
                            exImage.setBackgroundResource(R.drawable.check_green);
                            exerciseToDisplay.add("FREE");
                        }

                        /* recreates fragment filtering exercises */
                        mainAdapter=new MainAdapter(getChildFragmentManager());
                        mainAdapter.AddFragment(new SuggestedFragment(exerciseToDisplay), "Suggested");
                        mainAdapter.AddFragment(new PopularFragment(exerciseToDisplay), "Popular");
                        viewPager.setAdapter(mainAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                        break;
                }
            }
        });

        /* make gridView non scrollable */
        /* gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getActivity().getApplicationContext(), "Unable to add schedule", Toast.LENGTH_SHORT).show();
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });*/
        scheduleFilterAdapter = new ScheduleFilterAdapter(getContext());
        gridView.setAdapter(scheduleFilterAdapter);

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