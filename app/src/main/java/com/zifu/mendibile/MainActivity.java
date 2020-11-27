package com.zifu.mendibile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    public static BBDDHelper helper;
    int prueba = 4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE"));
        shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE");


        context = getApplicationContext();
        helper = new BBDDHelper(MainActivity.context);

    Button btn1 = (Button) findViewById(R.id.btn1);
    btn1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),Principal.class);
            startActivity(i);
        }
    });





    }
}