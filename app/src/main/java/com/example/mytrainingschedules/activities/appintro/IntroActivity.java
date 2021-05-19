package com.example.mytrainingschedules.activities.appintro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    ImageView buttonNext;
    TextView textView;
    IntroViewPagerAdapter introViewPagerAdapter;
    Animation scaleDown, scaleUp;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);

        List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Welcome!", "d1", R.drawable.exercise));
        mList.add(new ScreenItem("Second", "d2", R.drawable.weightlifting));
        mList.add(new ScreenItem("Let's start!", "d3", R.drawable.gym));

        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);

        screenPager.setAdapter(introViewPagerAdapter);

        textView = findViewById(R.id.exerciseTitle);
        textView.setText("");

        buttonNext = findViewById(R.id.buttonNext);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(screenPager.getCurrentItem() < 2 && !textView.getText().equals("GO TO APP")){
                    buttonNext.startAnimation(scaleDown);
                    buttonNext.startAnimation(scaleUp);
                    screenPager.setCurrentItem(screenPager.getCurrentItem() + 1);
                }
                if(screenPager.getCurrentItem() == 2){
                    buttonNext.startAnimation(scaleDown);
                    buttonNext.startAnimation(scaleUp);
                    textView.setText("GO TO APP");
                    buttonNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}
