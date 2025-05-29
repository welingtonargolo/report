package com.example.report.ui.history;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.report.data.models.Problem;
import com.example.report.databinding.ItemReportBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private List<Problem> reports = new ArrayList<>();
    private final OnReportClickListener listener;

    public interface OnReportClickListener {
        void onReportClick(Problem problem);
    }

    public ReportAdapter(OnReportClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReportBinding binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new ReportViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.bind(reports.get(position));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public void setReports(List<Problem> reports) {
        this.reports = reports;
        notifyDataSetChanged();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        private final ItemReportBinding binding;

        ReportViewHolder(ItemReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("ResourceType")
        void bind(Problem problem) {

            binding.categoryText.setText(problem.getCategoryName());
            binding.descriptionText.setText(problem.getDescription());
            

            binding.statusChip.setText(problem.getStatus());


            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date date = inputFormat.parse(problem.getDatetime());
                if (date != null) {
                    binding.dateText.setText(outputFormat.format(date));
                }
            } catch (ParseException e) {
                binding.dateText.setText(problem.getDatetime());
            }


            if (problem.getPhoto() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                    problem.getPhoto(),
                    0,
                    problem.getPhoto().length
                );
                binding.reportImage.setImageBitmap(bitmap);
            } else {
                binding.reportImage.setImageResource(android.R.drawable.ic_menu_camera);
            }


            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReportClick(problem);
                }
            });


            int chipColor;
            switch (problem.getStatus().toLowerCase()) {
                case "resolvido":
                    chipColor = android.graphics.Color.parseColor("#4CAF50"); // Green
                    break;
                case "em análise":
                    chipColor = android.graphics.Color.parseColor("#2196F3"); // Blue
                    break;
                case "em andamento":
                    chipColor = android.graphics.Color.parseColor("#FF9800"); // Orange
                    break;
                default:
                    chipColor = android.graphics.Color.parseColor("#757575"); // Gray
                    break;
            }
            binding.statusChip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(chipColor));
        }
    }
}
