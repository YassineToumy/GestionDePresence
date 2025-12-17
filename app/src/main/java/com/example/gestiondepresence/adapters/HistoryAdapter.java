package com.example.gestiondepresence.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.R;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Attendance;
import com.example.gestiondepresence.models.Student;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<Attendance> attendanceList;
    private DatabaseHelper db;

    public HistoryAdapter(Context context, List<Attendance> attendanceList, DatabaseHelper db) {
        this.context = context;
        this.attendanceList = attendanceList;
        this.db = db;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);

        // Get student info
        List<Student> students = db.getAllStudents();
        String studentName = "Inconnu";
        for (Student student : students) {
            if (student.getId() == attendance.getStudentId()) {
                studentName = student.getName() + " " + student.getSurname();
                break;
            }
        }

        holder.txtHistoryStudent.setText(studentName);
        holder.txtHistoryDate.setText(attendance.getDate());
        holder.txtHistorySession.setText(attendance.getSessionName());

        // Set status with color
        holder.txtHistoryStatus.setText(attendance.getStatus());
        if ("present".equals(attendance.getStatus())) {
            holder.txtHistoryStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else if ("absent".equals(attendance.getStatus())) {
            holder.txtHistoryStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else if ("retard".equals(attendance.getStatus())) {
            holder.txtHistoryStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtHistoryStudent, txtHistoryDate, txtHistoryStatus, txtHistorySession;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHistoryStudent = itemView.findViewById(R.id.txtHistoryStudent);
            txtHistoryDate = itemView.findViewById(R.id.txtHistoryDate);
            txtHistoryStatus = itemView.findViewById(R.id.txtHistoryStatus);
            txtHistorySession = itemView.findViewById(R.id.txtHistorySession);
        }
    }
}
