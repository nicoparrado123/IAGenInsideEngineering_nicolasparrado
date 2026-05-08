package eci.edu.byteProgramming.ejercicio.paper.videoclub;

public abstract class Pelicula {
    protected String titulo;
    protected double precio;
    protected boolean disponible;

    public Pelicula(String titulo, double precio, boolean disponible) {
        this.titulo = titulo;
        this.precio = precio;
        this.disponible = disponible;
    }

    public abstract String getTipo();

    public String getTitulo() { return titulo; }
    public double getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }
}
