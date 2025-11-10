/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Modelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author Luis Echevarria
 */
public class MuestraDAO {
    private ConexionBD conexionBD;
    private PacienteDAO pacienteDAO;

    public MuestraDAO() {
    }

    public MuestraDAO(ConexionBD conexionBD, PacienteDAO pacienteDAO) {
        this.conexionBD = conexionBD;
        this.pacienteDAO = pacienteDAO;
    }
    
    public void guardarMuestra(Muestra muestra) throws SQLException {
        
    String sql = "INSERT INTO MUESTRA (mu_id, mu_tipo, mu_estado, PACIENTE_pa_rut) "
                + "VALUES (muestra_id_seq.NEXTVAL, ?, ?, ?)";
    try (Connection con = conexionBD.conectar();
        PreparedStatement pstmt = con.prepareStatement(sql)) {
        
        pstmt.setString(1, muestra.getMu_tipo());
        pstmt.setString(2, muestra.getMu_estado());
        pstmt.setString(3, muestra.getPaciente().getPa_rut()); 
        
        pstmt.executeUpdate(); 

    }
    }
    
    public ArrayList<Muestra> listarMuestras() throws SQLException {
        ArrayList<Muestra> lista = new ArrayList<>();

        String sql = "SELECT "
                    + "mu_id, mu_tipo, mu_estado, PACIENTE_pa_rut "
                    + "FROM MUESTRA";

        try (Connection con = conexionBD.conectar();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) { // ejecuta el SELECT
            while (rs.next()) {
                // convertir fila actual en objeto Paciente
                Muestra m = new Muestra();

                m.setMu_id(rs.getInt("mu_id"));
                m.setMu_tipo(rs.getString("mu_tipo"));
                m.setMu_estado(rs.getString("mu_estado"));
                
                // crear un paciente basado en los datos obtenidos
                // con busarPorRut() de PacienteDAO
                Paciente p = pacienteDAO.buscarPorRut(rs.getString("PACIENTE_pa_rut"));
                m.setPaciente(p);

                lista.add(m);
            }
        }
        return lista;
        }
    
    public void actualizarEstado(Integer mu_id, String estado) throws SQLException {
        
        String sql = "UPDATE MUESTRA "
                    + "SET mu_estado = ? " // nuevo estado
                    + "WHERE mu_id = ?";   // la muestra a cambiar
        
        try (Connection con = conexionBD.conectar();
            PreparedStatement pstmt = con.prepareStatement(sql);) 
        {
           pstmt.setString(1, estado);
           pstmt.setInt(2, mu_id);
           
           pstmt.executeUpdate();
        }
    }
}
