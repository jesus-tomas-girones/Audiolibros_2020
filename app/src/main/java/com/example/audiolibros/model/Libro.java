package com.example.audiolibros.model;

import java.util.ArrayList;
import java.util.List;

public class Libro {
   private String titulo;
   private String autor;
   private String urlImagen;
   private String urlAudio;
   private String genero;   // Género literario
   private Boolean novedad; // Es una novedad
   private Boolean leido;   // Leído por el usuario

   private final static String G_TODOS = "Todos los géneros";
   public final static String G_EPICO = "Poema épico";
   public final static String G_S_XIX = "Literatura siglo XIX";
   public final static String G_SUSPENSE = "Suspense";
   public final static String[] G_ARRAY = new String[] {G_TODOS, G_EPICO,
           G_S_XIX, G_SUSPENSE };

   private Libro(String titulo, String autor, String urlImagen,
                String urlAudio, String genero, Boolean novedad, Boolean leido) {
      this.titulo = titulo; this.autor = autor;
      this.urlImagen = urlImagen; this.urlAudio = urlAudio;
      this.genero = genero; this.novedad = novedad; this.leido = leido;
   }

   public static List<Libro> ejemploLibros() {
      final String SERVIDOR = "http://mmoviles.upv.es/audiolibros/";
      List<Libro> libros = new ArrayList<>();
      libros.add(new Libro("Kappa", "Akutagawa",
              SERVIDOR+"kappa.jpg", SERVIDOR+"kappa.mp3",
              Libro.G_S_XIX, false, false));
      libros.add(new Libro("Avecilla", "Alas Clarín, Leopoldo",
              SERVIDOR+"avecilla.jpg", SERVIDOR+"avecilla.mp3",
              Libro.G_S_XIX, true, false));
      libros.add(new Libro("Divina Comedia", "Dante",
              SERVIDOR+"divina_comedia.jpg", SERVIDOR+"divina_comedia.mp3",
              Libro.G_EPICO, true, false));
      libros.add(new Libro("Viejo Pancho, El", "Alonso y Trelles, José",
              SERVIDOR+"viejo_pancho.jpg", SERVIDOR+"viejo_pancho.mp3",
              Libro.G_S_XIX, true, true));
      libros.add(new Libro("Canción de Rolando", "Anónimo",
              SERVIDOR+"cancion_rolando.jpg",   SERVIDOR+"cancion_rolando.mp3",
              Libro.G_EPICO, false, true));
      libros.add(new Libro("Matrimonio de sabuesos", "Agata Christie",
              SERVIDOR+"matrim_sabuesos.jpg",SERVIDOR+"matrim_sabuesos.mp3",
              Libro.G_SUSPENSE, false, true));
      libros.add(new Libro("La iliada", "Homero",
              SERVIDOR+"la_iliada.jpg", SERVIDOR+"la_iliada.mp3",
              Libro.G_EPICO, true, false));
      return libros;
   }

   public String getTitulo() {
      return titulo;
   }

   public void setTitulo(String titulo) {
      this.titulo = titulo;
   }

   public String getAutor() {
      return autor;
   }

   public void setAutor(String autor) {
      this.autor = autor;
   }

   public String getUrlImagen() {
      return urlImagen;
   }

   public void setUrlImagen(String urlImagen) {
      this.urlImagen = urlImagen;
   }

   public String getUrlAudio() {
      return urlAudio;
   }

   public void setUrlAudio(String urlAudio) {
      this.urlAudio = urlAudio;
   }

   public String getGenero() {
      return genero;
   }

   public void setGenero(String genero) {
      this.genero = genero;
   }

   public Boolean getNovedad() {
      return novedad;
   }

   public void setNovedad(Boolean novedad) {
      this.novedad = novedad;
   }

   public Boolean getLeido() {
      return leido;
   }

   public void setLeido(Boolean leido) {
      this.leido = leido;
   }
}
