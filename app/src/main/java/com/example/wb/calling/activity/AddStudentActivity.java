package com.example.wb.calling.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.wb.calling.R;
import com.example.wb.calling.adapter.StudentAdapter;
import com.example.wb.calling.entry.Student;
import com.example.wb.calling.utils.RegexUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.shamanland.fab.ShowHideOnScroll;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class AddStudentActivity extends BaseActivity {

    private ListView stuLv;
    private ArrayList<Student> students = new ArrayList<>();
    private LinearLayout stumenuLayout;
    private Button importBtn;
    private Button addBtn;
    private Button saveBtn;
    private StudentAdapter adapter;
    static final int RESULTROLL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        String jstus = getIntent().getStringExtra("jstudents");
        if(jstus != null && !jstus.isEmpty() ){
            students = json2array(jstus);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar("学生名单");
        initLv();
        initView();
    }

    private void initView() {
        importBtn = (Button) findViewById(R.id.btn_import);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddStudentActivity.this, FileExplorerActivity.class), 1);
            }
        });

        addBtn = (Button) findViewById(R.id.btn_add_student);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });

        saveBtn = (Button) findViewById(R.id.btn_save_roll);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("students",array2json(students));
                setResult(RESULTROLL,intent);
                finish();
            }
        });
    }

    private void initLv() {
        stuLv = (ListView) findViewById(R.id.list_student);
        stumenuLayout = (LinearLayout) findViewById(R.id.layout_student_menu);
        adapter = new StudentAdapter(students, this);
        stuLv.setAdapter(adapter);
        stuLv.setOnTouchListener(new ShowHideOnScroll(stumenuLayout));
        stuLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alertStudent(students.get(position).getName(),
                        students.get(position).getNumber(),
                        position);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("students",array2json(students));
        setResult(RESULTROLL,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FileExplorerActivity.RESULTDIR:
                if (data != null) {
                    String path = data.getStringExtra("path");
                    ArrayList<Student> studentsfromExe = parsingExcel(path);
                    if (studentsfromExe.isEmpty()) {
                        toast("Excel格式错误");
                    } else {
                        students.addAll(studentsfromExe);
                        refresh();
                    }
                }

        }
    }

    /**
     * 解析excel 文件 获取学生姓名学号
     *
     * @param path
     * @return
     */
    private ArrayList<Student> parsingExcel(String path) {
        ArrayList<Student> students = new ArrayList<>();
        InputStream file = null;
        try {
            file = new FileInputStream(path);
            POIFSFileSystem ts = new POIFSFileSystem(file);

            int dotIndex = path.lastIndexOf(".");
            String fileType = dotIndex < 0 ? "" : path.substring(dotIndex).toLowerCase();
            //WorkBook ->excel;sheet->工作薄 ；row->一行；cell->一个单元格
            if (fileType.equals(".xls")) {
                Workbook wb = new HSSFWorkbook(ts);
                Sheet sh = wb.getSheetAt(0);
                Row row = null;
                ArrayList<String> titles = new ArrayList<>();
                for (int i = 0; i < sh.getRow(0).getLastCellNum(); i++) {
                    String title = sh.getRow(0).getCell(i).toString().replaceAll("\\s*", "");
                    Log.d("col", title);
                    titles.add(title);
                }

                int numberCol = titles.indexOf("学号");
                int nameCol = titles.indexOf("姓名");
                Log.d("学号", numberCol + "");
                Log.d("姓名", nameCol + "");
                if (nameCol == -1 || nameCol == -1) {
                    return students;
                }
                for (int i = 1; sh.getRow(i) != null; i++) {
                    row = sh.getRow(i);

                    String name = row.getCell(nameCol).toString();
                    String number = null;

                    Cell cell = row.getCell(numberCol);
                    cell.setCellType(1);
                    int type = cell.getCellType();
                    switch (type) {
                        case 0:
                            double value = new Double(cell.getNumericCellValue());
                            number = String.valueOf(value);
                            break;
                        case 1:
                            number = cell.getStringCellValue();
                            break;
                        case 2:
                            break;
                        case 3:
                            System.out.print(" , ");
                            break;
                        default:
                            System.out.print("未知的单元类型" + type + " , ");
                    }

                    Student student = new Student();
                    student.setName(name);
                    student.setNumber(number);

                    students.add(student);
                }
            } else {
                // 解析 .xlsx
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return students;
        }
    }

    /**
     * dialog 对话框 添加学生
     */
    private void addStudent() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_stu_dialog, null);
        final MaterialEditText nameEdt = (MaterialEditText) view.findViewById(R.id.edt_stu_name);
        final MaterialEditText numberEdt = (MaterialEditText) view.findViewById(R.id.edt_stu_number);
        nameEdt.addValidator(new METValidator("姓名格式不正确！") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]+$");
                if (pattern.matcher(text).matches()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        numberEdt.addValidator(new METValidator("学号格式不正确！") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                if (RegexUtil.isIDCode(text.toString())) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        builder.setTitle("添加学生");
        builder.setIcon(R.drawable.ic_touch_app_black_24dp);
        builder.setView(view);
        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nameEdt.validate() && numberEdt.validate()) {
                    String name = nameEdt.getText().toString();
                    String number = numberEdt.getText().toString();
                    Student student = new Student();
                    student.setNumber(number);
                    student.setName(name);
                    students.add(student);
                    refresh();
                } else {

                }

            }
        });
        builder.create();
        builder.show();
    }
    /**
     * dialog 对话框 添加学生
     */
    private void alertStudent(String name, String number, final int postion) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_stu_dialog, null);
        final MaterialEditText nameEdt = (MaterialEditText) view.findViewById(R.id.edt_stu_name);
        final MaterialEditText numberEdt = (MaterialEditText) view.findViewById(R.id.edt_stu_number);
        nameEdt.setText(name);
        numberEdt.setText(number);
        nameEdt.addValidator(new METValidator("姓名格式不正确！") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]+$");
                if (pattern.matcher(text).matches()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        numberEdt.addValidator(new METValidator("学号格式不正确！") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                if (RegexUtil.isIDCode(text.toString())) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        builder.setTitle("修改");
        builder.setIcon(R.drawable.ic_touch_app_black_24dp);
        builder.setView(view);
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nameEdt.validate() && numberEdt.validate()) {
                    String name = nameEdt.getText().toString();
                    String number = numberEdt.getText().toString();
                    Student student = students.get(postion);
                    student.setNumber(number);
                    student.setName(name);
                    refresh();
                } else {

                }

            }
        });
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                students.remove(postion);
                refresh();
            }
        });
        builder.create();
        builder.show();
    }

    private void refresh() {
        Collections.sort(students);
        adapter.notifyDataSetChanged();
    }

    /**
     * 将 student 集合 转换为 json格式
     * @param students
     * @return jsonarray
     */
    private String array2json(ArrayList<Student> students){
        Gson gson = new Gson();
        return gson.toJson(students);
    }
    /**
     * 将 student 集合 转换为 json格式
     * @param str
     * @return 集合
     */
    private ArrayList<Student> json2array(String str){
        Gson gson = new Gson();
        return gson.fromJson(str,new TypeToken<ArrayList<Student>>(){}.getType());
    }

}
