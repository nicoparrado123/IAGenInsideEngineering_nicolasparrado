package eci.edu.byteProgramming.ejercicio.paper.videoclub;

public class PeliculaDigital extends Pelicula {
    public PeliculaDigital(String titulo, double precio, boolean disponible) {
        super(titulo, precio, disponible);
    }

    @Override
    public String getTipo() { return "Digital"; }
}
