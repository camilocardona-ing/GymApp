/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.gym.modelo;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import org.junit.jupiter.api.io.TempDir;

public class RepositorioUsuariosTest {
    
    public RepositorioUsuariosTest() {
    }
    @TempDir
    File tempDir;

    @Test
    void agregarYListar_debeGuardarUsuarioEnMemoriaYCSV() {
        File archivo = new File(tempDir, "usuarios_test.csv");
        RepositorioUsuarios repo = new RepositorioUsuarios(archivo);

        LocalDate fecha = LocalDate.of(2025, 1, 1);
        Usuario u = new Usuario3Meses("Camilo", fecha);

        repo.agregar(u);

        List<Usuario> lista = repo.listarTodos();
        assertEquals(1, lista.size());
        Usuario guardado = lista.get(0);

        assertEquals("Camilo", guardado.getNombre());
        assertEquals(3, guardado.getMesesDuracion());
        assertEquals(fecha, guardado.getFechaPago());

        // El archivo debe existir y tener contenido
        assertTrue(archivo.exists());
        assertTrue(archivo.length() > 0);
    }

    @Test
    void cargarDesdeArchivo_debeReconstruirUsuarios() throws Exception {
        File archivo = new File(tempDir, "usuarios_test2.csv");

        // Escribimos un CSV manualmente
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write("1;Camilo;2025-01-10\n");
            fw.write("6;Juan;2025-02-05\n");
        }

        // Repositorio debe leer ese archivo
        RepositorioUsuarios repo = new RepositorioUsuarios(archivo);
        List<Usuario> lista = repo.listarTodos();

        assertEquals(2, lista.size());
        assertEquals("Camilo", lista.get(0).getNombre());
        assertEquals(1, lista.get(0).getMesesDuracion());
        assertEquals(LocalDate.of(2025,1,10), lista.get(0).getFechaPago());

        assertEquals("Juan", lista.get(1).getNombre());
        assertEquals(6, lista.get(1).getMesesDuracion());
    }

    @Test
    void reemplazar_debeActualizarElUsuario() {
        File archivo = new File(tempDir, "usuarios_test3.csv");
        RepositorioUsuarios repo = new RepositorioUsuarios(archivo);

        LocalDate fecha = LocalDate.of(2025, 3, 1);
        Usuario original = new Usuario1Mes("Camilo", fecha);
        repo.agregar(original);

        Usuario actualizado = new Usuario6Meses("Camilo", fecha);
        repo.reemplazar(original, actualizado);

        List<Usuario> lista = repo.listarTodos();
        assertEquals(1, lista.size());
        Usuario u = lista.get(0);

        assertEquals(6, u.getMesesDuracion());
        assertTrue(u instanceof Usuario6Meses);
    }    
}
