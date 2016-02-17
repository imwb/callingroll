package com.example.wb.calling.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by wb on 16/2/1.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toast(String info){
        Toast.makeText(this,info,Toast.LENGTH_LONG).show();
    }

}
