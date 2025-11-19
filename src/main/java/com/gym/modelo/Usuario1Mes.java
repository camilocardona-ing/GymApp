package com.gym.modelo;

import java.time.LocalDate;

public class Usuario1Mes extends Usuario implements Verificable {
    public Usuario1Mes(String nombre, LocalDate fechaPago) { super(nombre, fechaPago, 1); }
    @Override public double calcularPrecio() { return 80000.0; } 
    @Override public boolean verificarDatos() { return nombre != null && !nombre.trim().isEmpty() && fechaPago != null; }
}
