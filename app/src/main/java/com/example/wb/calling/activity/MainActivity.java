package com.example.wb.calling.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.CallCourseAdapter;
import com.example.wb.calling.adapter.ChooseCourseAdapter;
import com.example.wb.calling.adapter.ItemTouchHelperCallback;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.entry.CourseOrder;
import com.example.wb.calling.manager.CourseManager;
import com.shamanland.fab.ShowHideOnScroll;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wb on 16/2/1.
 */
public class MainActivity extends BaseActivity {


    private RecyclerView callRecycler;
    private ArrayList<Course> courseList;
    private CallCourseAdapter adapter;
    private LinearLayoutManager mlayoutManager;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbarAndDrawer("点名");
        initCall();
        initMenu(1);
        initFab();
    }

    private void initFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab_add_call);
        callRecycler.setOnTouchListener(new ShowHideOnScroll(fab));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCourse();
            }
        });
    }

    private void selectCourse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_call, null);
        ListView lv = (ListView) view.findViewById(R.id.lv_courses);
        final ArrayList<Course> courses = CourseManager.getInstance(getApplicationContext()).getCourseBySelected();
        final ChooseCourseAdapter chooseadapter = new ChooseCourseAdapter(this, courses);
        chooseadapter.initSelected();
        lv.setAdapter(chooseadapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseCourseAdapter.Holder holder = (ChooseCourseAdapter.Holder) view.getTag();
                Log.d("on click", "---------");
                holder.cb.toggle();
                chooseadapter.getIsSelected().put(position, holder.cb.isChecked());
            }
        });
        builder.setTitle("选则课程");
        builder.setView(view);
        builder.setNegativeButton("选择", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<Integer, Boolean> map = chooseadapter.getIsSelected();
                for (int i = 0; i < map.size(); i++) {
                    if (map.get(i) == true) {
                        Course c = courses.get(i);
                        courseList.add(c);
                        //同步order
                        adapter.setOrderdata(courseList);
                        adapter.notifyDataSetChanged();
                        adapter.refreshSort();
                        c.setCall(true);
                        CourseManager.getInstance(getApplicationContext()).updateCourseSqlite(c);
                    }
                }
            }
        });
        builder.show();
    }


    private void initCall() {
        callRecycler = (RecyclerView) findViewById(R.id.recycler_call);
        mlayoutManager = new LinearLayoutManager(this);
        mlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        callRecycler.setLayoutManager(mlayoutManager);
        callRecycler.setHasFixedSize(true);
        callRecycler.setItemAnimator(new DefaultItemAnimator());

        courseList = new ArrayList<>();
        ArrayList<CourseOrder> orders = CourseManager.getInstance(getApplicationContext()).getOrder();
        Log.d("orders", orders.toString());
        for (int i = 0; i < orders.size(); i++) {
            Course course = CourseManager.getInstance(getApplicationContext()).getCourseByID(orders.get(i).getCourseId());
            courseList.add(course);
        }
//        Course c1= new Course();
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
//        courseList.add(c12);

        adapter = new CallCourseAdapter(courseList,this);

        callRecycler.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(callRecycler);
    }


}