package com.guidogonzalez.eventos.ui.evento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.guidogonzalez.eventos.adapter.EventosAdapter;
import com.guidogonzalez.eventos.databinding.FragmentEventosBinding;
import com.guidogonzalez.eventos.viewmodel.evento.EventosViewModel;

import java.util.ArrayList;
import java.util.List;

public class EventosFragment extends Fragment {

    private EventosViewModel eventosViewModel;
    private FragmentEventosBinding binding;
    private EventosAdapter listaEventosAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEventosBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventosViewModel = new ViewModelProvider(this).get(EventosViewModel.class);
        listaEventosAdapter = new EventosAdapter(new ArrayList<>(), eventosViewModel);
        eventosViewModel.cargarEventos();

        binding.rvEventos.setAdapter(listaEventosAdapter);

        observarViewModel();
    }

    private void observarViewModel() {

        eventosViewModel.mldListaEventos.observe(getViewLifecycleOwner(), eventos -> {

            if (eventos != null && eventos instanceof List) {

                if (eventos.isEmpty()) {
                    binding.contenedorVacio.setVisibility(View.VISIBLE);
                } else {
                    binding.contenedorVacio.setVisibility(View.GONE);
                }
                binding.rvEventos.setVisibility(View.VISIBLE);
                listaEventosAdapter.actualizarListaEventos(eventos);
            }
        });

        eventosViewModel.bEventoErrorCargar.observe(getViewLifecycleOwner(), esError -> {

            if (esError != null && esError instanceof Boolean) {

                binding.tvErrorCargar.setVisibility(esError ? View.VISIBLE : View.GONE);
            }
        });

        eventosViewModel.bEventoCargando.observe(getViewLifecycleOwner(), estaCargando -> {

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