package com.gym.modelo;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
  

public class Usuario6MesesTest {   @Test
    void usuario6Meses_debeCalcularPrecioYVencimientoCorrecto() {
        LocalDate fechaPago = LocalDate.of(2025, 3, 15);
        Usuario u = new Usuario6Meses("Felipe", fechaPago);

        assertEquals(6, u.getMesesDuracion());
        assertEquals(380000.0, u.calcularPrecio(), 0.0001);

        LocalDate esperado = fechaPago.plusMonths(6);
        assertEquals(esperado, u.getFechaVencimiento());
    }
}
   