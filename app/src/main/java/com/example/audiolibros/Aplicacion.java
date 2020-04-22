package com.example.audiolibros;

import android.app.Application;
//import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.audiolibros.model.Libro;
import com.example.audiolibros.ui.AdaptadorLibrosFiltro;

import java.util.List;

import androidx.collection.LruCache;

public class Aplicacion extends Application {
   private List<Libro> listaLibros;
   private AdaptadorLibrosFiltro adaptador;
   private static RequestQueue colaPeticiones;
   private static ImageLoader lectorImagenes;
//   private static SharedPreferences preferencias;

   @Override
   public void onCreate() {
      super.onCreate();
//      preferencias = getSharedPreferences(
//              "com.example.audiolibros_internal", MODE_PRIVATE);
      listaLibros = Libro.ejemploLibros();
      adaptador = new AdaptadorLibrosFiltro (this, listaLibros);
      colaPeticiones = Volley.newRequestQueue(this);
      lectorImagenes = new ImageLoader(colaPeticiones,
              new ImageLoader.ImageCache() {
                 private final LruCache<String, Bitmap> cache =
                         new LruCache<String, Bitmap>(10);
                 public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                 }
                 public Bitmap getBitmap(String url) {
                    return cache.get(url);
                 }
              });
   }

   public AdaptadorLibrosFiltro getAdaptador() {
      return adaptador;
   }

   public List<Libro> getListaLibros() {
      return listaLibros;
   }

   public static RequestQueue getColaPeticiones() {
      return colaPeticiones;
   }

   public static void setColaPeticiones(RequestQueue colaPeticiones) {
      Aplicacion.colaPeticiones = colaPeticiones;
   }

   public static ImageLoader getLectorImagenes() {
      return lectorImagenes;
   }

   public static void setLectorImagenes(ImageLoader lectorImagenes) {
      Aplicacion.lectorImagenes = lectorImagenes;
   }

/*   public static int getUltimoVisitado(){
      return preferencias.getInt("ultimo", 0);
   }

   public static void setUltimoVisitado(int pos){
      SharedPreferences.Editor editor = preferencias.edit();
      editor.putInt("ultimo", pos);
      editor.commit();
   }*/

}
