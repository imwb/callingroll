package com.example.wb.calling.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;

/**
 * Created by wb on 16/3/9.
 */
public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
            final MessageEvent event =(MessageEvent)intent.getSerializableExtra("event");
            //开发者可以在这里发应用通知
            BmobIMMessage msg = event.getMessage();
            Log.d("msg",msg.toString());
        }
    }
}
