package com.guidogonzalez.eventos.api;

import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.model.Usuario;
import com.guidogonzalez.eventos.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private Api api;

    public ApiService() {
        api = new Retrofit.Builder()
                .baseUrl(Utils.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(Api.class);
    }

    public Single<List<Evento>> getEventos(String bearer) {
        return api.getEventos(bearer);
    }

    public Single<Evento> getEvento(String bearer,
                                    String id) {
        return api.getEvento(bearer, id);
    }

    public Single<Evento> nuevoEvento(String bearer,
                                      RequestBody nombre,
                                      RequestBody descripcion,
                                      RequestBody fechaEvento,
                                      MultipartBody.Part fotos,
                                      RequestBody precio,
                                      RequestBody idCreador) {
        return api.nuevoEvento(bearer, nombre, descripcion, fechaEvento, fotos, precio, idCreador);
    }

    public Single<Evento> actualizarEvento(String bearer,
                                           String idEvento,
                                           RequestBody nombre,
                                           RequestBody descripcion,
                                           RequestBody fechaEvento,
                                           MultipartBody.Part fotos,
                                           RequestBody precio,
                                           RequestBody fotoCreador,
                                           RequestBody nombreCreador) {
        return api.actualizarEvento(bearer, idEvento, nombre, descripcion, fechaEvento, fotos, precio, fotoCreador, nombreCreador);
    }

    public Single<Evento> eliminarEvento(String bearer, String id) {
        return api.eliminarEvento(bearer, id);
    }

    public Single<Usuario> login(String email, String contrasena) {
        return api.login(email, contrasena);
    }

    public Single<Usuario> registrar(RequestBody nombre,
                                     RequestBody apellidos,
                                     RequestBody email,
                                     RequestBody fechaNacimiento,
                                     MultipartBody.Part foto,
                                     RequestBody contrasena) {
        return api.registrar(nombre, apellidos, email, fechaNacimiento, foto, contrasena);
    }


    public Single<Usuario> consultarUsuario(String bearer, String idUsuario) {
        return api.consultarUsuario(bearer, idUsuario);
    }

    public Single<Usuario> actualizarUsuario(String bearer,
                                             String idUsuario,
                                             RequestBody nombre,
                                             RequestBody apellidos,
                                             RequestBody email,
                                             RequestBody fechaNacimiento,
                                             MultipartBody.Part foto,
                                             RequestBody contrasena) {
        return api.actualizarUsuario(bearer, idUsuario, nombre, apellidos, email, fechaNacimiento, foto, contrasena);
    }
}
