package in.cbslgroup.ezeeoffice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.cbslgroup.ezeeoffice.R;


public class SplashScreenActivity extends AppCompatActivity {

    Handler handler;
    TextView tvCopyRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Date date = Calendar.getInstance().getTime();
        Log.e("date", date.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String formattedDate = simpleDateFormat.format(date);
        Log.e("formatteddate", formattedDate);

        tvCopyRight = findViewById(R.id.tvCopyRight);
        tvCopyRight.setText("Copyright © " + formattedDate + " CBSL Group. All rights reserved");


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

        ImageView logo =findViewById(R.id.iv_splash_logo);
        AnimationSet mAnimationSet = new AnimationSet(false);
      //  Animation fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
       // Animation fadeOutAnimation = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
        Animation bounceAnimation = AnimationUtils.loadAnimation(this,R.anim.rotate);
        mAnimationSet.addAnimation(bounceAnimation);
        logo.startAnimation(mAnimationSet);


    }
}
