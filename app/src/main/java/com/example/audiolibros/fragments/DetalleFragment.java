package com.example.audiolibros.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.audiolibros.Aplicacion;
import com.example.audiolibros.Libro;
import com.example.audiolibros.MainActivity;
import com.example.audiolibros.R;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

public class DetalleFragment extends Fragment implements
        View.OnTouchListener, MediaPlayer.OnPreparedListener,
        MediaController.MediaPlayerControl {
   public static String ARG_ID_LIBRO = "id_libro";
   MediaPlayer mediaPlayer;
   MediaController mediaController;

   /**  ?????????????????????
    * Mandatory empty constructor for the fragment manager to instantiate the
    * fragment (e.g. upon screen orientation changes).
    */
  public DetalleFragment() { }

   @Override
   public View onCreateView(LayoutInflater inflador, ViewGroup
           contenedor, Bundle savedInstanceState) {

      View vista = inflador.inflate(R.layout.fragment_detalle,
              contenedor, false);
      Bundle args = getArguments();
      if (args != null) {
         int position = args.getInt(ARG_ID_LIBRO);
         ponInfoLibro(position, vista);
      } else {
         SharedPreferences pref = getActivity().getSharedPreferences(
                 "com.example.audiolibros_internal", MODE_PRIVATE);
         int id = pref.getInt("ultimo", 0);
         ponInfoLibro(id, vista);
      }
      return vista;
   }

   private void ponInfoLibro(int id, View vista) {
      Libro libro = ((Aplicacion) getActivity().getApplication())
              .getListaLibros().get(id);
      ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
      ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
//      ((ImageView) vista.findViewById(R.id.portada))
//              .setImageResource(libro.recursoImagen);
      Aplicacion aplicacion = (Aplicacion) getActivity().getApplication();
      ((NetworkImageView) vista.findViewById(R.id.portada)).setImageUrl(
              libro.urlImagen,aplicacion.getLectorImagenes());

      vista.setOnTouchListener(this);
      if (mediaPlayer != null){
         mediaPlayer.release();
      }
      mediaPlayer = new MediaPlayer();
      mediaPlayer.setOnPreparedListener(this);
      mediaController = new MediaController(getActivity());
      Uri audio = Uri.parse(libro.urlAudio);
      try {
         mediaPlayer.setDataSource(getActivity(), audio);
         mediaPlayer.prepareAsync();
      } catch (IOException e) {
         Log.e("Audiolibros", "ERROR: No se puede reproducir "+audio,e);
      }
   }

   public void ponInfoLibro(int id) {
      ponInfoLibro(id, getView());
   }

   @Override
   public void onPrepared(MediaPlayer mediaPlayer) {
      Log.d("Audiolibros", "Entramos en onPrepared de MediaPlayer");
      SharedPreferences preferencias = PreferenceManager
              .getDefaultSharedPreferences(getActivity());
      if (preferencias.getBoolean("pref_autoreproducir", true)) {
         mediaPlayer.start();
      }
      mediaController.setMediaPlayer(this);
      View v = getView().findViewById(R.id.fragment_detalle);
      if (v != null) {
         mediaController.setAnchorView(v);
      } else {
         mediaController.setAnchorView(getView().findViewById(R.id.detalle_fragment));
      }
      mediaController.setEnabled(true);
      try {
         mediaController.show();
      } finally {
         Log.e("AudioLibros","ERROR al visionar MediaController");
      }
   }

   @Override
   public boolean onTouch(View vista, MotionEvent evento) {
      mediaController.show();
      return false;
   }

   @Override
   public void onDestroy() {
      try {
         mediaController.hide();
         mediaPlayer.stop();
         mediaPlayer.release();
      } catch (Exception e) {
         Log.d("Audiolibros", "Error en mediaPlayer.stop()");
      }
      super.onDestroy();//Stop();
   }

   @Override
   public boolean canPause() {
      return true;
   }

   @Override
   public boolean canSeekBackward() {
      return true;
   }

   @Override
   public boolean canSeekForward() {
      return true;
   }

   @Override
   public int getBufferPercentage() {
      return 0;
   }

   @Override
   public int getCurrentPosition() {
      try {
         return mediaPlayer.getCurrentPosition();
      } catch (Exception e) {
         return 0;
      }
   }

   @Override
   public int getDuration() {
      return mediaPlayer.getDuration();
   }

   @Override
   public boolean isPlaying() {
      return mediaPlayer.isPlaying();
   }

   @Override
   public void pause() {
      mediaPlayer.pause();
   }

   @Override
   public void seekTo(int pos) {
      mediaPlayer.seekTo(pos);
   }

   @Override
   public void start() {
      mediaPlayer.start();
   }

   @Override
   public int getAudioSessionId() {
      return 0;
   }

}