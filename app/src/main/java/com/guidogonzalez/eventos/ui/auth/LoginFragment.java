package com.guidogonzalez.eventos.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.guidogonzalez.eventos.R;
import com.guidogonzalez.eventos.databinding.FragmentLoginBinding;
import com.guidogonzalez.eventos.model.Usuario;
import com.guidogonzalez.eventos.utils.Utils;
import com.guidogonzalez.eventos.viewmodel.auth.LoginViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Utils.obtenerValorSharedPreferences(getContext(), "token") != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);
        }

        if (Utils.obtenerValorSharedPreferences(getContext(), "email") != null) {
            binding.etLoginEmail.setText(Utils.obtenerValorSharedPreferences(getContext(), "email"));
        }

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {

            String email = binding.etLoginEmail.getText().toString().trim();
            String contrasena = binding.etLoginContrasena.getText().toString().trim();

            if (!validarDatos(email, contrasena)) {
                loginViewModel.login(email, contrasena);
            }
        });

        binding.tvLoginRegistrarse.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registroFragment));

        observarViewModel();
    }

    private void observarViewModel() {

        loginViewModel.mldLoginResponse.observe(getViewLifecycleOwner(), loginResponse -> {

            if (loginResponse != null && loginResponse instanceof Usuario) {
                Utils.notificarExito(getContext(), getString(R.string.mensaje_login_exito));
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);
                binding.btnLogin.setEnabled(false);

                Utils.guardarDatosLogin(getContext(), loginResponse);
                loginViewModel.mldLoginResponse.removeObservers(this);
            }
        });

        loginViewModel.bLoginError.observe(getViewLifecycleOwner(), esError -> {

            if (esError != null && esError instanceof Boolean) {
                binding.btnLogin.setEnabled(true);
                Utils.notificarError(getContext(), getString(R.string.mensaje_login_error));
            }
        });

        loginViewModel.bLoginCargar.observe(getViewLifecycleOwner(), estaCargando -> {

            if (estaCargando != null && estaCargando instanceof Boolean) {
                binding.pbLogin.setVisibility(estaCargando ? View.VISIBLE : View.GONE);

                if (estaCargando) {
                    binding.btnLogin.setEnabled(false);
                }
            }
        });
    }

    private Boolean validarDatos(String email, String contrasena) {

        if (email.isEmpty()) {
            binding.etLoginEmail.setError(getString(R.string.mensaje_email_campo_obligatorio));
            return true;
        }

        if (contrasena.isEmpty()) {
            binding.etLoginContrasena.setError(getString(R.string.mensaje_contrasena_campo_obligatorio));
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}