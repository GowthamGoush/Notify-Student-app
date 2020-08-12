package com.example.studentnotify;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class FullscreenActivity extends AppCompatActivity {

    Animation top,bottom;
    TextView textView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        textView = findViewById(R.id.IntroText);
        imageView = findViewById(R.id.IntroImage);

        top.setDuration(1500);
        bottom.setDuration(1500);

        imageView.setAnimation(top);
        textView.setAnimation(bottom);

        final Intent intent = new Intent(this,MainActivity.class);

        CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(intent);
            }
        }.start();

    }



}