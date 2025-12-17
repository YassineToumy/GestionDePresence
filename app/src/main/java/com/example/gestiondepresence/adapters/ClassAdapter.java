package com.example.gestiondepresence.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.R;
import com.example.gestiondepresence.StudentListActivity;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.ClassRoom;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private Context context;
    private List<ClassRoom> classList;
    private DatabaseHelper db;

    public ClassAdapter(Context context, List<ClassRoom> classList, DatabaseHelper db) {
        this.context = context;
        this.classList = classList;
        this.db = db;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassRoom classRoom = classList.get(position);
        holder.txtClassName.setText(classRoom.getClassName());
        holder.txtClassDescription.setText(classRoom.getDescription());

        // View students button
        holder.btnViewStudents.setOnClickListener(v -> {
            Intent intent = new Intent(context, StudentListActivity.class);
            intent.putExtra("classId", classRoom.getId());
            intent.putExtra("className", classRoom.getClassName());
            context.startActivity(intent);
        });

        // Delete button
        holder.btnDeleteClass.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Supprimer la classe")
                    .setMessage("Voulez-vous vraiment supprimer cette classe ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        if (db.deleteClass(classRoom.getId())) {
                            classList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, classList.size());
                            Toast.makeText(context, "Classe supprimée", Toast.LENGTH_SHORT).show();
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
        return classList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView txtClassName, txtClassDescription;
        Button btnViewStudents, btnDeleteClass;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtClassDescription = itemView.findViewById(R.id.txtClassDescription);
            btnViewStudents = itemView.findViewById(R.id.btnViewStudents);
            btnDeleteClass = itemView.findViewById(R.id.btnDeleteClass);
        }
    }
}