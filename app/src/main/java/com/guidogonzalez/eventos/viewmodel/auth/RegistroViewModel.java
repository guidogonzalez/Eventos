package com.guidogonzalez.eventos.viewmodel.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.guidogonzalez.eventos.api.ApiService;
import com.guidogonzalez.eventos.model.Usuario;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistroViewModel extends AndroidViewModel {


    public MutableLiveData<Usuario> mldRegistoResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> bRegistroError = new MutableLiveData<>();
    public MutableLiveData<Boolean> bRegistroCargando = new MutableLiveData<>();

    private ApiService apiService = new ApiService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RegistroViewModel(@NonNull Application application) {
        super(application);
    }

    public void registrar(RequestBody nombre,
                          RequestBody apellidos,
                          RequestBody email,
                          RequestBody fechaNacimiento,
                          MultipartBody.Part foto,
                          RequestBody contrasena) {

        bRegistroCargando.setValue(true);

        // Cuando obtenemos los datos de la API, no queremos hacerlo en el hilo principal de la aplicación para no bloquearla
        compositeDisposable.add(
                apiService.registrar(nombre, apellidos, email, fechaNacimiento, foto, contrasena)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Usuario>() {

                            @Override
                            public void onSuccess(Usuario usuario) {
                                registroUsuario(usuario);
                            }

                            @Override
                            public void onError(Throwable e) {
                                bRegistroError.setValue(true);
                                bRegistroCargando.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void registroUsuario(Usuario usuario) {
        mldRegistoResponse.setValue(usuario);
        bRegistroError.setValue(false);
        bRegistroCargando.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}