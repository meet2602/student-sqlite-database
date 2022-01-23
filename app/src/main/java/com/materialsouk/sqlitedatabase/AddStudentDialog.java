package com.materialsouk.sqlitedatabase;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

public class AddStudentDialog extends DialogFragment {


    private Toolbar toolbar;
    ArrayList<Student> studentList;
    StudentAdapter studentAdapter;
    DatabaseHelper myDB;
    boolean updateSate;
    int updatePos;

    public AddStudentDialog(boolean updateSate, int updatePos, ArrayList<Student> studentList, StudentAdapter studentAdapter, DatabaseHelper myDB) {
        this.updateSate = updateSate;
        this.updatePos = updatePos;
        this.studentList = studentList;
        this.studentAdapter = studentAdapter;
        this.myDB = myDB;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    private EditText edStudentName,
            edStudentAddress,
            edStudentPhoneNo,
            edStudentEmail, edDateBirth,
            edPassword, edConPassword;
    private TextInputLayout txtPasswordL, txtConPasswordL;
    private boolean isValidPassword = false;
    private boolean isValidConPassword = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.add_student_layout, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        edStudentName = view.findViewById(R.id.edStudentName);
        edStudentAddress = view.findViewById(R.id.edStudentAddress);
        edStudentPhoneNo = view.findViewById(R.id.edStudentPhoneNo);
        edStudentEmail = view.findViewById(R.id.edStudentEmail);
        edPassword = view.findViewById(R.id.edPassword);
        edDateBirth = view.findViewById(R.id.edDateBirth);
        edConPassword = view.findViewById(R.id.edConPassword);
        txtPasswordL = view.findViewById(R.id.txtPasswordL);
        txtConPasswordL = view.findViewById(R.id.txtConPasswordL);
        if (!updateSate) {
            edPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    validateEditTextPassword(edPassword, txtPasswordL, "password");
                }
            });
            edConPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    validateEditTextPassword(edConPassword, txtConPasswordL, "conPassword");
                }
            });
        }
        MaterialDatePicker materialDatePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Birthday")
                        .setSelection(getDate())
                        .build();
        materialDatePicker.addOnPositiveButtonClickListener(selection -> edDateBirth.setText(materialDatePicker.getHeaderText()));
        edDateBirth.setOnClickListener(v -> materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER"));

        if (updateSate) {
            txtPasswordL.setVisibility(View.GONE);
            txtConPasswordL.setVisibility(View.GONE);
            edStudentName.setText(studentList.get(updatePos).getStudentName());
            edStudentAddress.setText(studentList.get(updatePos).getStudentAddress());
            edStudentPhoneNo.setText(studentList.get(updatePos).getStudentPhoneNo());
            edStudentEmail.setText(studentList.get(updatePos).getStudentEmail());
            edDateBirth.setText(studentList.get(updatePos).getStudentDOB());
        }
        return view;
    }

    private Long getDate() {
        if (updateSate) {
            try {
                return new SimpleDateFormat("dd MMM yyyy").parse(studentList.get(updatePos).getStudentDOB()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        if (updateSate) {
            toolbar.setTitle("Update Student");
        } else {
            toolbar.setTitle("Add Student");
        }
        toolbar.inflateMenu(R.menu.save_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.saveMenu) {
                uploadData();
                return true;
            } else {
                return false;
            }
        });
    }

    private void uploadData() {
        if (edStudentName.getText().toString().trim().isEmpty()) {
            edStudentName.setError("Required");
        } else if (edStudentAddress.getText().toString().trim().isEmpty()) {
            edStudentAddress.setError("Required");
        } else if (edStudentPhoneNo.getText().toString().trim().isEmpty()) {
            edStudentPhoneNo.setError("Required");
        } else if (edStudentEmail.getText().toString().trim().isEmpty()) {
            edStudentEmail.setError("Required");
        } else if (edStudentPhoneNo.getText().toString().length() <= 9 ||
                edStudentPhoneNo.getText().toString().length() >= 11) {
            edStudentPhoneNo.setError("Phone no is 10 Digits.");
        } else if (!validEmail(edStudentEmail.getText().toString().trim())) {
            edStudentEmail.setError("valid e-mail");
        } else if (edDateBirth.getText().toString().trim().isEmpty()) {
            edDateBirth.setError("Required");
        } else {
            if (updateSate) {
                myDB.updateStudent(edStudentName.getText().toString().trim(),
                        edStudentAddress.getText().toString().trim(),
                        edStudentPhoneNo.getText().toString().trim(),
                        edStudentEmail.getText().toString().trim(),
                        edDateBirth.getText().toString().trim(),
                        studentList.get(updatePos).getId());
                studentList.set(updatePos, new Student(
                        studentList.get(updatePos).getId(),
                        edStudentName.getText().toString().trim(),
                        edStudentAddress.getText().toString().trim(),
                        edStudentPhoneNo.getText().toString().trim(),
                        edStudentEmail.getText().toString().trim(),
                        edDateBirth.getText().toString().trim(),
                        studentList.get(updatePos).getPassword()
                ));
                studentAdapter.notifyItemChanged(updatePos);
                AllNullEditText();
                dismiss();
            } else {
                validateEditTextPassword(edPassword, txtPasswordL, "password");
                if (isValidPassword) {
                    validateEditTextPassword(edConPassword, txtConPasswordL, "conPassword");
                    if (isValidConPassword) {
                        if (!edPassword.getText().toString().trim().equals(edConPassword.getText().toString().trim())) {
                            txtConPasswordL.setError("Password don't match!");
                        } else {
                            txtConPasswordL.setError(null);
                            String id = UUID.randomUUID().toString();
                            myDB.addStudent(edStudentName.getText().toString().trim(),
                                    edPassword.getText().toString().trim(),
                                    edStudentAddress.getText().toString().trim(),
                                    edStudentPhoneNo.getText().toString().trim(),
                                    edStudentEmail.getText().toString().trim(),
                                    edDateBirth.getText().toString().trim(),
                                    id);
                            studentList.add(new Student(
                                    id,
                                    edStudentName.getText().toString().trim(),
                                    edStudentAddress.getText().toString().trim(),
                                    edStudentPhoneNo.getText().toString().trim(),
                                    edStudentEmail.getText().toString().trim(),
                                    edDateBirth.getText().toString().trim(),
                                    edPassword.getText().toString().trim()
                            ));
                            studentAdapter.notifyItemInserted(studentList.size());
                            AllNullEditText();
                            dismiss();
                        }
                    }
                }
            }
        }
    }

    private boolean validEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void AllNullEditText() {
        edStudentName.setText(null);
        edStudentAddress.setText(null);
        edStudentPhoneNo.setText(null);
        edStudentEmail.setText(null);
        edDateBirth.setText(null);
        edPassword.setText(null);
        edConPassword.setText(null);
    }

    private void validateEditTextPassword(
            EditText editText,
            TextInputLayout textInputLayout,
            String state
    ) {
        boolean truthState = false;

        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError("Required");
        } else if (editText.getText().toString().trim().length() < 8 || editText.getText().toString().trim().length() > 10) {
            textInputLayout.setError("Password must be 8 to 10 character!");
        } else {
            textInputLayout.setError(null);
            truthState = true;
        }

        if (truthState) {
            if (state.equals("password")) {
                isValidPassword = true;
            } else if (state.equals("conPassword")) {
                isValidConPassword = true;
            }
        } else {
            if (state.equals("password")) {
                isValidPassword = false;
            } else if (state.equals("conPassword")) {
                isValidConPassword = false;
            }
        }
    }
}

