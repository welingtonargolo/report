package com.example.report.ui.profile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.report.data.DatabaseHelper;
import com.example.report.databinding.FragmentProfileBinding;
import com.example.report.ui.auth.LoginActivity;
import com.example.report.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        loadUserInfo();
        loadStatistics();
        setupLogoutButton();
    }

    private void loadUserInfo() {
        binding.nameText.setText(sessionManager.getUserName());
        binding.emailText.setText(sessionManager.getUserEmail());
    }

    private void loadStatistics() {
        long userId = sessionManager.getUserId();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Get total reports
        String[] totalProjection = {"COUNT(*)"};
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor totalCursor = db.query(
            DatabaseHelper.TABLE_PROBLEMS,
            totalProjection,
            selection,
            selectionArgs,
            null,
            null,
            null
        );

        if (totalCursor.moveToFirst()) {
            int totalReports = totalCursor.getInt(0);
            binding.totalReportsText.setText(String.valueOf(totalReports));
        }
        totalCursor.close();

        // Get resolved reports
        String[] resolvedProjection = {"COUNT(*)"};
        String resolvedSelection = DatabaseHelper.COLUMN_USER_ID + " = ? AND " +
                                 DatabaseHelper.COLUMN_STATUS + " = ?";
        String[] resolvedSelectionArgs = {String.valueOf(userId), "Resolvido"};

        Cursor resolvedCursor = db.query(
            DatabaseHelper.TABLE_PROBLEMS,
            resolvedProjection,
            resolvedSelection,
            resolvedSelectionArgs,
            null,
            null,
            null
        );

        if (resolvedCursor.moveToFirst()) {
            int resolvedReports = resolvedCursor.getInt(0);
            binding.resolvedReportsText.setText(String.valueOf(resolvedReports));
        }
        resolvedCursor.close();
    }

    private void setupLogoutButton() {
        binding.logoutButton.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
