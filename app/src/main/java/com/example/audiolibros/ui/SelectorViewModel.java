package com.example.audiolibros.ui;

import com.example.audiolibros.model.Libro;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SelectorViewModel extends ViewModel {

   private MutableLiveData<List<Libro>> libroLiveData;
   private List<Libro> libroList;

   public SelectorViewModel() {
      libroLiveData = new MutableLiveData<>();
      init(); // call your Rest API in init method
   }

   public MutableLiveData<List<Libro>> getLibroMutableLiveData() { //ArrayList en el original
      return libroLiveData;
   }

   public void init(){
      libroList = Libro.ejemploLibros();
      libroLiveData.setValue(libroList);
   }

   public Libro get(int id){
      return libroList.get(id);
   }

   public void delete(int id){
      libroList.remove(id);
      libroLiveData.setValue(libroList);
   }

   public void insert(Libro libro){
      libroList.add(0, libro);
      libroLiveData.setValue(libroList);
   }

}
