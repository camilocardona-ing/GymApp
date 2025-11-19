package com.gym.modelo;

import java.time.LocalDate;

public class Usuario3Meses extends Usuario implements Verificable {
    public Usuario3Meses(String nombre, LocalDate fechaPago) { super(nombre, fechaPago, 3); }
    @Override public double calcularPrecio() { return 210000.0; }
    @Override public boolean verificarDatos() { return nombre != null && !nombre.trim().isEmpty() && fechaPago != null; }
}
