package com.guidogonzalez.eventos.ui.perfil;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import com.guidogonzalez.eventos.databinding.FragmentPerfilBinding;
import com.guidogonzalez.eventos.model.Usuario;
import com.guidogonzalez.eventos.utils.Utils;
import com.guidogonzalez.eventos.viewmodel.perfil.PerfilViewModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel perfilViewModel;
    private String sFechaGuardar = "";
    private Bitmap imageBitmap;
    private Uri path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        perfilViewModel.consultarUsuario(Utils.obtenerValorSharedPreferences(getContext(), "idUsuario"));

        // Cuando hagamos click en el EditText de Fecha evento se nos abrirá el DatePicker
        // Por cuestiones que desconozco no cogía la fecha correctamente llamando al método de Utils, por lo tanto he de crear otra vez el DatePicker
        binding.etFechaNacimientoUsuario.setOnClickListener(v -> {

            final Calendar currentDate = Calendar.getInstance();
            Calendar date = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat fechaMostrar = new SimpleDateFormat("dd-MMM-yyyy");

            new DatePickerDialog(getContext(), ((view1, year, month, dayOfMonth) -> {

                date.set(year, month, dayOfMonth);
                sFechaGuardar = dateFormat.format(date.getTime());
                String sFechaMostrar = fechaMostrar.format(date.getTime());
                binding.etFechaNacimientoUsuario.setText(sFechaMostrar);

            }), currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
        });

        // Cuando hagamos click en la foto, se nos abrirá la galería para seleccionar las fotos
        binding.civFotoUsuario.setOnClickListener(v -> seleccionarImagen());

        binding.btnActualizarUsuario.setOnClickListener(v -> {

            String nombre = binding.etNombreUsuario.getText().toString().trim();
            String apellidos = binding.etApellidosUsuario.getText().toString().trim();
            String email = binding.etEmailUsuario.getText().toString().trim();
            String fechaNacimiento = binding.etFechaNacimientoUsuario.getText().toString().trim();
            String contrasena = binding.etContrasenaUsuario.getText().toString().trim();
            String confirmarContrasena = binding.etConfirmarContrasenaUsuario.getText().toString().trim();

            // Validamos que no están vacíos los campos y las contraseñas son iguales
            if (!validarDatos(nombre, apellidos, email, fechaNacimiento, contrasena, confirmarContrasena)) {

                // Creamos File para subir la foto, envíamos null en caso contrario
                MultipartBody.Part uploads = null;
                // Comprobamos que hemos subido una imagen para meterla en el Body
                if (imageBitmap != null) {

                    File file = Utils.bitmapToFile(imageBitmap);

                    // Creamos el Body para meterlo en la creación del Evento
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                    uploads = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);
                }

                RequestBody rbNombre = RequestBody.create(MediaType.parse("multipart/form-data"), nombre);
                RequestBody rbApellidos = RequestBody.create(MediaType.parse("multipart/form-data"), apellidos);
                RequestBody rbEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email);
                RequestBody rbFechaNacimiento = RequestBody.create(MediaType.parse("multipart/form-data"), sFechaGuardar);
                RequestBody rbContrasena = RequestBody.create(MediaType.parse("multipart/form-data"), contrasena);

                // Llamamos al viewmodel para actualizar el usuario
                perfilViewModel.actualizarUsuario(
                        Utils.obtenerValorSharedPreferences(getContext(), "token"),
                        Utils.obtenerValorSharedPreferences(getContext(), "idUsuario"),
                        rbNombre,
                        rbApellidos,
                        rbEmail,
                        rbFechaNacimiento,
                        uploads,
                        rbContrasena);
            }
        });

        binding.btnCerrarSesion.setOnClickListener(v -> {
            Utils.eliminarValorSharedPreferences(getContext(), "token");
            Navigation.findNavController(v).navigate(R.id.action_perfilFragment_to_loginFragment);
            Utils.notificarExito(getContext(), getString(R.string.mensaje_sesion_cerrada_exito));
        });

        observarViewModel();
    }

    private void observarViewModel() {

        // Consulta de perfil
        perfilViewModel.mldConsultarUsuarioResponse.observe(getViewLifecycleOwner(), usuario -> {

            if (usuario != null && usuario instanceof Usuario) {

                binding.etNombreUsuario.setText(usuario.nombre);
                binding.etApellidosUsuario.setText(usuario.apellidos);
                binding.etEmailUsuario.setText(usuario.email);
                binding.etFechaNacimientoUsuario.setText(Utils.transformarDateBd(usuario.fechaNacimiento));
                Utils.aplicarImagen(getContext(), Utils.URL_BASE_IMAGEN + usuario.foto, binding.civFotoUsuario);
            }
        });

        perfilViewModel.bConsultarUsuarioError.observe(getViewLifecycleOwner(), esError -> {

            if (esError != null && esError instanceof Boolean) {

                if (esError) {
                    Utils.notificarError(getContext(), getString(R.string.mensaje_consultar_usuario_error));
                }
            }
        });

        perfilViewModel.bConsultarUsuarioCargando.observe(getViewLifecycleOwner(), estaCargando -> {

            if (estaCargando != null && estaCargando instanceof Boolean) {
                binding.pbRegistro.setVisibility(estaCargando ? View.VISIBLE : View.GONE);
            }
        });

        // Actualizar perfil
        perfilViewModel.mldActualizarResponse.observe(getViewLifecycleOwner(), usuario -> {

            if (usuario != null && usuario instanceof Usuario) {
                binding.civFotoUsuario.setEnabled(false);
                binding.btnActualizarUsuario.setEnabled(true);
                Utils.notificarExito(getContext(), getString(R.string.mensaje_actualizar_perfil_exito));

                // Actualizamos los datos guardados en el dispositivo
                Utils.guardarDatosLogin(getContext(), usuario);
            }
        });

        perfilViewModel.bActualizarError.observe(getViewLifecycleOwner(), esError -> {

            if (esError != null && esError instanceof Boolean) {

                if (esError) {
                    binding.btnActualizarUsuario.setEnabled(false);
                    Utils.notificarError(getContext(), getString(R.string.mensaje_actualizar_perfil_error));
                }
            }
        });

        perfilViewModel.bActualizarCargando.observe(getViewLifecycleOwner(), estaCargando -> {

            if (estaCargando != null && estaCargando instanceof Boolean) {
                binding.btnActualizarUsuario.setEnabled(false);
                binding.pbRegistro.setVisibility(estaCargando ? View.VISIBLE : View.GONE);
            }
        });
    }

    private Boolean validarDatos(String nombre, String apellidos, String email, String fechaNacimiento, String contrasena, String confirmarContrasena) {

        if (!contrasena.isEmpty() && !confirmarContrasena.equals(contrasena)) {
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

        return false;
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