package com.example.wb.calling.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.wb.calling.entry.Record;
import com.example.wb.calling.entry.RecordAdapterEntry;
import com.example.wb.calling.entry.RecordItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wb on 16/3/25.
 */
public class RecordManager {
    public static final String TAG = "record manager";
    private Context context;
    protected boolean IsSuccess;

    private RecordManager(Context context) {
        this.context = context;
    }

    private static RecordManager manager;

    public static RecordManager getInstance(Context context) {
        if (manager != null) {
            manager.context = context;
            return manager;
        } else {
            return new RecordManager(context);

        }
    }

    public boolean saveRecords(final Record record, final ArrayList<RecordItem> records) {
        IsSuccess = true;
//        final List<BmobObject> objects = new ArrayList<BmobObject>();
        record.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                final String recordId = record.getObjectId();
                Log.d(TAG, records.toString());
                for (int i = 0; i < records.size(); i++) {
                    RecordItem item = records.get(i);
                    item.setRecordId(recordId);
                    final int finalI = i;
                    item.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "save" + finalI + " success");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d(TAG, i + s);
                            IsSuccess = false;
                        }
                    });
//                    objects.add(item);
                }
//                new BmobObject().insertBatch(context, objects, new SaveListener() {
//                    @Override
//                    public void onSuccess() {
//                        Log.d(TAG,"save records success");
//                    }
//
//                    @Override
//                    public void onFailure(int i, String s) {
//
//                        Log.d(TAG,i+s);
//                        IsSuccess = false;
//                    }
//                });
            }

            @Override
            public void onFailure(int i, String s) {
                IsSuccess = false;
            }
        });

        return IsSuccess;
    }

    public void loadRecordGroupByCourseName( final List<RecordAdapterEntry> entries) {
        BmobQuery<Record> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherName", UserManager.getInstance((context.getApplicationContext())).getuserInfo().getUsername());
        query.groupby(new String[]{"courseName", "courseClass"});
        query.setHasGroupCount(true);
        query.findStatistics(context, Record.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                JSONArray ary = (JSONArray) o;
                if (ary != null) {
                    int length = ary.length();
                    Log.d(TAG, Thread.currentThread().getName());
                    try {
                        for (int i = 0; i < length; i++) {
                            JSONObject object = ary.getJSONObject(i);
                            Log.d(TAG, i + object.toString());
                            RecordAdapterEntry entry = new RecordAdapterEntry();
                            entry.setCount(object.getInt("_count"));
                            entry.setCourseName(object.getString("courseName"));
                            entry.setCourseClass(object.getString("courseClass"));
                            entries.add(entry);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d(TAG, i + s);
            }
        });

    }

    /**
     * 根据 课程明查询点名记录
     *
     * @param courseName
     * @return
     */
    public List<Record> loadRecordsByCourseName(String courseName) {
        final List<Record> records = new ArrayList<>();
        final BmobQuery<Record> bmobQuery = new BmobQuery<Record>();
        bmobQuery.addWhereEqualTo("courseName", courseName);
        bmobQuery.addWhereEqualTo("teacherName", UserManager.getInstance((context.getApplicationContext())).getuserInfo().getUsername());
        bmobQuery.setLimit(50);
        bmobQuery.order("createdAt");
        bmobQuery.findObjects(context, new FindListener<Record>() {

            @Override
            public void onSuccess(List<Record> object) {
                // TODO Auto-generated method stub
                Log.d(TAG, "查询成功：共" + object.size() + "条数据。");
                for (Record record : object) {
                    records.add(record);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "i" + s);
            }
        });
        return records;
    }

    public void loadRecordsByteacherName(final List<Record> records) {
        BmobQuery<Record> query = new BmobQuery<>();
        query.setLimit(1000);
        query.order("-createdAt");
        query.addWhereEqualTo("teacherName", UserManager.getInstance(context.getApplicationContext()).getuserInfo().getUsername());
        query.findObjects(context, new FindListener<Record>() {
            @Override
            public void onSuccess(List<Record> list) {
                records.addAll(list);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, i + s);
            }
        });
    }

    public void deleteByRecordID(final String recordID){


        BmobQuery<RecordItem> query = new BmobQuery<RecordItem>();
        query.addWhereEqualTo("recordId", recordID);
        query.setLimit(500);
        query.findObjects(context, new FindListener<RecordItem>() {
            @Override
            public void onSuccess(List<RecordItem> list) {
                for(RecordItem item:list){
                    item.delete(context, new DeleteListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG,"delete item success " );
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d(TAG,"delete item failure " + i + s );
                            Toast.makeText(context,"删除失败" + s,Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                Record record = new Record();
                record.setObjectId(recordID);
                record.delete(context, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"delete record success " );
                        Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d(TAG,"delete item failure " + i + s );
                        Toast.makeText(context,"删除失败" + s,Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Log.d(" query ",i+s);
                Toast.makeText(context,"删除失败" + s,Toast.LENGTH_SHORT).show();
            }

        });


    }
}
