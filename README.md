# Eventos
<h1 align="center">Eventos</h1>

<p align="center">
Aplicación realizada en Java.
</p>
</br>

## Descargar

Através de [Descarga](https://github.com/guidogonzalez/Eventos/tree/master/descarga) para descargar la APK.
<br>
Para descargar la API podemos acceder a través del [repositorio](https://github.com/guidogonzalez/restapieventos) de la misma

<img src="/imagenes/eventos.png" align="right" width="25%"/>

## Información

- Java
- Patrón de diseño MVVM
- [Glide](https://github.com/bumptech/glide) - cargar imagenes.
- [Material-Components](https://github.com/material-components/material-components-android) - componentes de Material design como Card Views, Text Inputs, Buttons.
- [Retrofit](https://square.github.io/retrofit/) - llamadas HTTP
- [RxJava](https://github.com/ReactiveX/RxAndroid) - tareas asíncronas

## A destacar

Son apuntes a destacar que se puede encontrar u observar.
<br>
- Inconsistencias en Livedata, no llego a entender por qué al hacer una llamada a la API que es satisfactoria luego se ejecuta el método de onError del CompositeDisposable
- Diversos workarounds como por ejemplo para el Login y Logout
- Utilizado SharedPreferences para almacenamiento de datos del login/sensibles, cuando a lo mejor AccountManager puede ser una solución 
- Falta de validaciones tanto en los webservices como en la propia App, por ejemplo comprobar que el email tiene el formato correcto, etc.
- Por falta de tiempo y además de que me he olvidado completamente del filtro de búsqueda

## Capturas

<p align="center">
<img src="/imagenes/login.png" align="left" width="15%"/>
<img src="/imagenes/registro.png" align="center" width="15%"/>
<img src="/imagenes/nuevoevento.png" align="left" width="15%"/>
<img src="/imagenes/perfil.png" align="left" width="15%"/>
<img src="/imagenes/editarevento.png" align="left" width="15%"/>
</p>
