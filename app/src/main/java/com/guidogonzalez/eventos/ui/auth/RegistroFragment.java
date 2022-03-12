package com.guidogonzalez.eventos.ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.guidogonzalez.eventos.R;
import com.guidogonzalez.eventos.databinding.FragmentRegistroBinding;
import com.guidogonzalez.eventos.model.Usuario;
import com.guidogonzalez.eventos.utils.Utils;
import com.guidogonzalez.eventos.viewmodel.auth.RegistroViewModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistroFragment extends Fragment {

    private FragmentRegistroBinding binding;
    private RegistroViewModel registroViewModel;
    private String sFechaGuardar = "";
    private Bitmap imageBitmap;
    private Uri path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegistroBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registroViewModel = new ViewModelProvider(this).get(RegistroViewModel.class);

        // Cuando hagamos click en el EditText de Fecha evento se nos abrirá el DatetimePicker
        binding.etFechaNacimientoUsuario.setOnClickListener(v -> sFechaGuardar = Utils.showDatePicker(getContext(), binding.etFechaNacimientoUsuario));

        // Cuando hagamos click en el botón de Subir fotos, se nos abrirá la galería para seleccionar las fotos
        binding.civFotoUsuario.setOnClickListener(v -> seleccionarImagen());

        binding.btnRegistrarUsuario.setOnClickListener(v -> {

            String nombre = binding.etNombreUsuario.getText().toString().trim();
            String apellidos = binding.etApellidosUsuario.getText().toString().trim();
            String email = binding.etEmailUsuario.getText().toString().trim();
            String fechaNacimiento = binding.etFechaNacimientoUsuario.getText().toString().trim();
            String contrasena = binding.etContrasenaUsuario.getText().toString().trim();
            String confirmarContrasena = binding.etConfirmarContrasenaUsuario.getText().toString().trim();

            // Validamos que no están vacíos los campos y las contraseñas son iguales
            if (!validarDatos(nombre, apellidos, email, fechaNacimiento, contrasena, confirmarContrasena)) {

                // Creamos File para subir la foto
                File file = Utils.bitmapToFile(imageBitmap);

                // Creamos el Body para meterlo en la creación del Evento
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                MultipartBody.Part uploads = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);

                RequestBody rbNombre = RequestBody.create(MediaType.parse("multipart/form-data"), nombre);
                RequestBody rbApellidos = RequestBody.create(MediaType.parse("multipart/form-data"), apellidos);
                RequestBody rbEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email);
                RequestBody rbFechaNacimiento = RequestBody.create(MediaType.parse("multipart/form-data"), sFechaGuardar);
                RequestBody rbContrasena = RequestBody.create(MediaType.parse("multipart/form-data"), contrasena);

                // Llamamos al viewmodel para crear el nuevo evento
                registroViewModel.registrar(
                        rbNombre,
                        rbApellidos,
                        rbEmail,
                        rbFechaNacimiento,
                        uploads,
                        rbContrasena);
            }
        });

        // Observamos el Viewmodel para comprobar que se ha creado el usuario
        observarViewModel();
    }

    private Boolean validarDatos(String nombre, String apellidos, String email, String fechaNacimiento, String contrasena, String confirmarContrasena) {

        if (!confirmarContrasena.equals(contrasena)) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_contrasenas_error));
            return true;
        }

        if (nombre.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_nombre_campo_obligatorio));
            return true;
        }

        if (apellidos.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_apellidos_campo_obligatorio));
            return true;
        }

        if (email.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_email_campo_obligatorio));
            return true;
        }

        if (fechaNacimiento.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_fecha_nacimiento_obligatorio));
            return true;
        }

        if (contrasena.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_contrasena_campo_obligatorio));
            return true;
        }

        if (confirmarContrasena.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_contrasena_campo_obligatorio));
            return true;
        }

        if (imageBitmap == null) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_fotos_campo_obligatorio));
            return true;
        }

        return false;
    }

    private void observarViewModel() {

        registroViewModel.mldRegistoResponse.observe(getViewLifecycleOwner(), usuario -> {

            if (usuario != null && usuario instanceof Usuario) {
                Utils.notificarExito(getContext(), getString(R.string.mensaje_registro_exito));
                Navigation.findNavController(getView()).navigate(R.id.action_registroFragment_to_loginFragment);

                // Guardar email
            }
        });

        registroViewModel.bRegistroError.observe(getViewLifecycleOwner(), esError -> {

            if (esError != null && esError instanceof Boolean) {

                if (esError) {
                    Utils.notificarError(getContext(), getString(R.string.mensaje_registro_error));
                }
            }
        });

        registroViewModel.bRegistroCargando.observe(getViewLifecycleOwner(), estaCargando -> {

            if (estaCargando != null && estaCargando instanceof Boolean) {
                binding.pbRegistro.setVisibility(estaCargando ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        path = data.getData();

                        try {

                            imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                            binding.civFotoUsuario.setImageBitmap(imageBitmap);

                        } catch (Exception e) {
                        }
                    }
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}