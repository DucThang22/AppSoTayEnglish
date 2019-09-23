package com.example.appsotayenglish.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appsotayenglish.R;


public class MainActivity extends AppCompatActivity {

   ImageView imgEnglish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgEnglish = findViewById(R.id.imv_laguageEnglish);
        imgEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownLoadDataActivity.class);
                startActivity(intent);
            }
        });
    }


}
