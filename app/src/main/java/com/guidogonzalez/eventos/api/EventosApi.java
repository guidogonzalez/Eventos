package com.guidogonzalez.eventos.api;

import com.guidogonzalez.eventos.model.Evento;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface EventosApi {

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
}
