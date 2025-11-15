package com.gym.modelo;

import java.time.LocalDate;

public class Usuario6Meses extends Usuario implements Verificable {
    public Usuario6Meses(String nombre, LocalDate fechaPago) { super(nombre, fechaPago, 6); }
    @Override public double calcularPrecio() { return 380000.0; }
    @Override public boolean verificarDatos() { return nombre != null && !nombre.trim().isEmpty() && fechaPago != null; }
}
