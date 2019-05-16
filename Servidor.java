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
    private DataInputStream bufferEntrada;
    private DataOutputStream bufferSalida;
    
    Scanner scaner = new Scanner(System.in);
    
    public void conectar(int puerto) {
        try {
            serverSocket = new ServerSocket(puerto);
            
            System.out.println("Esperando conexión puerto: " + 
                    String.valueOf(puerto));
            
            socket = serverSocket.accept();
            
            System.out.println("Conexión establecida con: " + 
                    socket.getInetAddress().getHostName());
            
            
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
            System.exit(0);
        }
    }
    public void sendDatos(String mensaje){}
    
    public void escribirMensaje() {
        while(true) {
            System.out.print("<< Servidor >> ");
            sendDatos(scaner.nextLine());
        }
    }
    public void getDatos(){}
    public void cerrarConexion(){}
    public void establecerConexion(final int puerto){
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
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
    
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        
        Scanner scan = new Scanner(System.in);
        
        System.out.print("Escribe el pueto [5555 por default]");
        
        String puerto = scan.nextLine();
        
        if (puerto.length() <= 0) {
            puerto = "5555";
        }
        
        servidor.establecerConexion(Integer.parseInt(puerto));
        servidor.escribirMensaje();
        
        // github.com/ozwaldo
    }
}
