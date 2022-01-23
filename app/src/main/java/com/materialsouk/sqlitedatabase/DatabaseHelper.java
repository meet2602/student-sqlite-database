package com.materialsouk.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 1;
    //// NEW Student
    private static final String TABLE_NAME_NEW_STUDENT = "Student";
    private static final String COLUMN_studentId = "studentId";
    private static final String COLUMN_studentName = "studentName";
    private static final String COLUMN_studentAddress = "studentAddress";
    private static final String COLUMN_studentPhoneNo = "studentPhoneNo";
    private static final String COLUMN_studentEmail = "studentEmail";
    private static final String COLUMN_studentDOB = "studentDOB";
    private static final String COLUMN_password = "password";

    //// NEW Student
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String new_user = "CREATE TABLE " + TABLE_NAME_NEW_STUDENT +
                " (" + COLUMN_studentId + " TEXT, " +
                COLUMN_studentName + " TEXT, " +
                COLUMN_studentAddress + " TEXT, " +
                COLUMN_studentPhoneNo + " TEXT, " +
                COLUMN_studentEmail + " TEXT, " +
                COLUMN_studentDOB + " TEXT, " +
                COLUMN_password + " TEXT);";
        db.execSQL(new_user);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NEW_STUDENT);
        onCreate(db);
    }

    public void addStudent(String studentName,
                           String password,
                           String studentAddress,
                           String studentPhoneNo,
                           String studentEmail,
                           String studentDOB,
                           String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_studentName, studentName);
        cv.put(COLUMN_studentAddress, studentAddress);
        cv.put(COLUMN_studentPhoneNo, studentPhoneNo);
        cv.put(COLUMN_studentEmail, studentEmail);
        cv.put(COLUMN_studentDOB, studentDOB);
        cv.put(COLUMN_password, password);
        cv.put(COLUMN_studentId, id);
        long result = db.insert(TABLE_NAME_NEW_STUDENT, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllStudentData() {
        String query = "SELECT * FROM " + TABLE_NAME_NEW_STUDENT + " ORDER BY " + COLUMN_studentName + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateStudent(String studentName,
                              String password,
                              String studentAddress,
                              String studentPhoneNo,
                              String studentEmail,
                              String studentDOB,
                              String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_studentName, studentName);
        cv.put(COLUMN_studentAddress, studentAddress);
        cv.put(COLUMN_studentPhoneNo, studentPhoneNo);
        cv.put(COLUMN_studentEmail, studentEmail);
        cv.put(COLUMN_studentDOB, studentDOB);
        cv.put(COLUMN_password, password);
        cv.put(COLUMN_studentId, id);

        long result = db.update(TABLE_NAME_NEW_STUDENT, cv, "studentId=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_NEW_STUDENT, "studentId=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor checkEmailPassword(String email, String password) {
        String[] columns = {
                COLUMN_studentId, COLUMN_studentEmail, COLUMN_studentName, COLUMN_studentPhoneNo, COLUMN_studentAddress, COLUMN_studentDOB
        };
        String selection = COLUMN_studentEmail + " = ?" + " AND " + COLUMN_password + " = ?";
        String[] selectionArgs = {email, password};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(TABLE_NAME_NEW_STUDENT, //Table to query
                    columns,                    //columns to return
                    selection,                  //columns for the WHERE clause
                    selectionArgs,              //The values for the WHERE clause
                    null,                       //group the rows
                    null,                       //filter by row groups
                    null);
        }
        return cursor;

    }
}
