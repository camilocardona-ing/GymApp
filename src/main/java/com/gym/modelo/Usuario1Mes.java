package com.gym.modelo;

import java.time.LocalDate;

/** Clase hija: plan de 1 mes */
public class Usuario1Mes extends Usuario implements Verificable {
    public Usuario1Mes(String nombre, LocalDate fechaPago) { super(nombre, fechaPago, 1); }
    @Override public double calcularPrecio() { return 80000.0; } // ejemplo
    @Override public boolean verificarDatos() { return nombre != null && !nombre.trim().isEmpty() && fechaPago != null; }
}
