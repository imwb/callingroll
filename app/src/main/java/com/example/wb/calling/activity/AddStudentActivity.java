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
    private ArrayList<Student> students;
    private LinearLayout stumenuLayout;
    private Button importBtn;
    private Button addBtn;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
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
    }

    private void initLv() {
        stuLv = (ListView) findViewById(R.id.list_student);
        stumenuLayout = (LinearLayout) findViewById(R.id.layout_student_menu);

        students = new ArrayList<>();
        final Student s1 = new Student();
        s1.setName("吴比");
        s1.setNumber("2012201223");
        Student s2 = new Student();
        s2.setName("吴比");
        s2.setNumber("2012201223");
        Student s3 = new Student();
        s3.setName("吴比");
        s3.setNumber("2012201223");
        Student s4 = new Student();
        s4.setName("吴比");
        s4.setNumber("2012201223");
        Student s5 = new Student();
        s5.setName("吴比");
        s5.setNumber("2012201223");
        Student s6 = new Student();
        s6.setName("吴比");
        s6.setNumber("2012201223");
        Student s7 = new Student();
        s7.setName("吴比");
        s7.setNumber("2012201223");
        Student s8 = new Student();
        s8.setName("吴比");
        s8.setNumber("2012201223");
        Student s9 = new Student();
        s9.setName("吴比");
        s9.setNumber("2012201223");
        Student s10 = new Student();
        s10.setName("吴比");
        s10.setNumber("2012201223");
        Student s11 = new Student();
        s11.setName("吴比");
        s11.setNumber("2012201223");
        Student s12 = new Student();
        s12.setName("吴比");
        s12.setNumber("2012201223");
        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);
        students.add(s5);
        students.add(s6);
        students.add(s7);
        students.add(s8);
        students.add(s9);
        students.add(s10);
        students.add(s11);
        students.add(s12);

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
                            Log.d("0", number);
                            break;
                        case 1:
                            number = cell.getStringCellValue();
                            Log.d("1", number);
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
}
