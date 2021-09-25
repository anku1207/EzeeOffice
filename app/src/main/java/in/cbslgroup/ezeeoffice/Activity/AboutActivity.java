package in.cbslgroup.ezeeoffice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.cbslgroup.ezeeoffice.BuildConfig;
import in.cbslgroup.ezeeoffice.R;


public class AboutActivity extends AppCompatActivity {


    private TextView tvVersion;
    private TextView tvCopyRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        Date date = Calendar.getInstance().getTime();
        Log.e("date", date.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String formattedDate = simpleDateFormat.format(date);
        Log.e("formatteddate", formattedDate);

        tvVersion = findViewById(R.id.tv_about_version_name);
        tvVersion.setText(BuildConfig.VERSION_NAME);

        tvCopyRight = findViewById(R.id.tv_about_CopyRight);
        tvCopyRight.setText("Copyright © " + formattedDate + " CBSL Group. All rights reserved");


    }
}
