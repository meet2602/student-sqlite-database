package com.materialsouk.sqlitedatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    ArrayList<Student> studentList;
    StudentAdapter studentAdapter;
    AddStudentDialog addStudentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentList = new ArrayList<>();
        myDB = new DatabaseHelper(this);
        FloatingActionButton addUserFloatBtn = findViewById(R.id.floatingActionButton);


        //// select Dialog start
        Dialog selectDialog = new Dialog(this);
        selectDialog.setContentView(R.layout.select_layout);
        selectDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        selectDialog.setCancelable(true);

        Button editStudentBtn = selectDialog.findViewById(R.id.edit_student);
        Button deleteStudentBtn = selectDialog.findViewById(R.id.delete_student);
        //// select Dialog close

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        Cursor cursor = myDB.readAllStudentData();
        while (cursor.moveToNext()) {
            studentList.add(new Student(
                    cursor.getString((int) cursor.getColumnIndex("studentId")),
                    cursor.getString((int) cursor.getColumnIndex("studentName")),
                    cursor.getString((int) cursor.getColumnIndex("studentAddress")),
                    cursor.getString((int) cursor.getColumnIndex("studentPhoneNo")),
                    cursor.getString((int) cursor.getColumnIndex("studentEmail")),
                    cursor.getString((int) cursor.getColumnIndex("studentDOB")),
                    cursor.getString((int) cursor.getColumnIndex("password"))
            ));
        }

        studentAdapter = new StudentAdapter(studentList, (position, key) -> {
            selectDialog.show();
            editStudentBtn.setOnClickListener(v -> {
                selectDialog.dismiss();
                addStudentDialog = new AddStudentDialog(true,position, studentList, studentAdapter, myDB);
                addStudentDialog.show(getSupportFragmentManager(), "AddStudentDialog");
            });
            deleteStudentBtn.setOnClickListener(v -> {
                selectDialog.dismiss();
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Delete Student")
                        .setMessage("Are you sure, you want to delete this Student?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            studentList.remove(position);
                            myDB.deleteStudent(key);
                            studentAdapter.notifyItemRemoved(position);
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            });
        });
        recyclerView.setAdapter(studentAdapter);
        addUserFloatBtn.setOnClickListener(v -> {
            addStudentDialog = new AddStudentDialog(false,-1, studentList, studentAdapter, myDB);
            addStudentDialog.show(getSupportFragmentManager(), "AddStudentDialog");
        });

    }
}