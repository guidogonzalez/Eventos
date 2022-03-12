package com.guidogonzalez.eventos.api;

import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.model.LoginResponse;
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

    public Single<List<Evento>> getEventos() {
        return api.getEventos();
    }

    public Single<Evento> nuevoEvento(RequestBody nombre,
                                      RequestBody descripcion,
                                      RequestBody fechaEvento,
                                      MultipartBody.Part fotos,
                                      RequestBody precio,
                                      RequestBody idCreador) {
        return api.nuevoEvento(nombre, descripcion, fechaEvento, fotos, precio, idCreador);
    }

    public Single<LoginResponse> login(String email, String contrasena) {
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


    public Single<Usuario> consultarUsuario(String idUsuario) {
        return api.consultarUsuario(idUsuario);
    }

    public Single<Usuario> actualizarUsuario(String idUsuario,
                                             RequestBody nombre,
                                             RequestBody apellidos,
                                             RequestBody email,
                                             RequestBody fechaNacimiento,
                                             MultipartBody.Part foto,
                                             RequestBody contrasena) {
        return api.actualizarUsuario(idUsuario, nombre, apellidos, email, fechaNacimiento, foto, contrasena);
    }
}
