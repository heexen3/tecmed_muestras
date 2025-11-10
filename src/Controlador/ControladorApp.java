/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Controlador;

import Modelo.*;
import Vista.VentanaPrincipal;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel; // Necesitarás este import
import java.util.ArrayList;                 // Necesitarás este import
import Modelo.Muestra;

/*
 *
 * @author Luis Echevarria
*/

public class ControladorApp implements ActionListener {
    private VentanaPrincipal vista;
    private PacienteDAO pacienteDAO;
    private MuestraDAO muestraDAO;

    public ControladorApp() {
    }

    public ControladorApp(VentanaPrincipal vista, PacienteDAO pacienteDAO, MuestraDAO muestraDAO) {
        this.vista = vista;
        this.pacienteDAO = pacienteDAO;
        this.muestraDAO = muestraDAO;
        
        iniciarVista();
    }
    
    private void iniciarVista() {
        // Botón
        vista.getBtnGuardar().addActionListener(this);
        vista.getBtnCompletar().addActionListener(this);
        cargarTablaMuestras();
        
        // Configuraciones de la ventana
        vista.setTitle("Sistema de Gestión de Muestras (MVP)");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnGuardar()) {
            guardarMuestraYPaciente();
        } else if (e.getSource() == vista.getBtnCompletar()) {
            marcarMuestraCompletada();
        }
    }
    
    public void guardarMuestraYPaciente() {
        String rutIngresado = vista.getTxtRut().getText();
            
        Muestra nuevaMuestra = null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        try {
            // Convertir el campo de fecha a LocalDate
            LocalDate fechaNacimiento = LocalDate.parse(vista.getTxtNacimiento().getText(), formatter);
            Paciente paciente = pacienteDAO.buscarPorRut(rutIngresado);    
            
            if (paciente == null) {
                paciente = new Paciente(
                    vista.getTxtRut().getText(),
                    vista.getTxtNombres().getText(),
                    vista.getTxtApellidos().getText(),
                    fechaNacimiento,
                    vista.getTxtEmail().getText(),
                    vista.getTxtTelefono().getText());
                pacienteDAO.guardarPaciente(paciente);
            }
            
            nuevaMuestra = new Muestra(
                    (String) vista.getCmbTipo().getSelectedItem(),
                    paciente // Inyección del objeto Paciente
            );
             
            muestraDAO.guardarMuestra(nuevaMuestra);
            
            // Feedback
            JOptionPane.showMessageDialog(vista,
                    "Muestra guardada con éxito.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);

            cargarTablaMuestras();
            
            } catch (DateTimeParseException ex) {
            // error de UX: El usuario no puso la fecha en formato DD-MM-YYYY
            JOptionPane.showMessageDialog(vista, 
                "Error de formato en la fecha. Use DD-MM-YYYY.", 
                "Error de Entrada", JOptionPane.ERROR_MESSAGE);

            } catch (NumberFormatException ex) {
                // error de UX: El usuario escribió texto en el campo ID (que es un Integer)
                JOptionPane.showMessageDialog(vista, 
                    "Error de formato: El ID de Muestra debe ser un número entero.", 
                    "Error de Entrada", JOptionPane.ERROR_MESSAGE);

            } catch (IllegalArgumentException ex) {
                // error de NEGOCIO: Vienen del Modelo (POJO Paciente o Muestra)
                JOptionPane.showMessageDialog(vista, 
                    "Error de Negocio: " + ex.getMessage(), 
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);

            } catch (SQLException ex) {
                // error de PERSISTENCIA: Vienen de la BD (DAO)
                JOptionPane.showMessageDialog(vista, 
                    "Error de Base de Datos: Verifique si el ID de Muestra o RUT ya existen.\n" + ex.getMessage(), 
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
    }
    
    private void cargarTablaMuestras() {
    DefaultTableModel modelo = new DefaultTableModel();
    
    String[] cabeceras = {"ID Muestra", "Tipo", "Estado", "RUT Paciente", "Nombre Paciente"};
    modelo.setColumnIdentifiers(cabeceras);
    
    try {
        ArrayList<Muestra> listaMuestras = muestraDAO.listarMuestras();
        
        
        for (Muestra m : listaMuestras) {
            // El array de objetos Object[] se convierte en una fila de la tabla
            Object[] fila = new Object[5];
            
            fila[0] = m.getMu_id();
            fila[1] = m.getMu_tipo();
            fila[2] = m.getMu_estado();
            // Accedemos a la relación de objeto (el POO que diseñamos)
            fila[3] = m.getPaciente().getPa_rut();
            fila[4] = m.getPaciente().getPa_nombres() + " " + m.getPaciente().getPa_apellidos();
            
            modelo.addRow(fila);
        }
        
        // Asignar el modelo a la JTable de la Vista
        vista.getTblMuestras().setModel(modelo);
        
    } catch (SQLException e) {
        // Si falla la conexión o el SELECT
        JOptionPane.showMessageDialog(vista, 
            "Error al cargar datos de la BD: " + e.getMessage(), 
            "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void marcarMuestraCompletada() {
        int filaSeleccionada = vista.getTblMuestras().getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, 
                    "Debe seleccionar una muestra de la tabla.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Integer muId = (Integer)(vista.getTblMuestras().getValueAt(filaSeleccionada, 0));
            
            muestraDAO.actualizarEstado(muId, "Completada");
            
            JOptionPane.showMessageDialog(vista, 
                    "Muestra ID " + muId + " actualizada a 'Completada'.",
                    "Actualización Exitosa!", JOptionPane.INFORMATION_MESSAGE);
            
            cargarTablaMuestras();
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista,
                    "Error de BD al actualizar: " + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista,
                    "Error al procesar a selección: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}