package com.example.wb.calling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wb.calling.R;
import com.example.wb.calling.view.FileListView;

import java.io.File;

public class FileExplorerActivity extends AppCompatActivity {

    private FileListView fileExplorer;
    private Toolbar mToolbar;
    private String dir;
    public static final int REQUSETDIR = 1;
    public static final int RESULTDIR = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_file_explorer);
        Intent intent = getIntent();
        if(intent.getStringExtra("dir")!=null){
            dir = intent.getStringExtra("dir");
            initToolbar(dir.substring(20,dir.length()));
        }else {
            dir = Environment.getExternalStorageDirectory().getPath();
            initToolbar("导入名单");
        }
        initEx();
    }

    private void initToolbar(String title){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
    private void initEx() {

        fileExplorer = (FileListView) findViewById(R.id.ex);

        // 打开路径
        fileExplorer.setCurrentDir(dir);

        // 根路径（能到达最深的路径，以此避免用户进入root路径）
        fileExplorer.setRootDir(Environment.getExternalStorageDirectory().getPath());

        //覆盖屏蔽原有长按事件
        fileExplorer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        fileExplorer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = ((TextView)view.findViewById(R.id.list_text)).getText().toString();
                try {
                    File e = new File(dir + "/" + fileName);
                    if(e.isFile()) {
                        int dotIndex = e.getName().lastIndexOf(".");
                        String type = dotIndex < 0?"":e.getName().substring(dotIndex).toLowerCase();
//                        if (type.equals(".xls") || type.equals(".xlsx")){
                        if (type.equals(".xls")){
                            Intent intent = new Intent();
                            intent.putExtra("path",e.getPath());
                            setResult(RESULTDIR,intent);
                            finish();
                        }else {
                            Toast.makeText(FileExplorerActivity.this,"请选择Excel文件(.xls)",Toast.LENGTH_SHORT).show();
                        }

                    } else if(e.isDirectory()) {
                        Intent intent = new Intent(FileExplorerActivity.this,FileExplorerActivity.class);
                        intent.putExtra("dir",e.getPath());
                        startActivityForResult(intent,REQUSETDIR);
                    }
                } catch (Exception var10) {
                    var10.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULTDIR :
                String path = data.getStringExtra("path");
                setResult(RESULTDIR,data);
                finish();
                break;
        }
    }
}
