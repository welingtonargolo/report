package com.example.report.ui.history;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.report.data.DatabaseHelper;
import com.example.report.data.models.Problem;
import com.example.report.databinding.FragmentHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements ReportAdapter.OnReportClickListener {

    private FragmentHistoryBinding binding;
    private DatabaseHelper dbHelper;
    private ReportAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        adapter = new ReportAdapter(this);

        binding.reportsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.reportsRecyclerView.setAdapter(adapter);

        loadReports();
    }

    private void loadReports() {
        List<Problem> reports = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_CATEGORY_ID,
            DatabaseHelper.COLUMN_DESCRIPTION,
            DatabaseHelper.COLUMN_PHOTO,
            DatabaseHelper.COLUMN_LATITUDE,
            DatabaseHelper.COLUMN_LONGITUDE,
            DatabaseHelper.COLUMN_DATETIME,
            DatabaseHelper.COLUMN_STATUS
        };

        Cursor cursor = db.query(DatabaseHelper.TABLE_PROBLEMS, projection,
                null, null, null, null, DatabaseHelper.COLUMN_DATETIME + " DESC");

        while (cursor.moveToNext()) {
            Problem problem = new Problem();
            problem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
            problem.setCategoryId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)));
            problem.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)));
            problem.setPhoto(cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHOTO)));
            problem.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LATITUDE)));
            problem.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LONGITUDE)));
            problem.setDatetime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATETIME)));
            problem.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS)));

            // TODO: Load category name for display in adapter
            problem.setCategoryName(getCategoryName(problem.getCategoryId()));

            reports.add(problem);
        }
        cursor.close();

        if (reports.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
            binding.reportsRecyclerView.setVisibility(View.GONE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
            binding.reportsRecyclerView.setVisibility(View.VISIBLE);
            adapter.setReports(reports);
        }
    }

    private String getCategoryName(long categoryId) {
        String categoryName = "";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DatabaseHelper.COLUMN_NAME};
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};

        try (Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORIES, projection, selection, selectionArgs, null, null, null)) {
            if (cursor.moveToFirst()) {
                categoryName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            }
        }
        return categoryName;
    }

    @Override
    public void onReportClick(Problem problem) {
        // TODO: Implement detail view or status update simulation
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
