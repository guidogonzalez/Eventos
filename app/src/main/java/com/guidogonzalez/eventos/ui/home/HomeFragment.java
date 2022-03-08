package com.guidogonzalez.eventos.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guidogonzalez.eventos.adapter.EventosAdapter;
import com.guidogonzalez.eventos.databinding.FragmentHomeBinding;
import com.guidogonzalez.eventos.viewmodel.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private EventosAdapter listaEventosAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listaEventosAdapter = new EventosAdapter(new ArrayList<>());

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.cargarEventos();

        binding.rvEventos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvEventos.setAdapter(listaEventosAdapter);

        observarViewModel();
    }

    private void observarViewModel() {

        homeViewModel.listaEventos.observe(getViewLifecycleOwner(), eventos -> {

            if (eventos != null && eventos instanceof List) {

                binding.rvEventos.setVisibility(View.VISIBLE);
                listaEventosAdapter.actualizarListaEventos(eventos);
            }
        });

        homeViewModel.bEventoErrorCargar.observe(getViewLifecycleOwner(), esError -> {

            if (esError != null && esError instanceof Boolean) {

                binding.tvErrorCargar.setVisibility(esError ? View.VISIBLE : View.GONE);
            }
        });

        homeViewModel.bEventoCargando.observe(getViewLifecycleOwner(), estaCargando -> {

            if (estaCargando != null && estaCargando instanceof Boolean) {

                binding.pbCargar.setVisibility(estaCargando ? View.VISIBLE : View.GONE);

                if (estaCargando) {
                    binding.rvEventos.setVisibility(View.GONE);
                    binding.tvErrorCargar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}