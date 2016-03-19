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