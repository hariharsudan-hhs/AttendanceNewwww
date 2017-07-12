package com.example.hhs.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shyam on 2/3/17.
 */

public class StartingActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE = 111;
    public static String img="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startingactivity);


        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);



        TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img="male";
                Intent i = new Intent(StartingActivity.this,StartingActivity2.class);
                startActivity(i);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img="female";
                Intent i = new Intent(StartingActivity.this,StartingActivity2.class);
                startActivity(i);
            }
        });

        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-R.ttf");
        textView.setTypeface(face);
        textView2.setTypeface(face);
    }

}
