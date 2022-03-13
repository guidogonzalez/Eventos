package com.guidogonzalez.eventos.api;

import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.model.Usuario;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    @GET("eventos")
    Single<List<Evento>> getEventos(@Header("Authorization") String bearer);

    @GET("eventos/{id}")
    Single<Evento> getEvento(@Header("Authorization") String bearer,
                             @Path("id") String id);

    @Multipart
    @POST("nuevoEvento")
    Single<Evento> nuevoEvento(@Header("Authorization") String bearer,
                                       @Part("nombre") RequestBody nombre,
                                       @Part("descripcion") RequestBody descripcion,
                                       @Part("fechaEvento") RequestBody fechaEvento,
                                       @Part MultipartBody.Part fotos,
                                       @Part("precio") RequestBody precio,
                                       @Part("idCreador") RequestBody idCreador,
                                       @Part("fotoCreador") RequestBody fotoCreador,
                                       @Part("nombreCreador") RequestBody nombreCreador);

    @Multipart
    @PUT("eventos/{id}")
    Single<Evento> actualizarEvento(@Header("Authorization") String bearer,
                                    @Path("id") String id,
                                    @Part("nombre") RequestBody nombre,
                                    @Part("descripcion") RequestBody descripcion,
                                    @Part("fechaEvento") RequestBody fechaEvento,
                                    @Part MultipartBody.Part fotos,
                                    @Part("precio") RequestBody precio,
                                    @Part("fotoCreador") RequestBody fotoCreador,
                                    @Part("nombreCreador") RequestBody nombreCreador);

    @DELETE("eventos/{id}")
    Single<Evento> eliminarEvento(@Header("Authorization") String bearer,
                                  @Path("id") String id);

    @FormUrlEncoded
    @POST("login")
    Single<Usuario> login(@Field("email") String email, @Field("contrasena") String contrasena);

    @Multipart
    @POST("registrar")
    Single<Usuario> registrar(@Part("nombre") RequestBody nombre,
                              @Part("apellidos") RequestBody apellidos,
                              @Part("email") RequestBody email,
                              @Part("fechaNacimiento") RequestBody fechaNacimiento,
                              @Part MultipartBody.Part foto,
                              @Part("contrasena") RequestBody contrasena);

    @GET("usuario/{id}")
    Single<Usuario> consultarUsuario(@Header("Authorization") String bearer, @Path("id") String idUsuario);

    @Multipart
    @PUT("usuario/{id}")
    Single<Usuario> actualizarUsuario(@Header("Authorization") String bearer,
                                      @Path("id") String idUsuario,
                                      @Part("nombre") RequestBody nombre,
                                      @Part("apellidos") RequestBody apellidos,
                                      @Part("email") RequestBody email,
                                      @Part("fechaNacimiento") RequestBody fechaNacimiento,
                                      @Part MultipartBody.Part foto,
                                      @Part("contrasena") RequestBody contrasena);
}
