package com.guidogonzalez.eventos.viewmodel.evento;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.guidogonzalez.eventos.api.ApiService;
import com.guidogonzalez.eventos.model.Evento;
import com.guidogonzalez.eventos.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EventosViewModel extends AndroidViewModel {

    public MutableLiveData<List<Evento>> mldListaEventos = new MutableLiveData<>();
    public MutableLiveData<Boolean> bEventoErrorCargar = new MutableLiveData<>();
    public MutableLiveData<Boolean> bEventoCargando = new MutableLiveData<>();

    public MutableLiveData<Evento> mldEliminarEvento = new MutableLiveData<>();
    public MutableLiveData<Boolean> bEliminarEventoError = new MutableLiveData<>();
    public MutableLiveData<Boolean> bEliminarEventoCargando = new MutableLiveData<>();

    private ApiService apiService = new ApiService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public EventosViewModel(@NonNull Application application) {
        super(application);
    }

    public void cargarEventos() {

        bEventoCargando.setValue(true);

        compositeDisposable.add(

                apiService.getEventos(Utils.obtenerValorSharedPreferences(getApplication(), "token"))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<Evento>>() {

                            @Override
                            public void onSuccess(List<Evento> eventos) {
                                eventosRecibidos(eventos);
                            }

                            @Override
                            public void onError(Throwable e) {

                                bEventoErrorCargar.setValue(true);
                                bEventoCargando.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    public void eliminarEvento(String bearer,
                               String id) {

        bEventoCargando.setValue(true);

        compositeDisposable.add(

                apiService.eliminarEvento(bearer, id)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Evento>() {

                            @Override
                            public void onSuccess(Evento evento) {
                                eventoEliminado(evento);
                            }

                            @Override
                            public void onError(Throwable e) {

                                bEliminarEventoError.setValue(true);
                                bEliminarEventoCargando.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void eventosRecibidos(List<Evento> listaEventos) {

        mldListaEventos.setValue(listaEventos);
        bEventoErrorCargar.setValue(false);
        bEventoCargando.setValue(false);
    }

    private void eventoEliminado(Evento evento) {

        mldEliminarEvento.setValue(evento);
        bEliminarEventoError.setValue(false);
        bEliminarEventoCargando.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}