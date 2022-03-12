package com.guidogonzalez.eventos.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.guidogonzalez.eventos.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static final String URL_BASE = "http://192.168.1.134:3000/api/";
    public static final String URL_BASE_IMAGEN = "http://192.168.1.134:3000/";

    public static Date transformarFecha(String sFecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = new Date();
        try {
            date = sdf.parse(sFecha);
        } catch (Exception e) { }
        return date;
    }

    public static String transformarDateBd(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String fechaMostrar = "";
        try {
            fechaMostrar = sdf.format(date.getTime());
        } catch (Exception e) {

        }

        return fechaMostrar;
    }

    // Para mostrar los dialogs de seleccionar fecha y hora
    public static String showDateTimePicker(Context context, EditText editText) {

        final Calendar currentDate = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat fechaMostrar = new SimpleDateFormat("dd-MMM-yyyy hh:mm");

        final String[] sFechaGuardar = {dateFormat.format(date.getTime())};

        new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);

                sFechaGuardar[0] = dateFormat.format(date.getTime());
                String sFechaMostrar = fechaMostrar.format(date.getTime());

                editText.setText(sFechaMostrar);

            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();

        return sFechaGuardar[0];
    }

    // Para mostrar el dialog de seleccionar fecha
    public static String showDatePicker(Context context, EditText editText) {

        final Calendar currentDate = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat fechaMostrar = new SimpleDateFormat("dd-MMM-yyyy");

        // Lo obliga Android ni idea de como solucionarlo para evitar el Array
        final String[] sFechaGuardar = {dateFormat.format(date.getTime())};

        new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            sFechaGuardar[0] = dateFormat.format(date.getTime());
            Log.d("FECHAGUARDAR", sFechaGuardar[0]);
            String sFechaMostrar = fechaMostrar.format(date.getTime());
            editText.setText(sFechaMostrar);

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();

        return sFechaGuardar[0];
    }

    // Toasts personalizados
    public static void notificarError(Context context, String texto) {

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);

        View view = LayoutInflater.from(context).inflate(R.layout.toast_personalizado, null);
        ((TextView) view.findViewById(R.id.tvToastMensaje)).setText(texto);
        ((ImageView) view.findViewById(R.id.ivIcono)).setImageResource(R.drawable.ic_cerrar);
        ((CardView) view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.colorRojo_0));

        toast.setView(view);
        toast.show();
    }

    public static void notificarExito(Context context, String texto) {

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);

        View view = LayoutInflater.from(context).inflate(R.layout.toast_personalizado, null);
        ((TextView) view.findViewById(R.id.tvToastMensaje)).setText(texto);
        ((ImageView) view.findViewById(R.id.ivIcono)).setImageResource(R.drawable.ic_hecho);
        ((CardView) view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.colorVerde_0));

        toast.setView(view);
        toast.show();
    }

    public static void notificarInfo(Context context, String texto) {

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);

        View view = LayoutInflater.from(context).inflate(R.layout.toast_personalizado, null);
        ((TextView) view.findViewById(R.id.tvToastMensaje)).setText(texto);
        ((ImageView) view.findViewById(R.id.ivIcono)).setImageResource(R.drawable.ic_notificacion);
        ((CardView) view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.purple_500));

        toast.setView(view);
        toast.show();
    }

    // Aplicar imagen con Glide
    public static void aplicarImagen(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    // Workaround: pasamos Bitmap a File guardandolo en el dispositivo por problemas de acceso al storage
    public static File bitmapToFile(Bitmap bitmap) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar date = Calendar.getInstance();
        String nombreArchivo = sdf.format(date.getTime());
        // Creamos el File para escribir los datos del Bitmap
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + nombreArchivo + ".jpeg");
            file.createNewFile();

            // Convertimos el Bitmap a Byte Array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos);
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
}
