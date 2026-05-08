package eci.edu.byteProgramming.ejercicio.paper.videoclub;

public class MembresiaPremium implements Membresia {
    private static final double DESCUENTO = 0.20;

    @Override
    public double calcularTotal(double subtotal) { return subtotal * (1 - DESCUENTO); }

    @Override
    public String getNombre() { return "Premium"; }
}
