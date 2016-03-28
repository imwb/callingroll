package com.example.wb.calling.manager;

import android.content.Context;
import android.util.Log;

import com.example.wb.calling.entry.Record;
import com.example.wb.calling.entry.RecordItem;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
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
    public static RecordManager getInstance(Context context){
        if(manager!= null){
            return manager;
        }else {
            manager = new RecordManager(context);
            return manager;
        }
    }

    public boolean saveRecords(final Record record , final ArrayList<RecordItem> records){
        IsSuccess = true;
        final List<BmobObject> objects = new ArrayList<BmobObject>();
        record.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                final String recordId = record.getObjectId();

                for(int i = 0; i < records.size();i++){
                    RecordItem item = records.get(i);
                    item.setRecordId(recordId);
                    objects.add(item);
                }
                new BmobObject().insertBatch(context, objects, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"save records success");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        IsSuccess = false;
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                IsSuccess = false;
            }
        });

        return IsSuccess;
    }
}
