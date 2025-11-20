/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.gymapp;
import GUI.GymAppGui;
import javax.swing.SwingUtilities;

/**
 *
 * @author camilocardonasuarez
 */
public class GymApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GymAppGui().setVisible(true));
    }
}

