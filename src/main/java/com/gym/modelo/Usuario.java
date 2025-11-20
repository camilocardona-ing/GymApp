package com.gym.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Usuario {
    protected String nombre;
    protected LocalDate fechaPago;
    protected int mesesDuracion; 

    public Usuario(String nombre, LocalDate fechaPago, int mesesDuracion) {
        this.nombre = nombre;
        this.fechaPago = fechaPago;
        this.mesesDuracion = mesesDuracion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nuevoNombre) { this.nombre = nuevoNombre; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate nuevaFecha) { this.fechaPago = nuevaFecha; }

    public int getMesesDuracion() { return mesesDuracion; }

    public LocalDate getFechaVencimiento() { return fechaPago.plusMonths(mesesDuracion); }

    public boolean estaActivo(LocalDate hoy) { return !hoy.isAfter(getFechaVencimiento()); }

    public String getEstado(LocalDate hoy) { return estaActivo(hoy) ? "Activo" : "Vencido"; }

    public String getFechaPagoStr() { return fechaPago.format(DateTimeFormatter.ISO_LOCAL_DATE); }
    public String getFechaVencimientoStr() { return getFechaVencimiento().format(DateTimeFormatter.ISO_LOCAL_DATE); }

    public abstract double calcularPrecio();

    @Override
    public String toString() {
        return nombre + " | Plan " + mesesDuracion + " mes(es) | Pagó: " + getFechaPagoStr() +
               " | Vence: " + getFechaVencimientoStr() + " | Estado: " + getEstado(LocalDate.now());
    }
}
 