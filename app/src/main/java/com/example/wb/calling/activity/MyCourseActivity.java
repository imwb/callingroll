package com.example.wb.calling.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.CourseAdapter;
import com.example.wb.calling.entry.Course;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;

/**
 * Created by wb on 16/2/1.
 */
public class MyCourseActivity extends BaseActivity {

    private SlideAndDragListView courseLv;
    private ArrayList<Course> courseList;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_mycourse);
        inittoolbar("我的课程");
        initMenu(2);
        initList();
    }

    private void initList() {
        courseLv = (SlideAndDragListView) findViewById(R.id.sdlv_course);
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

        adapter = new CourseAdapter(this,courseList);



        Menu menu = new Menu(new ColorDrawable(Color.WHITE), false, 0);//第2个参数表示滑动item是否能滑的过量(true表示过量，就像Gif中显示的那样；false表示不过量，就像QQ中的那样)

        menu.addItem(new MenuItem.Builder().setWidth(180)//单个菜单button的宽度
                .setBackground(new ColorDrawable(getResources().getColor(R.color.menuItemDelete)))//设置菜单的背景
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setText("删除")//set text string
                .setTextColor(Color.WHITE)//set text color
                .setTextSize(16)//set text size
                .build());

        menu.addItem(new MenuItem.Builder().setWidth(180)
                .setBackground(new ColorDrawable(getResources().getColor(R.color.menuTxtColor)))
                .setText("修改")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE)
                .setTextSize(16)
                .build());

        courseLv.setMenu(menu);
        courseLv.setAdapter(adapter);
    }


}