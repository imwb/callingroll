package com.example.wb.calling.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.CourseAdapter;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.manager.CourseManager;
import com.shamanland.fab.ShowHideOnScroll;
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
    private FloatingActionButton fab;
    private CourseManager mcourseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycourse);
        mcourseManager = CourseManager.getInstance(getApplicationContext());
        courseList = mcourseManager.getAllCouresFromSqlite();
        initToolbarAndDrawer("我的课程");
        initMenu(2);
        initFab();
        initList();
    }

    private void initFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab_add_course);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MyCourseActivity.this,AddCourseActivity.class),REQUEST_ADD_COURSE);
            }
        });
    }

    private void initList() {
        courseLv = (SlideAndDragListView) findViewById(R.id.sdlv_course);

//.add(c12);        Course c1= new Course();
//        c1.setCourse_id("0920313");
//        c1.setCourse_name("数据库设计");
//        c1.setCourse_class("20122011 20122012 20122013");
//        c1.setCourse_room_time("周一 3-4 21b301 周三 5-6 21b405");
//
//        Course c2= new Course();
//        c2.setCourse_id("0913138");
//        c2.setCourse_name("形式与政策");
//        c2.setCourse_class("20系 19系");
//        c2.setCourse_room_time("周五 3-4 ");
//
//        Course c3= new Course();
//        c3.setCourse_id("0912004");
//        c3.setCourse_name("软件工程");
//        c3.setCourse_class("20142011 20142012");
//        c3.setCourse_room_time("周二 1-2 周五 6-7 ");
//
//        Course c4= new Course();
//        c4.setCourse_id("0920313");
//        c4.setCourse_name("数据库设计");
//        c4.setCourse_class("20122011 20122012 20122013");
//        c4.setCourse_room_time("周一 3-4 21b301 周三 5-6 21b405");
//
//        Course c5= new Course();
//        c5.setCourse_id("0913138");
//        c5.setCourse_name("形式与政策");
//        c5.setCourse_class("20系 19系");
//        c5.setCourse_room_time("周五 3-4 ");
//
//        Course c6= new Course();
//        c6.setCourse_id("0912004");
//        c6.setCourse_name("软件工程");
//        c6.setCourse_class("20142011 20142012");
//        c6.setCourse_room_time("周二 1-2 周五 6-7 ");
//
//
//        Course c7= new Course();
//        c7.setCourse_id("0920313");
//        c7.setCourse_name("数据库设计");
//        c7.setCourse_class("20122011 20122012 20122013");
//        c7.setCourse_room_time("周一 3-4 21b301 周三 5-6 21b405");
//
//        Course c8= new Course();
//        c8.setCourse_id("0913138");
//        c8.setCourse_name("形式与政策");
//        c8.setCourse_class("20系 19系");
//        c8.setCourse_room_time("周五 3-4 ");
//
//        Course c9= new Course();
//        c9.setCourse_id("0912004");
//        c9.setCourse_name("软件工程");
//        c9.setCourse_class("20142011 20142012");
//        c9.setCourse_room_time("周二 1-2 周五 6-7 ");
//
//        Course c10= new Course();
//        c10.setCourse_id("0920313");
//        c10.setCourse_name("数据库设计");
//        c10.setCourse_class("20122011 20122012 20122013");
//        c10.setCourse_room_time("周一 3-4 21b301 周三 5-6 21b405");
//
//        Course c11= new Course();
//        c11.setCourse_id("0913138");
//        c11.setCourse_name("形式与政策");
//        c11.setCourse_class("20系 19系");
//        c11.setCourse_room_time("周五 3-4 ");
//
//        Course c12= new Course();
//        c12.setCourse_id("0912004");
//        c12.setCourse_name("软件工程");
//        c12.setCourse_class("20142011 20142012");
//        c12.setCourse_room_time("周二 1-2 周五 6-7 ");
//
//        courseList.add(c1);
//        courseList.add(c2);
//        courseList.add(c3);
//        courseList.add(c4);
//        courseList.add(c5);
//        courseList.add(c6);
//        courseList.add(c7);
//        courseList.add(c8);
//        courseList.add(c9);
//        courseList.add(c10);
//        courseList.add(c11);
//        courseList

        Log.d("course",courseList.toString());
        adapter = new CourseAdapter(this,courseList);

        final Menu menu = new Menu(new ColorDrawable(Color.WHITE), false, 0);//第2个参数表示滑动item是否能滑的过量(true表示过量，就像Gif中显示的那样；false表示不过量，就像QQ中的那样)

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

        courseLv.setOnTouchListener(new ShowHideOnScroll(fab));
        courseLv.setOnListItemClickListener(new SlideAndDragListView.OnListItemClickListener() {
            @Override
            public void onListItemClick(View v, int position) {
                Intent intent = new Intent(MyCourseActivity.this,CourseActivity.class);
                intent.putExtra("courseId",courseList.get(position).getId());
                startActivity(intent);
            }
        });

        courseLv.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                switch (direction){
                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition){
                            case 0://删除
                                deleteCourse(courseList.get(itemPosition),itemPosition);
                                return  Menu.ITEM_SCROLL_BACK;
                            case 1://修改
                                Intent intent = new Intent(MyCourseActivity.this,AlertCourseActivity.class);
                                intent.putExtra("courseId",courseList.get(itemPosition).getId());
                                startActivityForResult(intent,REQUEST_UPDATE_COURSE);
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                    default:
                        return Menu.ITEM_NOTHING;
                }
                return Menu.ITEM_SCROLL_BACK;
            }
        });
    }

    /**
     * 删除课程
     * @param course
     */
    private void deleteCourse(final Course course, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除课程")
                .setMessage("确定删除课程：'"+course.getCourse_name()+"'?")
                .setIcon(R.drawable.ic_clear_black_24dp)
                .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CourseManager.getInstance(getApplicationContext()).delete(course);
                        courseList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_UPDATE_COURSE:
                refresh();
                break;
            case RESULT_ADD_COURSE:
                refresh();
                break;
        }
    }

    private void refresh() {
        courseList = mcourseManager.getAllCouresFromSqlite();
        adapter.setData(courseList);
        adapter.notifyDataSetChanged();
    }
}