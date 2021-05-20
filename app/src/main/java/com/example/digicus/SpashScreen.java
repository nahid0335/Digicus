package com.example.digicus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class SpashScreen extends AppCompatActivity {
    TextView logoLeft,logoRight;
    Animation leftoright,righttoleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        logoLeft   = findViewById(R.id.textView_splashScreen_logoLeft);
        logoRight   = findViewById(R.id.textView_splashScreen_logoRight);

        leftoright  = AnimationUtils.loadAnimation(this,R.anim.lefttoright);
        righttoleft = AnimationUtils.loadAnimation(this,R.anim.righttoleft);

        logoRight.setAnimation(righttoleft);
        logoLeft.setAnimation(leftoright);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent a = new Intent(SpashScreen.this, MainActivity.class);
                startActivity(a);
                finish();
            }
        },1500);
    }
}