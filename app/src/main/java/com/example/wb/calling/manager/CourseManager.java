package com.example.wb.calling.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.wb.calling.activity.AddCourseActivity;
import com.example.wb.calling.activity.MyApp;
import com.example.wb.calling.entry.Course;
import com.example.wb.calling.entry.CourseOrder;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by wb on 16/1/29.
 */
public class CourseManager {

    private Context context;
    private static CourseManager manager;

    private CourseManager(Context context) {
        this.context = context;
    }

    private DbManager.DaoConfig daoConfig = MyApp.daoConfig;

    /**
     * 单例
     *
     * @param context
     * @return
     */
    public static CourseManager getInstance(Context context) {
        if (manager != null) {
            manager.context = context;
            return manager;
        } else {
            manager = new CourseManager(context);
            return manager;
        }
    }

    public void toast(String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 添加课程
     *
     * @param course
     */
    public void addCourse(final Course course, final Activity activity) {

        course.save(context, new SaveListener() {
            boolean IsSuccess = true;

            @Override
            public void onSuccess() {
                //保存本地数据库
                course.setId(course.getObjectId());
                saveCourse(course);
                toast("添加成功");
                activity.setResult(AddCourseActivity.RESULT_ADD_COURSE);
                activity.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                switch (i) {
                    case 105:
                        toast("此ID已创建");
                        break;
                }
                toast(i + s);
            }
        });
    }

    /**
     * 保存到sqlite数据库
     *
     * @param course
     */
    public void saveCourse(Course course) {
        DbManager db = x.getDb(daoConfig);
        course.setCall(false);
        try {
            db.saveOrUpdate(course);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存到sqlite数据库
     *
     * @param courses
     */
    public void saveCourses(List<Course> courses) {
        DbManager db = x.getDb(daoConfig);
        if (courses != null) {
            for (Course course : courses) {
                course.setCall(false);
                course.setId(course.getObjectId());
            }
            try {
                db.save(courses);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取数据库中所有课程
     *
     * @param
     */
    public ArrayList<Course> getAllCouresFromSqlite() {
        DbManager db = x.getDb(daoConfig);
        ArrayList<Course> courses = new ArrayList<>();
        try {
            List<Course> list = db.selector(Course.class).findAll();
            if (list != null)
                courses.addAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * 根据 id 查询 course
     *
     * @param id
     * @return
     */
    public Course getCourseByID(String id) {
        DbManager db = x.getDb(daoConfig);
        Course course = new Course();
        try {
            course = db.findById(Course.class, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return course;
    }

    /**
     * 根据 id 查询 course
     *
     * @param id
     * @return
     */
    public Course loadCourseByID(String id) {
        final Course course = new Course();
        BmobQuery<Course> query = new BmobQuery<Course>();
        query.getObject(context,id, new GetListener<Course>() {

            @Override
            public void onSuccess(Course object) {
                // TODO Auto-generated method stub
                course.setId(object.getObjectId());
                course.setUserID(object.getUserID());
                course.setTeacher_name(object.getTeacher_name());
                course.setCourse_name(object.getCourse_name());
                saveCourse(course);

            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Log.i("life", "onFailure = "+code+",msg = "+msg);
                toast(msg);
            }
        });
        return course;
    }
    /**
     * 修改 course
     *
     * @param course
     */
    public void updateCourse(final Course course) {
        course.setObjectId(course.getId());
        course.update(context, course.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                DbManager db = x.getDb(daoConfig);
                try {
                    db.saveOrUpdate(course);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                toast("修改成功！");
            }

            @Override
            public void onFailure(int i, String s) {
                switch (i) {
                    case 304:
                        toast(s);
                        break;
                }
                toast(i + ":" + s);

            }
        });
    }

    /**
     * 更新sqlite course
     */
    public void updateCourseSqlite(Course course) {
        DbManager db = x.getDb(daoConfig);
        try {
            db.saveOrUpdate(course);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除课程
     *
     * @param course
     */
    public void delete(final Course course) {
        course.setObjectId(course.getId());
        course.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {
                DbManager db = x.getDb(daoConfig);
                try {
                    db.delete(course);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                toast("删除成功");
            }

            @Override
            public void onFailure(int i, String s) {
                toast(i + ": " + s);
            }
        });
    }
    /**
     * 删除课程
     *
     * @param course
     */
    public void deleteInSqlite(final Course course) {

                DbManager db = x.getDb(daoConfig);
                try {
                    db.delete(course);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                toast("删除成功");
    }


    /**
     * 从bmob 加载所有课程
     *
     * @param userID
     * @return
     */
    public void loadCourseByUserID(final String userID) {
        final BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("userID", userID);
        query.count(context, Course.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                BmobQuery<Course> query1 = new BmobQuery<Course>();
                query1.addWhereEqualTo("userID",userID);
                query1.order("createdAt");
                query1.setLimit(i);
                query1.findObjects(context, new FindListener<Course>() {
                    @Override
                    public void onSuccess(List<Course> list) {
                        saveCourses(list);
                    }

                    @Override
                    public void onError(int i, String s) {
                        toast(i + ": " + s);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                toast(i + ": " + s);
            }
        });
    }

    /**
     * 查询 没加入点名列表的课程
     *
     * @return
     */
    public ArrayList<Course> getCourseBySelected() {
        DbManager db = x.getDb(daoConfig);
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses = (ArrayList<Course>) db.selector(Course.class).where("isCall", "=", false).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        Log.d("call course", courses.toString());
        return courses;
    }

    /**
     * 查询选择课程的排序
     *
     * @return
     */
    public ArrayList<CourseOrder> getOrder() {
        DbManager manager = x.getDb(daoConfig);
        ArrayList<CourseOrder> orders = new ArrayList<>();
        try {
            List<CourseOrder> list = manager.selector(CourseOrder.class).findAll();
            if (list != null) {
                orders.addAll(list);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * 更新 排序
     *
     * @param orders
     */
    public void refreshOrder(List<CourseOrder> orders) {
        DbManager manager = x.getDb(daoConfig);
        try {
            manager.delete(CourseOrder.class);
            manager.save(orders);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
