package com.materialsouk.sqlitedatabase;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private final List<Student> studentList;
    private final SelectListener selectListener;

    public StudentAdapter(List<Student> studentList, SelectListener selectListener) {
        this.studentList = studentList;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_user, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.studentId.setText("Id: " + student.getId());
        holder.studentName.setText("Name: " + student.getStudentName());
        holder.studentAddress.setText("Address: " + student.getStudentAddress());
        holder.studentPhoneNo.setText("Mobile No.: " + student.getStudentPhoneNo());
        holder.studentEmail.setText("Email id: " + student.getStudentEmail());
        holder.studentDOB.setText("DOB: " + student.getStudentDOB());

        holder.itemView.setOnLongClickListener(v -> {
            selectListener.onSelect(position, student.getId());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView studentId;
        private final TextView studentName;
        private final TextView studentAddress;
        private final TextView studentPhoneNo;
        private final TextView studentEmail;
        private final TextView studentDOB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentId = itemView.findViewById(R.id.studentId);
            studentName = itemView.findViewById(R.id.studentName);
            studentAddress = itemView.findViewById(R.id.studentAddress);
            studentPhoneNo = itemView.findViewById(R.id.studentPhoneNo);
            studentEmail = itemView.findViewById(R.id.studentEmail);
            studentDOB = itemView.findViewById(R.id.studentDOB);
        }
    }

    public interface SelectListener {
        void onSelect(int position, String id);
    }
}
