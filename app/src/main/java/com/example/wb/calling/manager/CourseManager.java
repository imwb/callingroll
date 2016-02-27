package com.example.wb.calling.manager;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.wb.calling.entry.Course;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wb on 16/1/29.
 */
public class CourseManager {

    private Context context;
    private static CourseManager manager;
    private CourseManager(Context context){
        this.context = context;
    }
    private  DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("calling.db")
            .setDbVersion(1)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                }
            });

    /**
     * 单例
     * @param context
     * @return
     */
    public static CourseManager getInstance(Context context){
        if(manager != null){
            manager.context = context;
            return manager;
        }else {
            return new CourseManager(context);
        }
    }

    /**
     * 添加课程
     * @param course
     */
    public void addCourse(final Course course , final Activity activity){

        course.save(context, new SaveListener() {
            boolean IsSuccess = true;
            @Override
            public void onSuccess() {
                //保存本地数据库
                course.setId(course.getObjectId());
                saveCourse(course);
                toast("添加成功");
                activity.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                switch (i){
                    case 105:toast("此ID已创建");
                        break;
                }
                toast(i+s);
            }
        });
    }

    /**
     * 保存到sqlite数据库
     * @param course
     */
    private void saveCourse(Course course){
        DbManager db = x.getDb(daoConfig);
        try {
            db.save(course);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库中所有课程
     * @param
     */
    private ArrayList<Course> getAllCouresFromSqlite(){
        DbManager db = x.getDb(daoConfig);
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses.addAll(db.selector(Course.class).findAll());
        } catch (DbException e) {
            e.printStackTrace();
        }
        return  courses;
    }
    private void toast(String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

}
