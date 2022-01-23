package com.materialsouk.sqlitedatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmailId, edPassword;
    Button signInBtn;
    private TextInputLayout txtPasswordL;
    private DatabaseHelper myDB;
    TextView studentId;
    TextView studentName;
    TextView studentAddress;
    TextView studentPhoneNo;
    TextView studentEmail;
    TextView studentDOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);
        studentId = findViewById(R.id.studentId);
        studentName = findViewById(R.id.studentName);
        studentAddress = findViewById(R.id.studentAddress);
        studentPhoneNo = findViewById(R.id.studentPhoneNo);
        studentEmail = findViewById(R.id.studentEmail);
        studentDOB = findViewById(R.id.studentDOB);

        myDB = new DatabaseHelper(this);
        findViewById(R.id.displayAllStudentBtn).setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, MainActivity.class)));
        edEmailId = findViewById(R.id.edEmailId);
        edPassword = findViewById(R.id.edPassword);
        signInBtn = findViewById(R.id.signInBtn);
        txtPasswordL = findViewById(R.id.textFieawld1);
        signInBtn.setOnClickListener(v -> {
            Validation();
            hideKeyboard(v);
        });
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edPassword.getText().toString().trim().isEmpty()) {
                    txtPasswordL.setError("Required");
                } else if (edPassword.getText().toString().trim().length() < 8 || edPassword.getText().toString().trim().length() > 10) {
                    txtPasswordL.setError("Password must be 8 to 10 character!");
                } else {
                    txtPasswordL.setError(null);
                }
            }
        });

    }

    private void Validation() {
        if (edEmailId.getText().toString().trim().isEmpty()) {
            edEmailId.setError("Required");
        } else if (!validEmail(edEmailId.getText().toString().trim())) {
            edEmailId.setError("Enter valid e-mail!");
        } else if (edPassword.getText().toString().trim().isEmpty()) {
            txtPasswordL.setError("Required");
        }else if (edPassword.getText().toString().trim().length() < 8 || edPassword.getText().toString().trim().length() > 10) {
            txtPasswordL.setError("Password must be 8 to 10 character!");
        } else {
            txtPasswordL.setError(null);

            Cursor cursor = myDB.checkEmailPassword(edEmailId.getText().toString().trim(), edPassword.getText().toString().trim());
            if (cursor.getCount() <= 0) {
                findViewById(R.id.view_user_card).setVisibility(View.GONE);
                Toast.makeText(this, "Not Found!", Toast.LENGTH_SHORT).show();
            }
            while (cursor.moveToNext()) {
                findViewById(R.id.view_user_card).setVisibility(View.VISIBLE);
                studentId.setText(cursor.getString((int) cursor.getColumnIndex("studentId")));
                studentName.setText(cursor.getString((int) cursor.getColumnIndex("studentName")));
                studentAddress.setText(cursor.getString((int) cursor.getColumnIndex("studentAddress")));
                studentPhoneNo.setText(cursor.getString((int) cursor.getColumnIndex("studentPhoneNo")));
                studentEmail.setText(cursor.getString((int) cursor.getColumnIndex("studentEmail")));
                studentDOB.setText(cursor.getString((int) cursor.getColumnIndex("studentDOB")));
            }
        }
    }

    private boolean validEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }
}