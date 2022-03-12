package com.guidogonzalez.eventos.ui.evento;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.guidogonzalez.eventos.databinding.FragmentEditarEventoBinding;
import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.utils.Utils;
import com.guidogonzalez.eventos.viewmodel.evento.EditarEventoViewModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditarEventoFragment extends Fragment {

    FragmentEditarEventoBinding binding;
    EditarEventoViewModel editarEventoViewModel;
    private String idEvento;
    private String sFechaGuardar = "";
    private Bitmap imageBitmap;
    private Uri path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        idEvento = getArguments().getString("idEvento");
        binding = FragmentEditarEventoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editarEventoViewModel = new ViewModelProvider(this).get(EditarEventoViewModel.class);

        editarEventoViewModel.consultarEvento(Utils.obtenerValorSharedPreferences(getContext(), "token"), idEvento);

        // Cuando hagamos click en el EditText de Fecha evento se nos abrirá el DatetimePicker
        binding.etFechaEvento.setOnClickListener(v -> sFechaGuardar = Utils.showDateTimePicker(getContext(), binding.etFechaEvento));

        // Cuando hagamos click en el botón de Subir fotos, se nos abrirá la galería para seleccionar las fotos
        binding.btnSubirFotosEvento.setOnClickListener(v -> seleccionarImagen());

        binding.btnGuardarEvento.setOnClickListener(v -> {

            String nombre = binding.etNombreEvento.getText().toString().trim();
            String descripcion = binding.etDescripcionEvento.getText().toString().trim();
            String fechaEvento = binding.etFechaEvento.getText().toString().trim();
            String precio = binding.etPrecioEvento.getText().toString().trim();

            // Validamos que no están vacíos los campos
            if (!validarDatos(nombre, descripcion, fechaEvento, precio)) {

                // Creamos File para subir la foto
                MultipartBody.Part uploads = null;
                if (imageBitmap != null) {
                    File file = Utils.bitmapToFile(imageBitmap);

                    // Creamos el Body para meterlo en la creación del Evento
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                    uploads = MultipartBody.Part.createFormData("fotos", file.getName(), reqFile);
                }

                RequestBody rbNombre = RequestBody.create(MediaType.parse("multipart/form-data"), nombre);
                RequestBody rbDescripcion = RequestBody.create(MediaType.parse("multipart/form-data"), descripcion);
                RequestBody rbFechaGuardar = RequestBody.create(MediaType.parse("multipart/form-data"), sFechaGuardar);
                RequestBody rbPrecio = RequestBody.create(MediaType.parse("multipart/form-data"), precio);
                RequestBody rbFotoCreador = RequestBody.create(MediaType.parse("multipart/form-data"), Utils.obtenerValorSharedPreferences(getContext(), "foto"));
                RequestBody rbNombreCreador = RequestBody.create(MediaType.parse("multipart/form-data"), Utils.obtenerValorSharedPreferences(getContext(), "nombre"));

                // Llamamos al viewmodel para actualizar el Evento
                editarEventoViewModel.actualizarEvento(
                        Utils.obtenerValorSharedPreferences(getContext(), "token"),
                        idEvento,
                        rbNombre,
                        rbDescripcion,
                        rbFechaGuardar,
                        uploads,
                        rbPrecio,
                        rbFotoCreador,
                        rbNombreCreador);
            }
        });

        // Observamos el Viewmodel para comprobar que se ha creado el evento
        observarViewModel();
    }

    private Boolean validarDatos(String nombre, String descripcion, String fechaEvento, String precio) {

        if (nombre.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_nombre_campo_obligatorio));
            return true;
        }

        if (descripcion.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_descripcion_campo_obligatorio));
            return true;
        }

        if (fechaEvento.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_fecha_campo_obligatorio));
            return true;
        }

        if (precio.isEmpty()) {
            Utils.notificarInfo(getContext(), getString(R.string.mensaje_precio_campo_obligatorio));
            return true;
        }

        return false;
    }

    private void observarViewModel() {

        editarEventoViewModel.mldConsultarEvento.observe(getViewLifecycleOwner(), evento -> {

            if (evento != null && evento instanceof Evento) {
                binding.etNombreEvento.setText(evento.nombre);
                binding.etDescripcionEvento.setText(evento.nombre);
                binding.etFechaEvento.setText(Utils.transformarDateBd(evento.fechaEvento));
                binding.etPrecioEvento.setText(String.valueOf(evento.precio));

                // Recorremos las fotos para mostrarlas dinámicamente
                for (String urlFoto : evento.fotos.split(",")) {
                    ImageView imageView = new ImageView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
                    layoutParams.gravity = Gravity.CENTER;
                    layoutParams.topMargin = 8;
                    imageView.setLayoutParams(layoutParams);
                    Utils.aplicarImagen(getContext(), Utils.URL_BASE_IMAGEN + urlFoto, imageView);
                    binding.llFotosEvento.addView(imageView);
                }
            }
        });

        editarEventoViewModel.bConsultarEventoError.observe(getViewLifecycleOwner(), esError -> {

            if (esError != null && esError instanceof Boolean) {

                if (esError) {
                    Utils.notificarError(getContext(), getString(R.string.mensaje_consultar_evento_error));
                }
            }
        });

        editarEventoViewModel.bConsultarEventoCargando.observe(getViewLifecycleOwner(), estaCargando -> {

            if (estaCargando != null && estaCargando instanceof Boolean) {
                binding.pbGuardarEvento.setVisibility(estaCargando ? View.VISIBLE : View.GONE);
            }
        });

        editarEventoViewModel.mldActualizarEvento.observe(getViewLifecycleOwner(), evento -> {

            if (evento != null && evento instanceof Evento) {
                Utils.notificarExito(getContext(), getString(R.string.mensaje_actualizar_evento_exito));
                Navigation.findNavController(getView()).navigate(R.id.action_editarEventoFragment_to_listaEventosFragment);
            }
        });

        editarEventoViewModel.bActualizarEventoError.observe(getViewLifecycleOwner(), esError -> {

            if (esError != null && esError instanceof Boolean) {

                if (esError) {
                    Utils.notificarError(getContext(), getString(R.string.mensaje_actualizar_evento_error));
                }
            }
        });

        editarEventoViewModel.bActualizarEventoCargando.observe(getViewLifecycleOwner(), estaCargando -> {

            if (estaCargando != null && estaCargando instanceof Boolean) {
                binding.pbGuardarEvento.setVisibility(estaCargando ? View.VISIBLE : View.GONE);
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

                            ImageView imageView = new ImageView(getContext());
                            imageView.setImageBitmap(imageBitmap);
                            binding.llFotosEvento.addView(imageView);

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