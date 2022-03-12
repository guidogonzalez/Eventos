package com.guidogonzalez.eventos.viewmodel.evento;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.guidogonzalez.eventos.api.ApiService;
import com.guidogonzalez.eventos.model.Evento;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditarEventoViewModel extends AndroidViewModel {

    public MutableLiveData<Evento> mldConsultarEvento = new MutableLiveData<>();
    public MutableLiveData<Boolean> bConsultarEventoError = new MutableLiveData<>();
    public MutableLiveData<Boolean> bConsultarEventoCargando = new MutableLiveData<>();

    public MutableLiveData<Evento> mldActualizarEvento = new MutableLiveData<>();
    public MutableLiveData<Boolean> bActualizarEventoError = new MutableLiveData<>();
    public MutableLiveData<Boolean> bActualizarEventoCargando = new MutableLiveData<>();

    private ApiService apiService = new ApiService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public EditarEventoViewModel(@NonNull Application application) {
        super(application);
    }

    public void consultarEvento(String bearer,
                                String id) {

        bConsultarEventoError.setValue(true);

        // Cuando obtenemos los datos de la API, no queremos hacerlo en el hilo principal de la aplicación para no bloquearla
        compositeDisposable.add(
                apiService.getEvento(bearer, id)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Evento>() {

                            @Override
                            public void onSuccess(Evento evento) {
                                eventoConsultado(evento);
                            }

                            @Override
                            public void onError(Throwable e) {
                                bConsultarEventoError.setValue(true);
                                bConsultarEventoCargando.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    public void actualizarEvento(String bearer,
                                 String id,
                                 RequestBody nombre,
                                 RequestBody descripcion,
                                 RequestBody fechaEvento,
                                 MultipartBody.Part fotos,
                                 RequestBody precio,
                                 RequestBody fotoCreador,
                                 RequestBody nombreCreador) {

        bActualizarEventoCargando.setValue(true);

        // Cuando obtenemos los datos de la API, no queremos hacerlo en el hilo principal de la aplicación para no bloquearla
        compositeDisposable.add(
                apiService.actualizarEvento(bearer, id, nombre, descripcion, fechaEvento, fotos, precio, fotoCreador, nombreCreador)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Evento>() {

                            @Override
                            public void onSuccess(Evento evento) {
                                eventoActualizado(evento);
                            }

                            @Override
                            public void onError(Throwable e) {
                                bActualizarEventoError.setValue(true);
                                bActualizarEventoCargando.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void eventoConsultado(Evento evento) {
        mldConsultarEvento.setValue(evento);
        bConsultarEventoError.setValue(false);
        bConsultarEventoCargando.setValue(false);
    }

    private void eventoActualizado(Evento evento) {
        mldActualizarEvento.setValue(evento);
        bActualizarEventoError.setValue(false);
        bActualizarEventoCargando.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}