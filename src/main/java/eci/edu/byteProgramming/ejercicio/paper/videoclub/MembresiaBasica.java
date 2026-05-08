package eci.edu.byteProgramming.ejercicio.paper.videoclub;

public class MembresiaBasica implements Membresia {
    @Override
    public double calcularTotal(double subtotal) { return subtotal; }

    @Override
    public String getNombre() { return "Basica"; }
}
