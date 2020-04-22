package com.example.audiolibros.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
//import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.audiolibros.AdaptadorLibros;
import com.example.audiolibros.AdaptadorLibrosFiltro;
import com.example.audiolibros.Aplicacion;
import com.example.audiolibros.Libro;
import com.example.audiolibros.MainActivity;
import com.example.audiolibros.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorFragment extends Fragment
        implements Animator.AnimatorListener{
   private MainActivity actividad;
   private RecyclerView recyclerView;
   private AdaptadorLibrosFiltro adaptador;
   private List<Libro> listaLibros;

   /**  ?????????????????????
    * Mandatory empty constructor for the fragment manager to instantiate the
    * fragment (e.g. upon screen orientation changes).
    */
   public SelectorFragment() { }

   @Override public void onAttach(Context context) {
      super.onAttach(context);
      actividad = (MainActivity) context;
      Aplicacion app = (Aplicacion) actividad.getApplication();
      adaptador = app.getAdaptador();
      listaLibros = app.getListaLibros();
   }

   @Override public View onCreateView(LayoutInflater inflador, ViewGroup
           contenedor, Bundle savedInstanceState) {
      setHasOptionsMenu(true);
      View vista = inflador.inflate(R.layout.fragment_selector,
              contenedor, false);
      recyclerView = (RecyclerView) vista.findViewById(
              R.id.recycler_view);
      recyclerView.setLayoutManager(new GridLayoutManager(actividad,2));
      recyclerView.setAdapter(adaptador);
      DefaultItemAnimator animator = new DefaultItemAnimator();
      animator.setAddDuration(1000);
      animator.setMoveDuration(1000);
      recyclerView.setItemAnimator(animator);
      adaptador.setOnItemClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            ((MainActivity) actividad).mostrarDetalle(
                    (int) adaptador.getItemId(
                          recyclerView.getChildAdapterPosition(v)));
         }
      });
      adaptador.setOnItemLongClickListener(new View.OnLongClickListener() {
         public boolean onLongClick(final View v) {
            final int id = recyclerView.getChildAdapterPosition(v);
            AlertDialog.Builder menu = new AlertDialog.Builder(actividad);
            CharSequence[] opciones = { "Compartir", "Borrar ", "Insertar" };
            menu.setItems(opciones, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int opcion) {
                  switch (opcion) {
                     case 0: //Compartir
                        Libro libro = listaLibros.get(id);
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, libro.titulo);
                        i.putExtra(Intent.EXTRA_TEXT, libro.urlAudio);
                        startActivity(Intent.createChooser(i, "Compartir"));
                        break;
                     case 1: //Borrar
                        Snackbar.make(v,"¿Estás seguro?", Snackbar.LENGTH_LONG)
                                .setAction("SI", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                      adaptador.borrar(id);
                                      Animator anim = AnimatorInflater.loadAnimator(actividad,
                                              R.animator.menguar);
                                      anim.addListener(SelectorFragment.this);
                                      anim.setTarget(v);
                                      anim.start();
                                   }
                                })
                                .show();
                        break;
                     case 2: //Insertar
                        //listaLibros.add(listaLibros.get(id));
                        int posicion = recyclerView.getChildLayoutPosition(v);
                        adaptador.insertar((Libro) adaptador.getItem(posicion));
                        adaptador.notifyItemInserted(0);
                        Snackbar.make(v,"Libro insertado", Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK", new View.OnClickListener() {
                                   @Override public void onClick(View view) { }
                                })
                                .show();
                        break;
                  }
               }
            });
            menu.create().show();
            return true;
         }
      });
      return vista;
   }

   @Override
   public void onStart() {
      super.onStart();
      actividad.mostrarBarraAmpliada(true);
   }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      if (menu.findItem(R.id.menu_buscar)==null) {
         inflater.inflate(R.menu.menu_selector, menu);
      }
      MenuItem searchItem = menu.findItem(R.id.menu_buscar);
      SearchView searchView = (SearchView) searchItem.getActionView();
      if (searchView!=null)
      searchView.setOnQueryTextListener(
              new SearchView.OnQueryTextListener() {
                 @Override
                 public boolean onQueryTextChange(String query) {
                    adaptador.setBusqueda(query);
                    adaptador.notifyDataSetChanged();
                    return false;
                 }
                 @Override
                 public boolean onQueryTextSubmit(String query) {
                    return false;
                 }
              });
      searchItem.setOnActionExpandListener(
              new MenuItem.OnActionExpandListener() {
                 @Override
                 public boolean onMenuItemActionCollapse(MenuItem item) {
                    adaptador.setBusqueda("");
                    adaptador.notifyDataSetChanged();
                    return true;  // Para permitir cierre
                 }
                 @Override
                 public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;  // Para permitir expansión
                 }
              });
      super.onCreateOptionsMenu(menu, inflater);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      if (id == R.id.menu_ultimo) {
         ((MainActivity) actividad).irUltimoVisitado();
         return true;
      } else if (id == R.id.menu_buscar) {
         return true;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public void onAnimationStart(Animator animation) { }

   @Override
   public void onAnimationEnd(Animator animation) {
      adaptador.notifyDataSetChanged();
   }

   @Override
   public void onAnimationCancel(Animator animation) { }

   @Override
   public void onAnimationRepeat(Animator animation) { }

}