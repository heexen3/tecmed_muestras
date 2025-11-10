/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Modelo;

/**
 *
 * @author Luis Echevarria
 */
public class Muestra {
    private Integer mu_id;
    private String mu_tipo;
    private String mu_estado;
    private Paciente paciente;

    public Muestra() {
    }

    public Muestra(String mu_tipo, Paciente paciente) {
        setMu_tipo(mu_tipo);
        setPaciente(paciente);
        this.mu_estado = "Recibida";
    }

    public Integer getMu_id() {
        return mu_id;
    }

    public void setMu_id(Integer mu_id) {
        if (mu_id == null) {
            throw new IllegalArgumentException("ID de muestra no puede ser nula.");
        }
        this.mu_id = mu_id;
    }

    public String getMu_tipo() {
        return mu_tipo;
    }

    public void setMu_tipo(String mu_tipo) {
        if (mu_tipo == null) {
            throw new IllegalArgumentException("La casilla Tipo no puede ser nula.");
        }
        if (mu_tipo.length() > 30) {
            throw new IllegalArgumentException("La casilla Tipo no puede superar 30 caracteres.");
        }
        this.mu_tipo = mu_tipo;
    }

    public String getMu_estado() {
        return mu_estado;
    }

    public void setMu_estado(String mu_estado) {
        if (mu_estado == null) {
            throw new IllegalArgumentException("La casilla Estado no puede ser nula.");
        }
        if (mu_estado.length() > 30) {
            throw new IllegalArgumentException("La casilla Estado no puede superar 30 caracteres.");
        }
        this.mu_estado = mu_estado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("La casilla Paciente no puede ser nula.");
        }
        this.paciente = paciente;
    }

    @Override
    public String toString() {
        return "Muestra{" + "mu_id=" + mu_id + ", mu_tipo=" + mu_tipo + ", mu_estado=" + mu_estado + ", paciente=" + paciente + '}';
    }

    
}
