/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Modelo;

import java.time.LocalDate;

/**
 *
 * @author Luis Echevarria
 */
public class Paciente {
    private String pa_rut;
    private String pa_nombres;
    private String pa_apellidos;
    private LocalDate pa_nacimiento;
    private String pa_email;
    private String pa_telefono;

    public Paciente() {
    }

    public Paciente(String pa_rut, String pa_nombres, String pa_apellidos, LocalDate pa_nacimiento, String pa_email, String pa_telefono) {
        setPa_rut(pa_rut);
        setPa_nombres(pa_nombres);
        setPa_apellidos(pa_apellidos);
        setPa_nacimiento(pa_nacimiento);
        setPa_email(pa_email);
        setPa_telefono(pa_telefono);
    }

    public String getPa_rut() {
        return pa_rut;
    }

    public void setPa_rut(String pa_rut) {
        if (pa_rut == null) {
            throw new IllegalArgumentException("RUT no puede ser nulo.");
        }
        if (pa_rut.length() > 13) {
            throw new IllegalArgumentException("RUT no puede exceder los 13 caracteres");
        }
        
        this.pa_rut = pa_rut;
    }

    public String getPa_nombres() {
        return pa_nombres;
    }

    public void setPa_nombres(String pa_nombres) {
        if (pa_nombres == null) {
            throw new IllegalArgumentException("La casilla Nombres no puede ser nula.");
        }
        if (pa_nombres.length() > 50) {
            throw new IllegalArgumentException("La casilla Nombres no puede superar 50 caracteres.");
        }
        this.pa_nombres = pa_nombres;
    }

    public String getPa_apellidos() {
        return pa_apellidos;
    }

    public void setPa_apellidos(String pa_apellidos) {
        if (pa_apellidos == null) {
            throw new IllegalArgumentException("La casilla Apellidos no puede ser nula.");
        }
        if (pa_apellidos.length() > 50) {
            throw new IllegalArgumentException("La casilla Apellidos no puede superar 50 caracteres.");
        }
        this.pa_apellidos = pa_apellidos;
    }

    public LocalDate getPa_nacimiento() {
        return pa_nacimiento;
    }

    public void setPa_nacimiento(LocalDate pa_nacimiento) {
        if (pa_nacimiento == null) {
            throw new IllegalArgumentException("Fecha de nacimiento no puede ser nula.");
        }
        
        this.pa_nacimiento = pa_nacimiento;
    }

    public String getPa_email() {
        return pa_email;
    }

    public void setPa_email(String pa_email) {
        if (pa_email == null) {
            throw new IllegalArgumentException("Email no puede ser nula.");
        }
        if (pa_email.length() > 50) {
            throw new IllegalArgumentException("Email no puede superar 50 caracteres.");
        }
        this.pa_email = pa_email;
    }

    public String getPa_telefono() {
        return pa_telefono;
    }

    public void setPa_telefono(String pa_telefono) {
        if (pa_telefono == null) {
            throw new IllegalArgumentException("Teléfono no puede ser nulo.");
        }
        if (pa_telefono.length() > 20) {
            throw new IllegalArgumentException("Teléfono no puede superar 20 caracteres.");
        }
        this.pa_telefono = pa_telefono;
    }

    @Override
    public String toString() {
        return "Paciente{" + "pa_rut=" + pa_rut + ", pa_nombres=" + pa_nombres + ", pa_apellidos=" + pa_apellidos + ", pa_nacimiento=" + pa_nacimiento + ", pa_email=" + pa_email + ", pa_telefono=" + pa_telefono + '}';
    }
    
    
}
