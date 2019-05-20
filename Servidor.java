/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.socketservidor2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Sistemas
 */
public class Servidor {
     private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream bufferEntrada = null;
    private DataOutputStream bufferSalida = null;
    Scanner scaner = new Scanner(System.in);
    final String COMANDO_SALIR = "salir()";
    
    public void conectar(int puerto) {
        try {
            serverSocket = new ServerSocket(puerto);
            
            System.out.println(
                    "Esperando conexión puerto: " + 
                            String.valueOf(puerto));
            
            socket = serverSocket.accept();
            
            System.out.println("Conexión establecida con: " + 
                    socket.getInetAddress().getHostName());
        } catch (Exception e) {
            System.out.println("Error al conectar: " + 
                    e.getMessage());
            System.exit(0);
        }
    }
    
    public void cerrarConexion() {
        try {
            bufferEntrada.close();
            bufferSalida.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar la conexión: " + 
                    e.getMessage());            
        } finally {
            System.out.println("Conexion finalizada.");
            System.exit(0);
        }
    }
    
    public void getDatos() {
        String texto = "";
        try {
            bufferEntrada = new DataInputStream(socket.getInputStream());
            do {                
                texto = (String) bufferEntrada.readUTF();
                System.out.print("<< Cliente >> " + texto);
                System.out.print("\n<< Servidor >>");                
            } while (!texto.equals(COMANDO_SALIR));
        } catch (Exception e) {
            cerrarConexion();
        }
    }
    
    public void sendDatos(String mensaje) {
        try {
            bufferSalida = new DataOutputStream(socket.getOutputStream());
            bufferSalida.writeUTF(mensaje);
            bufferSalida.flush();
        } catch (IOException e) {
            cerrarConexion();
            System.out.println("Error al enviar mensaje: " + e.getMessage());
        }
    }
    
    public void escribirMensaje() {
        while (true) {            
            System.out.print("<< Servidor >> ");
            sendDatos(scaner.nextLine());
        }
    }
    
    
    public void establecerConexion(final int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {                    
                    try {
                        conectar(puerto);
                        getDatos();
                    } finally {
                        cerrarConexion();
                    }
                }
            }
        });
        hilo.start();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        
        Scanner scan = new Scanner(System.in);
        
        System.out.print("Escribe el puerto [5555 por default]: ");
        String puerto = scan.nextLine();
        
        if (puerto.length() <= 0) {
            puerto = "5555";        
        }
        
        servidor.establecerConexion(Integer.parseInt(puerto));
        servidor.escribirMensaje();
    }
}
