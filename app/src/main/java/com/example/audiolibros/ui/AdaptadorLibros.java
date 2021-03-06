package com.example.audiolibros.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.audiolibros.Aplicacion;
import com.example.audiolibros.R;
import com.example.audiolibros.model.Libro;

import java.util.List;

import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorLibros extends
        RecyclerView.Adapter<AdaptadorLibros.ViewHolder> {
   private LayoutInflater inflador;      //Crea Layouts a partir del XML
   protected List<Libro> listaLibros;    //Lista de libros a visualizar
   private Context contexto;
   private View.OnClickListener onClickListener;
   private View.OnLongClickListener onLongClickListener;

   public AdaptadorLibros(Context contexto, List<Libro> listaLibros) {
      inflador = (LayoutInflater) contexto
              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.listaLibros = listaLibros;
      this.contexto = contexto;
   }

   //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
   public static class ViewHolder extends RecyclerView.ViewHolder {
      public ImageView portada;
      public TextView titulo;
      public ViewHolder(View itemView) {
         super(itemView);
         portada = itemView.findViewById(R.id.portada);
         titulo = itemView.findViewById(R.id.titulo);
      }
   }

   // Creamos el ViewHolder con las vista de un elemento sin personalizar
   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      // Inflamos la vista desde el xml
      View v = inflador.inflate(R.layout.elemento_selector, null);
      v.setOnClickListener(onClickListener);
      v.setOnLongClickListener(onLongClickListener);      return new ViewHolder(v);
   }

   // Usando como base el ViewHolder y lo personalizamos
   @Override
   public void onBindViewHolder(final ViewHolder holder, int posicion) {
      Libro libro = listaLibros.get(posicion);
      holder.titulo.setText(libro.getTitulo());
      holder.itemView.setScaleX(1);
      holder.itemView.setScaleY(1);

//      holder.portada.setImageResource(libro.recursoImagen);
      Aplicacion aplicacion = (Aplicacion) contexto.getApplicationContext();
      aplicacion.getLectorImagenes().get(libro.getUrlImagen(),
              new ImageLoader.ImageListener() {
                 @Override public void onResponse(ImageLoader.ImageContainer
                                                          response, boolean isImmediate) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
                       holder.portada.setImageBitmap(bitmap);
                       Palette palette = Palette.from(bitmap).generate();
                       holder.itemView.setBackgroundColor(palette.getLightMutedColor(0));
                       holder.titulo.setBackgroundColor(palette.getLightVibrantColor(0));
                       holder.portada.invalidate();
                    }
                 }
                 @Override public void onErrorResponse(VolleyError error) {
                    holder.portada.setImageResource(R.drawable.books);
                 }
              });
   }

   // Indicamos el número de elementos de la lista
   @Override public int getItemCount() {
      return listaLibros.size();
   }

   public void setOnItemClickListener(View.OnClickListener onClickListener) {
      this.onClickListener = onClickListener;
   }

   public void setOnItemLongClickListener(View.OnLongClickListener
                                                  onLongClickListener) {
      this.onLongClickListener = onLongClickListener;
   }

}
