package eci.edu.byteProgramming.ejercicio.paper.videoclub;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Videoclub {

    public static void main(String[] args) {
        List<Pelicula> catalogo = new ArrayList<>();
        catalogo.add(new PeliculaFisica("Interestellar", 8000, true));
        catalogo.add(new PeliculaFisica("El Padrino", 7000, false));
        catalogo.add(new PeliculaDigital("Inception", 5000, true));
        catalogo.add(new PeliculaDigital("Matrix", 6000, true));

        Scanner sc = new Scanner(System.in);

        System.out.println("=== Videoclub de Don Mario ===");
        System.out.println("Tipo de membresia (basica/premium): ");
        String tipo = sc.nextLine().trim().toLowerCase();
        Membresia membresia = tipo.equals("premium") ? new MembresiaPremium() : new MembresiaBasica();

        System.out.println("\nPeliculas disponibles:");
        for (int i = 0; i < catalogo.size(); i++) {
            Pelicula p = catalogo.get(i);
            String estado = p.isDisponible() ? "Disponible" : "No disponible";
            System.out.printf("%d. [%s] %s - $%.0f - %s%n", i + 1, p.getTipo(), p.getTitulo(), p.getPrecio(), estado);
        }

        System.out.println("\nSeleccione peliculas (numeros separados por coma): ");
        String[] seleccion = sc.nextLine().split(",");

        List<Pelicula> alquiladas = new ArrayList<>();
        for (String s : seleccion) {
            int idx = Integer.parseInt(s.trim()) - 1;
            Pelicula p = catalogo.get(idx);
            if (p.isDisponible()) {
                alquiladas.add(p);
            } else {
                System.out.println("'" + p.getTitulo() + "' no esta disponible, se omite.");
            }
        }

        double subtotal = alquiladas.stream().mapToDouble(Pelicula::getPrecio).sum();
        double total = membresia.calcularTotal(subtotal);
        double descuento = subtotal - total;

        System.out.println("\n--- RECIBO DE ALQUILER ---");
        System.out.println("Cliente: " + membresia.getNombre());
        System.out.println("Peliculas:");
        for (Pelicula p : alquiladas) {
            System.out.printf(" - %s (%s) - $%.0f%n", p.getTitulo(), p.getTipo(), p.getPrecio());
        }
        System.out.printf("Subtotal: $%.0f%n", subtotal);
        if (descuento > 0) {
            System.out.printf("Descuento (20%%): $%.0f%n", descuento);
        }
        System.out.printf("Total a pagar: $%.0f%n", total);
        System.out.println("--------------------------");
        System.out.println("¡Disfrute su pelicula!");

        sc.close();
    }
}
