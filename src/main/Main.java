/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package main;

import Controlador.ControladorApp;
import Modelo.ConexionBD;
import Modelo.PacienteDAO;
import Modelo.MuestraDAO;
import Vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        
        final String RUTA_WALLET = "C:\\oracle_wallet\\Wallet_BDTecMed"; // Ej: "C:\\oracle_wallet\\"
        final String ALIAS_SERVICIO = "bdtecmed_low";
        final String USER = "ADMIN";
        final String PASSWORD = "Lightyagami-1";

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró la clase del driver Oracle.");
            e.printStackTrace();
            return; // Terminar la aplicación si falla
        }
        
        // ====================== ENSAMBLAJE DEL MODELO ========================
        
        // Herramienta de Conexión
        ConexionBD conexionBD = new ConexionBD(RUTA_WALLET, ALIAS_SERVICIO, USER, PASSWORD);
        
        // DAO Paciente (solo necesita la Conexión)
        PacienteDAO pacienteDAO = new PacienteDAO(conexionBD);
        
        // DAO Muestra (necesita la Conexión Y el PacienteDAO)
        MuestraDAO muestraDAO = new MuestraDAO(conexionBD, pacienteDAO);
        
        // ==================== ENSAMBLAJE VISTA - CONTROLADOR =================
        
        // GUI
        VentanaPrincipal vista = new VentanaPrincipal();
        
        // El Controlador
        ControladorApp controlador = new ControladorApp(vista, pacienteDAO, muestraDAO);
        
    }
}
