package com.guidogonzalez.eventos.viewmodel.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.guidogonzalez.eventos.api.ApiService;
import com.guidogonzalez.eventos.model.LoginResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {

    public MutableLiveData<LoginResponse> mldLoginResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> bLoginError = new MutableLiveData<>();
    public MutableLiveData<Boolean> bLoginCargar = new MutableLiveData<>();

    private ApiService apiService = new ApiService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void login(String email, String contrasena) {

        bLoginCargar.setValue(true);

        compositeDisposable.add(
                apiService.login(email, contrasena)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {

                            @Override
                            public void onSuccess(LoginResponse loginResponse) {
                                loginRecibido(loginResponse);
                            }

                            @Override
                            public void onError(Throwable e) {
                                bLoginError.setValue(true);
                                bLoginCargar.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void loginRecibido(LoginResponse loginResponse) {
        mldLoginResponse.setValue(loginResponse);
        bLoginError.setValue(false);
        bLoginCargar.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
