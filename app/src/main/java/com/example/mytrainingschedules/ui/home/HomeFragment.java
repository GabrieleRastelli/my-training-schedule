package com.example.mytrainingschedules.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    GridView gridView;
    CustomAdapter adapter;
    FloatingActionButton fab;

    /*
    * getActivity() --> MainActivity
    * root          --> HomeFragment
    */

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        String[] data = {"uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove"};
        adapter = new CustomAdapter(this.getContext(), data);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                fab = getActivity().findViewById(R.id.fab);
                fab.setVisibility(View.VISIBLE);
                gridView = root.findViewById(R.id.grid);
                gridView.setAdapter(adapter);
            }
        });
        return root;
    }
}