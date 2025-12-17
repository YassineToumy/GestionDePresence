package com.example.gestiondepresence.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.R;
import com.example.gestiondepresence.models.Attendance;
import com.example.gestiondepresence.models.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private Context context;
    private List<Student> studentList;
    private Map<Integer, String> attendanceMap;

    public AttendanceAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
        this.attendanceMap = new HashMap<>();

        // Initialize all students as present by default
        for (Student student : studentList) {
            attendanceMap.put(student.getId(), "present");
        }
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.txtStudentName.setText(student.getName() + " " + student.getSurname());

        // Set default selection
        String currentStatus = attendanceMap.get(student.getId());
        if ("present".equals(currentStatus)) {
            holder.radioPresent.setChecked(true);
        } else if ("absent".equals(currentStatus)) {
            holder.radioAbsent.setChecked(true);
        } else if ("retard".equals(currentStatus)) {
            holder.radioRetard.setChecked(true);
        }

        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioPresent) {
                attendanceMap.put(student.getId(), "present");
            } else if (checkedId == R.id.radioAbsent) {
                attendanceMap.put(student.getId(), "absent");
            } else if (checkedId == R.id.radioRetard) {
                attendanceMap.put(student.getId(), "retard");
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public List<Attendance> getAttendanceData() {
        List<Attendance> attendanceList = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : attendanceMap.entrySet()) {
            Attendance attendance = new Attendance(entry.getKey(), "", entry.getValue(), "");
            attendanceList.add(attendance);
        }
        return attendanceList;
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView txtStudentName;
        RadioGroup radioGroup;
        RadioButton radioPresent, radioAbsent, radioRetard;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStudentName = itemView.findViewById(R.id.txtStudentName);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            radioPresent = itemView.findViewById(R.id.radioPresent);
            radioAbsent = itemView.findViewById(R.id.radioAbsent);
            radioRetard = itemView.findViewById(R.id.radioRetard);
        }
    }
}
