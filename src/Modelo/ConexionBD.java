/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Luis Echevarria
 */
public class ConexionBD {
    private String rutaWallet;
    private String aliasServicio;
    private String user;
    private String password;

    public ConexionBD() {
    }

    public ConexionBD(String rutaWallet, String aliasServicio, String user, String password) {
        this.rutaWallet = rutaWallet;
        this.aliasServicio = aliasServicio;
        this.user = user;
        this.password = password;
    }
    
    public Connection conectar() throws SQLException {
    
        String WALLET_PASSWORD = this.password;
        
        
        System.setProperty("javax.net.ssl.trustStore", this.rutaWallet + "\\truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", WALLET_PASSWORD);
        System.setProperty("javax.net.ssl.keyStore", this.rutaWallet + "\\keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", WALLET_PASSWORD);

        System.setProperty("oracle.net.tns_admin", this.rutaWallet); 
        
        Properties propiedades = new Properties();
        propiedades.put("user", this.user);
        propiedades.put("password", this.password);

        
        propiedades.put("javax.net.ssl.keyStorePassword", WALLET_PASSWORD);
        propiedades.put("javax.net.ssl.trustStorePassword", WALLET_PASSWORD);

        
        String url = "jdbc:oracle:thin:@" + this.aliasServicio;
        
        
        Connection conexion = DriverManager.getConnection(url, propiedades);
        
        return conexion;
    }
    
}