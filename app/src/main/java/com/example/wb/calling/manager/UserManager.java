package com.example.wb.calling.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.wb.calling.entry.User;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wb on 16/1/29.
 */
public class UserManager {

    private Context context;
    private static UserManager manager;
    private UserManager(Context context){
        this.context = context;
    }

    public static UserManager getInstance(Context context){
        if(manager != null){
            manager.context = context;
            return manager;
        }else {
            return new UserManager(context);
        }
    }

    public  void registerUser(final User user){
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
//                Intent intent = new Intent(context, LoginActivity.class);
//                intent.putExtra("username",user.getUsername());
                Toast.makeText(context,"注册成功，请及时通过邮件验证",Toast.LENGTH_LONG)
                        .show();

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context,"注册失败 : "+ i + "  " +s,Toast.LENGTH_LONG)
                        .show();
            }
        });
    }


    /**
     * 获取 当前用户 信息
     * @param
     */
    public User getuserInfo(){
        User user = new User();
        SharedPreferences sp = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        user.setUsername(sp.getString("username",""));
        user.setName(sp.getString("type",""));
        user.setEmail(sp.getString("email",""));
        user.setType(sp.getInt("type",0));
        return user;
    }
    protected  void toast(String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

}
