/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gym.modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioUsuarios {
    private final List<Usuario> usuarios = new ArrayList<>();
    private final File archivo;

    public RepositorioUsuarios(File archivo) {
        this.archivo = archivo;
        if (archivo != null && archivo.exists()) {
            cargarDesdeArchivo();
        }
    }

    public void agregar(Usuario u) {
        usuarios.add(u);
        guardarEnArchivo();
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    public List<Usuario> buscarPorNombre(String texto) {
        final String t = texto.toLowerCase();
        return usuarios.stream()
                .filter(u -> u.getNombre().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public void reemplazar(Usuario original, Usuario actualizado) {
        int idx = usuarios.indexOf(original);
        if (idx >= 0) {
            usuarios.set(idx, actualizado);
            guardarEnArchivo();
        }
    }

    public void persistirCambios() { guardarEnArchivo(); }

    private void cargarDesdeArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length < 3) continue;
                String tipo = p[0];
                String nombre = p[1];
                LocalDate fecha = LocalDate.parse(p[2]);

                Usuario u = null;
                switch (tipo) {
                    case "1": u = new Usuario1Mes(nombre, fecha); break;
                    case "3": u = new Usuario3Meses(nombre, fecha); break;
                    case "6": u = new Usuario6Meses(nombre, fecha); break;
                }
                if (u != null) usuarios.add(u);
            }
        } catch (Exception ex) {
            System.err.println("Error cargando CSV: " + ex.getMessage());
        }
    }

    private void guardarEnArchivo() {
        if (archivo == null) return;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Usuario u : usuarios) {
                int tipo = u.getMesesDuracion();
                bw.write(tipo + ";" + u.getNombre() + ";" + u.getFechaPago().toString());
                bw.newLine();
            }
        } catch (Exception ex) {
            System.err.println("Error guardando CSV: " + ex.getMessage());
        }
    }
}
