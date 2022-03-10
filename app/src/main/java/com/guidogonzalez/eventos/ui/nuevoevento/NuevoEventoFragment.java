package com.guidogonzalez.eventos.ui.nuevoevento;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.guidogonzalez.eventos.databinding.FragmentNuevoEventoBinding;
import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.utils.Utils;
import com.guidogonzalez.eventos.viewmodel.nuevoevento.NuevoEventoViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NuevoEventoFragment extends Fragment {

    private NuevoEventoViewModel nuevoEventoViewModel;
    private FragmentNuevoEventoBinding binding;
    private String sFechaGuardar = "";
    private static final int IMG_REQUEST = -1;
    private Bitmap imageBitmap;
    private Uri path;
    private ActivityResultLauncher<String> activityResultLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNuevoEventoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nuevoEventoViewModel = new ViewModelProvider(this).get(NuevoEventoViewModel.class);

        // Cuando hagamos click en el EditText de Fecha evento se nos abrirá el DatetimePicker
        binding.etFechaEvento.setOnClickListener(v -> sFechaGuardar = Utils.showDateTimePicker(getContext(), binding.etFechaEvento));

        binding.btnSubirFotosEvento.setOnClickListener(v -> seleccionarImagen());
        binding.btnGuardarEvento.setOnClickListener(v -> {

            String nombre = binding.etNombreEvento.getText().toString().trim();
            String descripcion = binding.etDescripcionEvento.getText().toString().trim();
            String fechaEvento = binding.etFechaEvento.getText().toString().trim();
            String precio = binding.etPrecioEvento.getText().toString().trim();

            // Validamos que no están vacíos los campos
            validarDatos(nombre, descripcion, fechaEvento, binding.llFotosEvento.getChildCount(), precio);

            File file = bitmapToFile(imageBitmap, "temp.jpg");

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part uploads = MultipartBody.Part.createFormData("fotos", file.getName(), reqFile);

            RequestBody rbNombre = RequestBody.create(MediaType.parse("multipart/form-data"), nombre);
            RequestBody rbDescripcion = RequestBody.create(MediaType.parse("multipart/form-data"), descripcion);
            RequestBody rbFechaGuardar = RequestBody.create(MediaType.parse("multipart/form-data"), sFechaGuardar);
            RequestBody rbPrecio = RequestBody.create(MediaType.parse("multipart/form-data"), precio);
            RequestBody rbIdCreador = RequestBody.create(MediaType.parse("multipart/form-data"), "idCreador");

            // Llamamos al viewmodel para crear el nuevo evento
            nuevoEventoViewModel.crearEvento(
                    rbNombre,
                    rbDescripcion,
                    rbFechaGuardar,
                    uploads,
                    rbPrecio,
                    rbIdCreador);
        });

        // Observamos el Viewmodel para comprobar que se ha creado el evento
        observarViewModel(view);
    }


    private void validarDatos(String nombre, String descripcion, String fechaEvento, Integer fotos, String precio) {

        if (nombre.isEmpty()) {
            Utils.notificarInfo(getContext(), "El nombre es un campo obligatorio.");
            return;
        }

        if (descripcion.isEmpty()) {
            Utils.notificarInfo(getContext(), "La descripcion es un campo obligatorio.");
            return;
        }

        if (fechaEvento.isEmpty()) {
            Utils.notificarInfo(getContext(), "La fecha del evento es un campo obligatorio.");
            return;
        }

        if (precio.isEmpty()) {
            Utils.notificarInfo(getContext(), "El precio es un campo obligatorio.");
            return;
        }

        if (fotos == 0) {
            Utils.notificarInfo(getContext(), "Es obligatorio subir mínimo una foto.");
            return;
        }
    }

    private void observarViewModel(View view) {

        nuevoEventoViewModel.mldEvento.observe(getViewLifecycleOwner(), eventos -> {

            if (eventos != null && eventos instanceof Evento) {
                Utils.notificarExito(getContext(), "El evento ha sido creado con éxito");
                Navigation.findNavController(view).navigate(R.id.action_navigation_nuevo_to_navigation_home);
            }
        });

        nuevoEventoViewModel.bEventoErrorCargar.observe(getViewLifecycleOwner(), esError -> {

            if (esError != null && esError instanceof Boolean) {

                if (esError) {
                    Utils.notificarError(getContext(), "Ha ocurrido un error al crear el nuevo evento.");
                }
            }
        });

        nuevoEventoViewModel.bEventoCargando.observe(getViewLifecycleOwner(), estaCargando -> {

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

    public static File bitmapToFile(Bitmap bitmap, String fileNameToSave) { // File name like "image.png"

        // Creamos el File para escribir los datos del Bitmap
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileNameToSave);
            file.createNewFile();

            // Convertimos el Bitmap a Byte Array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

            // Escribimos los bytes en File
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}