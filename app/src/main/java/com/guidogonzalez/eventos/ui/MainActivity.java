package com.guidogonzalez.eventos.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.guidogonzalez.eventos.R;
import com.guidogonzalez.eventos.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.navView, navController);

        observarNavController();
    }

    private void observarNavController() {
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {

            switch (navDestination.getId()) {

                case R.id.loginFragment:
                case R.id.registroFragment:
                    binding.navView.setVisibility(View.GONE);
                    break;

                default:
                    binding.navView.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

}