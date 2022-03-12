package com.guidogonzalez.eventos.api;

import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.model.LoginResponse;
import com.guidogonzalez.eventos.model.Usuario;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    // Para obtener todos los eventos
    @GET("eventos")
    Single<List<Evento>> getEventos();

    @Multipart
    @POST("nuevoEvento")
    Single<Evento> nuevoEvento(@Part("nombre") RequestBody nombre,
                               @Part("descripcion") RequestBody descripcion,
                               @Part("fechaEvento") RequestBody fechaEvento,
                               @Part MultipartBody.Part fotos,
                               @Part("precio") RequestBody precio,
                               @Part("idCreador") RequestBody idCreador);

    @FormUrlEncoded
    @POST("login")
    Single<LoginResponse> login(@Field("email") String email, @Field("contrasena") String contrasena);

    @Multipart
    @POST("registrar")
    Single<Usuario> registrar(@Part("nombre") RequestBody nombre,
                              @Part("apellidos") RequestBody apellidos,
                              @Part("email") RequestBody email,
                              @Part("fechaNacimiento") RequestBody fechaNacimiento,
                              @Part MultipartBody.Part foto,
                              @Part("contrasena") RequestBody contrasena);

    @GET("usuario/{id}")
    Single<Usuario> consultarUsuario(@Path("id") String idUsuario);

    @Multipart
    @PUT("usuario/{id}")
    Single<Usuario> actualizarUsuario(@Path("id") String idUsuario,
                                      @Part("nombre") RequestBody nombre,
                                      @Part("apellidos") RequestBody apellidos,
                                      @Part("email") RequestBody email,
                                      @Part("fechaNacimiento") RequestBody fechaNacimiento,
                                      @Part MultipartBody.Part foto,
                                      @Part("contrasena") RequestBody contrasena);
}
