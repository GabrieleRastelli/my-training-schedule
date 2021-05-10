package com.example.mytrainingschedules.activities.workout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class RunningWorkoutActivity extends AppCompatActivity {

    private Schedule schedule;
    private TextView initTimer, exerciseName, reps, weight, sets, rest;
    private FloatingActionButton nextExercise, addSet;

    TextView middleRest,time,text,motivationalQuote;
    private Button btnSave;
    private boolean timerRunning=false;

    private Animation circleExplosion,moveUp,fadeIn;
    private Animation scaleDown, scaleUp, scaleUpLong, scaleDownLong;
    private Context context;

    private String quotes[]={
            "\"The last three or four reps is what makes the muscle grow. This area of pain divides a champion from someone who is not a champion.\"\nArnold Schwarzenegger",
            "\"Success usually comes to those who are too busy to be looking for it.\"\nHenry David Thoreau",
            "\"All progress takes place outside the comfort zone.\"\nMichael John Bobak",
            "\"If you think lifting is dangerous, try being weak. Being weak is dangerous.\"\nBret Contreras",
            "\"The only place where success comes before work is in the dictionary.\"\nVidal Sassoon",
            "\"The clock is ticking. Are you becoming the person you want to be?\"\nGreg Plitt",
            "\"Whether you think you can, or you think you can’t, you’re right.\"\nHenry Ford",
            "\"The successful warrior is the average man, with laser-like focus.\"\nBruce Lee",
            "\"You must expect great things of yourself before you can do them.\"\nMichael Jordan",
            "\"Action is the foundational key to all success.\"\nPablo Picasso"
    };

    long trainingStart,trainingEnd,elapsed,
    restStart,restEnd,restElapsed=0, numberOfTimeRested;

    CountDownTimer timerReps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_layout);

        /* no notifications during training */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context=this;
        trainingStart=System.currentTimeMillis();


        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");

        initGUI();

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                initTimer.setText("Workout starting in...\n" + millisUntilFinished / 1000);
            }
            public void onFinish() {
                initTimer.setVisibility(View.GONE);
                setElementsVisible();
                startExercise(0, 0);
            }
        }.start();
    }

    private void initGUI(){
        circleExplosion = AnimationUtils.loadAnimation(this, R.anim.circle_explosion);

        moveUp = new TranslateAnimation(0, 0, 500, 0);
        moveUp.setDuration(2000);
        moveUp.setFillAfter(true);

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(4000);

        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleUpLong = AnimationUtils.loadAnimation(this, R.anim.scale_up_long);
        scaleDownLong = AnimationUtils.loadAnimation(this, R.anim.scale_down_long);

        btnSave= findViewById(R.id.btn_save);

        motivationalQuote=(TextView)findViewById(R.id.motivational_quote);
        text=(TextView)findViewById(R.id.finished_text);
        time=(TextView)findViewById(R.id.time);
        middleRest=(TextView)findViewById(R.id.rest_middle);
        exerciseName = findViewById(R.id.exerciseName);
        exerciseName.setVisibility(View.GONE);
        reps = findViewById(R.id.reps);
        reps.setVisibility(View.GONE);
        weight = findViewById(R.id.weight);
        weight.setVisibility(View.GONE);
        sets = findViewById(R.id.sets);
        sets.setVisibility(View.GONE);
        rest = findViewById(R.id.rest);
        rest.setVisibility(View.GONE);
        nextExercise = findViewById(R.id.done);
        nextExercise.setVisibility(View.GONE);
        addSet = findViewById(R.id.addSet);
        addSet.setVisibility(View.GONE);
        initTimer = findViewById(R.id.initTimer);
        initTimer.setVisibility(View.VISIBLE);

        btnSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    btnSave.startAnimation(scaleDown);
                }else if(event.getAction()==MotionEvent.ACTION_UP) {
                    btnSave.startAnimation(scaleUp);
                    Intent intent = new Intent(RunningWorkoutActivity.this, MainActivity.class);
                    intent.putExtra("USER_GUID",getIntent().getStringExtra("USER_GUID"));
                    startActivity(intent);
                }
                return true;
            }
        });
        circleExplosion.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                nextExercise.setVisibility(View.GONE);
                addSet.setVisibility(View.GONE);
                exerciseName.setVisibility(View.GONE);
                reps.setVisibility(View.GONE);
                weight.setVisibility(View.GONE);
                sets.setVisibility(View.GONE);
                rest.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.background_running);
                layout.setBackgroundResource(R.color.pale_green);

                AnimationSet set= new AnimationSet(false);
                set.addAnimation(fadeIn);
                set.addAnimation(moveUp);
                text.startAnimation(set);
                text.setVisibility(View.VISIBLE);


                elapsed-=5000; /* escludo i 5 secondi di wait */

                long hour=((elapsed/1000)/60)/60;
                long mins=((elapsed/1000)/60);
                long sec=(elapsed/1000);
                StringBuilder sb = new StringBuilder();
                sb.append("It took only ");
                if(hour!=0){
                    sb.append(hour);
                    sb.append(" Hours, ");
                }
                if (mins!=0 || hour!=0){
                    sb.append(mins);
                    sb.append(" Minutes and ");
                }
                sb.append(sec);
                sb.append(" Seconds!");
                time.setText(sb.toString());
                time.startAnimation(set);
                time.setVisibility(View.VISIBLE);



                if(numberOfTimeRested==0) {
                    numberOfTimeRested = 1;
                }
                middleRest.setText("Your avarage rest is "+restElapsed/numberOfTimeRested+" seconds.");
                middleRest.startAnimation(set);
                middleRest.setVisibility(View.VISIBLE);

                btnSave.startAnimation(set);
                btnSave.setVisibility(View.VISIBLE);

                Random r=new Random();
                int randomNumber=r.nextInt(quotes.length);
                motivationalQuote.setText(quotes[randomNumber]);
                motivationalQuote.startAnimation(set);
                motivationalQuote.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void setElementsVisible(){
        exerciseName.setVisibility(View.VISIBLE);
        reps.setVisibility(View.VISIBLE);
        weight.setVisibility(View.VISIBLE);
        sets.setVisibility(View.VISIBLE);
        nextExercise.setVisibility(View.VISIBLE);
        addSet.setVisibility(View.VISIBLE);
    }

    private void startExercise(int index, int doneSets){
        Exercise currentExercise = schedule.getExercises().get(index);
        rest.setVisibility(View.GONE);
        exerciseName.setText(currentExercise.getName());
        reps.setText(currentExercise.getReps() + " reps");
        if(currentExercise.isRequireEquipment()){
            weight.setText(currentExercise.getWeight() + " kg");
        }
        else{
            weight.setText("Bodyweight exercise");
        }
        sets.setVisibility(View.VISIBLE);
        sets.setText("Sets: " + (currentExercise.getSets() - doneSets));
        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!timerRunning){
                    restStart=System.currentTimeMillis();
                    timerRunning=true;
                    rest(index, doneSets);
                }
                else{
                    addSet.setImageResource(R.drawable.ic_baseline_timer_24);
                    timerRunning=false;
                    resetTimer(index,doneSets);
                }
            }
        });
        nextExercise.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                addSet.setEnabled(true);
                if((index + 1) == schedule.lenght()){
                    trainingEnd=System.currentTimeMillis();
                    elapsed=trainingEnd-trainingStart;
                    View circleView=findViewById(R.id.circle_view);
                    circleView.setVisibility(View.VISIBLE);
                    circleView.startAnimation(circleExplosion);

                }
                else{
                    startExercise(index + 1, 0);
                }
            }
        });
    }


    private void rest(int index, int doneSets){
        Exercise currentExercise = schedule.getExercises().get(index);
        doneSets++;
        if(doneSets == currentExercise.getSets()){
            // rest between sets
            sets.setVisibility(View.GONE);
            rest.setVisibility(View.VISIBLE);
            new CountDownTimer((currentExercise.getRest_between_exercises() + 1) * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    rest.setText("Exercise completed!\nRest: " + millisUntilFinished / 1000);
                    addSet.setEnabled(false);
                }
                public void onFinish() {
                    if((index + 1) != schedule.lenght()){
                        rest.setText("Next exercise: " + schedule.getExercises().get(index + 1).getName());
                        timerRunning=false;
                    }
                }
            }.start();
        }
        else{
            // rest between reps
            int doneSetsTmp = doneSets;
            sets.setVisibility(View.GONE);
            rest.setVisibility(View.VISIBLE);
            timerReps = new CountDownTimer((currentExercise.getRest_between_sets() + 1) * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    addSet.setImageResource(R.drawable.tick);
                    rest.setText("Rest: " + millisUntilFinished / 1000);
                }
                public void onFinish() { /* oltre a questo c'è il metodo resetTimer */
                    timerRunning=false;
                    addSet.setImageResource(R.drawable.ic_baseline_timer_24);
                    restEnd=System.currentTimeMillis();
                    restElapsed+=(restEnd-restStart)/1000;
                    if((restEnd-restStart)!=0){
                        numberOfTimeRested++;
                    }
                    sets.setText("Sets: " + (currentExercise.getSets() - doneSetsTmp));
                    startExercise(index, doneSetsTmp);
                }
            }.start();
        }
    }

    private void resetTimer(int index, int doneSets){

        timerReps.cancel();

        restEnd=System.currentTimeMillis();
        restElapsed+=(restEnd-restStart)/1000;

        if((restEnd-restStart)!=0){
            numberOfTimeRested++;
        }

        Exercise currentExercise = schedule.getExercises().get(index);
        doneSets++;

        sets.setText("Sets: " + (currentExercise.getSets() - doneSets));
        startExercise(index, doneSets);
    }

}