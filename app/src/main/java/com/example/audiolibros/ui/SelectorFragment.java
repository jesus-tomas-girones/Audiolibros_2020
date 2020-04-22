package com.example.audiolibros.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
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

import com.example.audiolibros.Aplicacion;
import com.example.audiolibros.model.Libro;
import com.example.audiolibros.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorFragment extends Fragment {
   private MainActivity actividad;
   private RecyclerView recyclerView;
   private AdaptadorLibrosFiltro adaptador;
   private SelectorViewModel viewModel;

   /**  ?????????????????????
    * Mandatory empty constructor for the fragment manager to instantiate the
    * fragment (e.g. upon screen orientation changes).
    */
   //public SelectorFragment() { }

   @Override public void onAttach(Context context) {
      super.onAttach(context);
      actividad = (MainActivity) context;
      Aplicacion app = (Aplicacion) actividad.getApplication();
      adaptador = app.getAdaptador();
      viewModel = ViewModelProviders.of(actividad).get(SelectorViewModel.class);
      viewModel.getLibroMutableLiveData().observe(actividad, libroListUpdateObserver);
   }

   Observer<List<Libro>> libroListUpdateObserver = new Observer<List<Libro>>() {
      @Override
      public void onChanged(List<Libro> libroList) {
         adaptador.setData(libroList);
      }
   };

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
                        Libro libro = viewModel.get(id); //**listaLibros.get(id);
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, libro.getTitulo());
                        i.putExtra(Intent.EXTRA_TEXT, libro.getUrlAudio());
                        startActivity(Intent.createChooser(i, "Compartir"));
                        break;
                     case 1: //Borrar
                        Snackbar.make(v,"¿Estás seguro?", Snackbar.LENGTH_LONG)
                                .setAction("SI", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                      viewModel.delete(id);//**adaptador.borrar(id);
                                      //No hace falta, se hace en adaptador.setData()
                                      // pero se pierden las animaciones
                                      //adaptador.notifyItemRemoved(pos);

                                      /*Animator anim = AnimatorInflater.loadAnimator(actividad,
                                              R.animator.menguar);
                                      anim.addListener(SelectorFragment.this);
                                      anim.setTarget(v);
                                      anim.start();*/
                                   }
                                })
                                .show();
                        break;
                     case 2: //Insertar
                        viewModel.insert(viewModel.get(id));
                        //No hace falta, se hace en adaptador.setData()
                        // pero se pierden las animaciones
                        //adaptador.notifyItemInserted(0);
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

}