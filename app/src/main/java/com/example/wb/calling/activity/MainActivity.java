package com.example.wb.calling.activity;

import android.content.DialogInterface;
import android.content.Intent;
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

        adapter = new CallCourseAdapter(courseList,this);
        callRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new CallCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this,CallActivity.class);
                intent.putExtra("cou_id",courseList.get(position).getId());
                intent.putExtra("cou_name",courseList.get(position).getCourse_name());
                startActivity(intent);
            }
        });
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(callRecycler);




    }


}