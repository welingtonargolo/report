package com.example.report;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.report.databinding.ActivityMainBinding;
import com.example.report.utils.PermissionUtils;
import com.example.report.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private NavController navController;
    private SessionManager sessionManager;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Inicialização segura do SessionManager
            sessionManager = new SessionManager(getApplicationContext());

            // Inflar o binding com tratamento de erro
            try {
                binding = ActivityMainBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());
            } catch (Exception e) {
                Log.e(TAG, "Erro ao inflar layout", e);
                showErrorAndRestart("Erro ao carregar a interface do aplicativo");
                return;
            }

            // Configuração segura da Toolbar
            try {
                setSupportActionBar(binding.toolbar);
            } catch (Exception e) {
                Log.w(TAG, "Erro ao configurar Toolbar", e);
            }


            setupNavigation();


            checkPermissions();

        } catch (Exception mainException) {
            Log.e(TAG, "Erro crítico na inicialização", mainException);
            recoverFromCriticalError();
        }
    }

    private void setupNavigation() {
        try {
            Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

            if (!(navHostFragment instanceof NavHostFragment)) {
                throw new IllegalStateException("NavHostFragment não encontrado ou tipo inválido");
            }

            navController = ((NavHostFragment) navHostFragment).getNavController();

            // Configuração da AppBar
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_map,
                    R.id.navigation_report,
                    R.id.navigation_history,
                    R.id.navigation_profile
            ).build();

            // Configuração segura da ActionBar
            try {
                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            } catch (Exception e) {
                Log.w(TAG, "Erro ao configurar ActionBar", e);
            }

            // Configuração segura da BottomNavigation
            if (binding.bottomNavigation != null) {
                try {
                    NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
                } catch (Exception e) {
                    Log.w(TAG, "Erro ao configurar BottomNavigation", e);
                }
            } else {
                Log.w(TAG, "BottomNavigationView não encontrada no layout");
            }

        } catch (Exception e) {
            Log.e(TAG, "Erro na configuração de navegação", e);
            showErrorAndRestart("Erro no sistema de navegação");
        }
    }

    private void checkPermissions() {
        try {
            if (!PermissionUtils.hasRequiredPermissions(this)) {
                PermissionUtils.requestRequiredPermissions(this);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro na verificação de permissões", e);
            Toast.makeText(this, "Erro ao verificar permissões", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (requestCode == PermissionUtils.REQUEST_CODE_PERMISSIONS) {
                boolean allGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                        break;
                    }
                }

                if (!allGranted) {
                    Toast.makeText(this,
                            "Algumas permissões são necessárias para o funcionamento completo do app",
                            Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro no tratamento de permissões", e);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        try {
            return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
        } catch (Exception e) {
            Log.e(TAG, "Erro na navegação Up", e);
            return super.onSupportNavigateUp();
        }
    }

    private void showErrorAndRestart(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Erro")
                .setMessage(message + "\nO aplicativo será reiniciado.")
                .setPositiveButton("OK", (dialog, which) -> restartApp())
                .setCancelable(false)
                .show();
    }

    private void recoverFromCriticalError() {
        if (binding != null) {
            try {
                binding.wait();
            } catch (Exception e) {
                Log.e(TAG, "Erro ao fazer unbind do binding", e);
            }
        }
        restartApp();
    }

    private void restartApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            try {
                binding.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
