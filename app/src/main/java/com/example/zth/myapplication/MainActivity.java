package com.example.zth.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.annotation.OnClick;
import com.example.annotation.TestActivity;
import com.example.annotation.TestInt;
import com.example.annotation.TestString;

@TestActivity(R.layout.activity_main)
public class MainActivity extends Activity{



    @TestInt(R.id.btn)
    Button btn;

    @OnClick(R.id.btn)
    public void onClick(){

        tv.setText(str);
    }


    @TestInt(R.id.tv)
    TextView tv;

    @TestString("979451341")
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TestMainActivity.setDefualt(this);


    }


}