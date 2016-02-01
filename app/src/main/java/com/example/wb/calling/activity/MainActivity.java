package com.example.wb.calling.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;

import com.example.wb.calling.R;

/**
 * Created by wb on 16/2/1.
 */
public class MainActivity extends BaseActivity{

    private DrawerLayout mDrawerLayout;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
    }
}
