package com.example.gestiondepresence.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.R;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context context;
    private List<Student> studentList;
    private DatabaseHelper db;

    public StudentAdapter(Context context, List<Student> studentList, DatabaseHelper db) {
        this.context = context;
        this.studentList = studentList;
        this.db = db;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.txtStudentInfo.setText(student.getName() + " " + student.getSurname());

        // Get absence and retard count
        int absenceCount = db.getAbsenceCount(student.getId());
        int retardCount = db.getRetardCount(student.getId());

        holder.txtGroup.setText("Groupe: " + student.getGroup() +
                " | Absences: " + absenceCount +
                " | Retards: " + retardCount);

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Supprimer l'étudiant")
                    .setMessage("Voulez-vous vraiment supprimer cet étudiant ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        if (db.deleteStudent(student.getId())) {
                            studentList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, studentList.size());
                            Toast.makeText(context, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView txtStudentInfo, txtGroup;
        Button btnDelete;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStudentInfo = itemView.findViewById(R.id.txtStudentInfo);
            txtGroup = itemView.findViewById(R.id.txtGroup);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}