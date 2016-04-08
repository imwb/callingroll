package com.example.wb.calling.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.wb.calling.R;
import com.example.wb.calling.fragment.RecordByCourseFrag;
import com.example.wb.calling.fragment.RecordByTimeFrag;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyPagerAdapter adapter;
    private List<Fragment> frags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initMenu(3);
        initToolbarAndDrawer("记录");
        initTab();
        initPager();
    }

    private void initPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        frags = new ArrayList<>();
        frags.add(new RecordByCourseFrag());
        frags.add(new RecordByTimeFrag());
        adapter = new MyPagerAdapter(getSupportFragmentManager(),frags);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);

    }

    private void initTab() {
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mTabLayout.addTab(mTabLayout.newTab().setText("课程"));
        mTabLayout.addTab(mTabLayout.newTab().setText("时间"));

    }

}

class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> frags;

    private String[] titles = new String[]{"课程","时间"};
    public MyPagerAdapter(FragmentManager fm,List<Fragment> frags) {
        super(fm);
        this.frags = frags;
    }


    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return frags.get(position);
    }

    @Override
    public int getCount() {
        return frags.size();
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
