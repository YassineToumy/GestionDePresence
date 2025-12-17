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
import com.example.gestiondepresence.SeanceDetailActivity;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Seance;

import java.util.List;

public class SeanceAdapter extends RecyclerView.Adapter<SeanceAdapter.SeanceViewHolder> {

    private Context context;
    private List<Seance> seanceList;
    private DatabaseHelper db;

    public SeanceAdapter(Context context, List<Seance> seanceList, DatabaseHelper db) {
        this.context = context;
        this.seanceList = seanceList;
        this.db = db;
    }

    @NonNull
    @Override
    public SeanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seance, parent, false);
        return new SeanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeanceViewHolder holder, int position) {
        Seance seance = seanceList.get(position);

        // Display seance info
        holder.tvSeanceNumber.setText("Séance " + seance.getSeanceNumber());
        holder.tvSubject.setText(seance.getSubjectName());
        holder.tvClassName.setText("Classe: " + seance.getClassName());
        holder.tvDateTime.setText(seance.getDate() + " à " + seance.getTime());

        // Status indicator
        if ("completed".equals(seance.getStatus())) {
            holder.tvStatus.setText("✅ Terminée");
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvStatus.setText("⏳ En cours");
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        }

        // View details button
        holder.btnViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, SeanceDetailActivity.class);
            intent.putExtra("seanceId", seance.getId());
            context.startActivity(intent);
        });

        // Delete button
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Supprimer la séance")
                    .setMessage("Voulez-vous vraiment supprimer cette séance et toutes les présences associées ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        if (db.deleteSeance(seance.getId())) {
                            seanceList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, seanceList.size());
                            Toast.makeText(context, "Séance supprimée", Toast.LENGTH_SHORT).show();
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
        return seanceList.size();
    }

    public static class SeanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeanceNumber, tvSubject, tvClassName, tvDateTime, tvStatus;
        Button btnViewDetails, btnDelete;

        public SeanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeanceNumber = itemView.findViewById(R.id.tvSeanceNumber);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}