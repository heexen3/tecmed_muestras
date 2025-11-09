/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.time.LocalDate;
        
/**
 *
 * @author Luis Echevarria
 */
public class PacienteDAO {
    private ConexionBD conexionBD;

    public PacienteDAO() {
    }
    
    public PacienteDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    public void guardarPaciente(Paciente paciente) throws SQLException {
        
        String sql = "INSERT INTO PACIENTE (pa_rut, pa_nombres, pa_apellidos, pa_nacimiento, pa_email, pa_telefono) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = conexionBD.conectar();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, paciente.getPa_rut());
            pstmt.setString(2, paciente.getPa_nombres());
            pstmt.setString(3, paciente.getPa_apellidos());
            pstmt.setDate(4, Date.valueOf(paciente.getPa_nacimiento())); // convierte LocalDate a Date
            pstmt.setString(5, paciente.getPa_email());
            pstmt.setString(6, paciente.getPa_telefono());
            
            pstmt.executeUpdate();
        } 
    }
    
    public ArrayList<Paciente> listarPacientes() throws SQLException {
        ArrayList<Paciente> lista = new ArrayList<>();
        
        String sql = "SELECT "
                    + "pa_rut, pa_nombres, pa_apellidos, pa_nacimiento, pa_email, pa_telefono "
                    + "FROM PACIENTE";
        
        try (Connection con = conexionBD.conectar();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) { // ejecuta el SELECT
            
            while (rs.next()) {
                // convertir fila actual en objeto Paciente
                Paciente p = new Paciente();
                
                p.setPa_rut(rs.getString("pa_rut"));
                p.setPa_nombres(rs.getString("pa_nombres"));
                p.setPa_apellidos(rs.getString("pa_apellidos"));
                p.setPa_nacimiento(rs.getDate("pa_nacimiento").toLocalDate());
                p.setPa_email(rs.getString("pa_email"));
                p.setPa_telefono(rs.getString("pa_telefono"));
                
                lista.add(p);
            }
        } 
        return lista;
    }
    
    public Paciente buscarPorRut(String rut) throws SQLException {
        Paciente p = null;
        String sql = "SELECT * FROM PACIENTE WHERE pa_rut = ?";
        
        try (Connection con = conexionBD.conectar();
            PreparedStatement pstmt = con.prepareStatement(sql))
        {
            pstmt.setString(1, rut);
            
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    p = new Paciente();
                    
                    p.setPa_rut(rs.getString("pa_rut"));
                    p.setPa_nombres(rs.getString("pa_nombres"));
                    p.setPa_apellidos(rs.getString("pa_apellidos"));
                    p.setPa_nacimiento(rs.getDate("pa_nacimiento").toLocalDate());
                    p.setPa_email(rs.getString("pa_email"));
                    p.setPa_telefono(rs.getString("pa_telefono"));
                    
                    return p;
                }
            }
        }
        return p;
    }
}
