package com.example.wb.calling.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.CallCourseAdapter;
import com.example.wb.calling.entry.Course;

import java.util.ArrayList;

/**
 * Created by wb on 16/2/1.
 */
public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView callRecycler;
    private ArrayList<Course> courseList;
    private CallCourseAdapter adapter;
    private LinearLayoutManager mlayoutManager;

    private LinearLayout menuCallLayout;
    private ImageButton menuCallImg;
    private TextView menuCallTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
        inittoolbar();
        initcall();
        initMenu();
    }

    private void initMenu() {
        menuCallImg = (ImageButton) findViewById(R.id.img_menu_call);
        menuCallTxt = (TextView) findViewById(R.id.txt_menu_call);
        menuCallImg.setBackgroundResource(R.drawable.ic_touch_app_black_24dp);
        menuCallTxt.setTextColor(getResources().getColor(R.color.black));

    }

    private void initcall() {
        callRecycler = (RecyclerView) findViewById(R.id.recycler_call);
        mlayoutManager = new LinearLayoutManager(this);
        mlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        callRecycler.setLayoutManager(mlayoutManager);
        callRecycler.setHasFixedSize(true);
        callRecycler.setItemAnimator(new DefaultItemAnimator());

        courseList = new ArrayList<>();
        Course c1= new Course();
        c1.setCourse_id("0920313");
        c1.setCourse_name("数据库设计");
        c1.setCourse_class("20122011 20122012 20122013");
        c1.setCourse_room_time("周一 3-4 21b301 周三 5-6 21b405");

        Course c2= new Course();
        c2.setCourse_id("0913138");
        c2.setCourse_name("形式与政策");
        c2.setCourse_class("20系 19系");
        c2.setCourse_room_time("周五 3-4 ");

        Course c3= new Course();
        c3.setCourse_id("0912004");
        c3.setCourse_name("软件工程");
        c3.setCourse_class("20142011 20142012");
        c3.setCourse_room_time("周二 1-2 周五 6-7 ");

        Course c4= new Course();
        c4.setCourse_id("0920313");
        c4.setCourse_name("数据库设计");
        c4.setCourse_class("20122011 20122012 20122013");
        c4.setCourse_room_time("周一 3-4 21b301 周三 5-6 21b405");

        Course c5= new Course();
        c5.setCourse_id("0913138");
        c5.setCourse_name("形式与政策");
        c5.setCourse_class("20系 19系");
        c5.setCourse_room_time("周五 3-4 ");

        Course c6= new Course();
        c6.setCourse_id("0912004");
        c6.setCourse_name("软件工程");
        c6.setCourse_class("20142011 20142012");
        c6.setCourse_room_time("周二 1-2 周五 6-7 ");


        Course c7= new Course();
        c7.setCourse_id("0920313");
        c7.setCourse_name("数据库设计");
        c7.setCourse_class("20122011 20122012 20122013");
        c7.setCourse_room_time("周一 3-4 21b301 周三 5-6 21b405");

        Course c8= new Course();
        c8.setCourse_id("0913138");
        c8.setCourse_name("形式与政策");
        c8.setCourse_class("20系 19系");
        c8.setCourse_room_time("周五 3-4 ");

        Course c9= new Course();
        c9.setCourse_id("0912004");
        c9.setCourse_name("软件工程");
        c9.setCourse_class("20142011 20142012");
        c9.setCourse_room_time("周二 1-2 周五 6-7 ");

        Course c10= new Course();
        c10.setCourse_id("0920313");
        c10.setCourse_name("数据库设计");
        c10.setCourse_class("20122011 20122012 20122013");
        c10.setCourse_room_time("周一 3-4 21b301 周三 5-6 21b405");

        Course c11= new Course();
        c11.setCourse_id("0913138");
        c11.setCourse_name("形式与政策");
        c11.setCourse_class("20系 19系");
        c11.setCourse_room_time("周五 3-4 ");

        Course c12= new Course();
        c12.setCourse_id("0912004");
        c12.setCourse_name("软件工程");
        c12.setCourse_class("20142011 20142012");
        c12.setCourse_room_time("周二 1-2 周五 6-7 ");

        courseList.add(c1);
        courseList.add(c2);
        courseList.add(c3);
        courseList.add(c4);
        courseList.add(c5);
        courseList.add(c6);
        courseList.add(c7);
        courseList.add(c8);
        courseList.add(c9);
        courseList.add(c10);
        courseList.add(c11);
        courseList.add(c12);

        adapter = new CallCourseAdapter(courseList);

        callRecycler.setAdapter(adapter);
    }

    private void inittoolbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("点名");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarColor));
        }

        drawerToggle = new ActionBarDrawerToggle(
                this,mDrawerLayout,mToolbar,
                R.string.abc_action_bar_home_description,R.string.abc_action_bar_home_description_format
        );

        drawerToggle.syncState();
        mDrawerLayout.setDrawerListener(drawerToggle);
    }
}