package com.gym.modelo;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Usuario1MesTest {  @Test
    void usuario1Mes_debeCalcularPrecioYVencimientoCorrecto() {
        LocalDate fechaPago = LocalDate.of(2025, 1, 10);
        Usuario u = new Usuario1Mes("Camilo", fechaPago);

        assertEquals(1, u.getMesesDuracion());
        assertEquals(80000.0, u.calcularPrecio(), 0.0001);

        LocalDate esperado = fechaPago.plusMonths(1);
        assertEquals(esperado, u.getFechaVencimiento());

        
        assertEquals("Activo", u.getEstado(esperado));
        assertEquals("Activo", u.getEstado(esperado.minusDays(1)));

        
        assertEquals("Vencido", u.getEstado(esperado.plusDays(1)));
    }
}    
