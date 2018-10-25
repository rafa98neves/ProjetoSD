package com.company;

import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;

public class ServidorMulti extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT_RECEIVE = 4322;
	private int PORT_SEND = 4321;
	private static int name;
	//private String con = "jdbc:sqlserver://ASUSPEDRO;databaseName=SD_DB;integratedSecurity=true;";
	//private String con = "Data Source=(LocalDB)\\MSSQLLocalDB;AttachDbFilename=D:\\Pedro\\GitHub\\ProjetoSD\\SD_DB.mdf;Integrated Security=True;Connect Timeout=30";
	//String url ="jdbc:sqlserver://PC01\inst01;databaseName=DB01;integratedSecurity=true";

    public static void main(String[] args) {
        ServidorMulti server = new ServidorMulti();
		//Synch s = new Synch();
		//name = s.GetServerNumber();
        server.start();
    }
	
    public ServidorMulti() {
        super("Server online");
    }
	
    public void run() {
        MulticastSocket socket = null;
        long counter = 0;

        System.out.println(this.getName() + " " + name + " running...");
        try {
            socket = new MulticastSocket(PORT_RECEIVE);
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);
            while (true) {
				byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT_RECEIVE);			
				System.out.println("Bloqueado");
				socket.receive(packet);
			    System.out.println("Desbloqueado");
				
				String message = ManageRequest(packet);
				
				buffer = message.getBytes();
				
				packet = new DatagramPacket(buffer, buffer.length, group, PORT_SEND);
				socket.send(packet);
				System.out.println("Sent!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
	
	public String ManageRequest(DatagramPacket packet){
		Connection conn = null;
		String strCmd;
		String protocolo = new String(packet.getData(), 0, packet.getLength());
		String[] processar = protocolo.split(Pattern.quote(" ; "));
		ArrayList<String> processa = new ArrayList<String>();
		String[] aux;
		for(String s : processar){
                    aux = s.split(Pattern.quote(" | "));
                    processa.add(aux[0]);
                    processa.add(aux[1]);
		}

		Connection conn = null;

		switch(processa.get(1)){
			case "registo":
				return protocolo;
				break;
				
			case "login":
				return protocolo;
				
				break;
			case "notifications":
				return protocolo;
				
				break;
			case "add_notifications":
				return protocolo;
				
				break;
				
			case "search":
				return protocolo;
				break;
				
			case "details":
				
				return protocolo;
				break;
				
			case "critic":
				
				return protocolo;
				break;
				
			case "privileges":
				
				return protocolo;
				break;
				
			case "share":
				
				return protocolo;
				break;
				
			case "getgeneros":
				
				return protocolo;
				break;
			
			case "addgenero":
				
				return protocolo;
				break;
				
			case "GetAddress":
				protocolo = "type | GetAddress ; " + MULTICAST_ADDRESS + " | 6000";
				if(processa.get(3).compareTo("download")==0){
					EnviaMusica e = new EnviaMusica();
				}
				else{
					RecebeMusica r = new RecebeMusica();
				}
				return protocolo;
			default: 
				protocolo = "type | error ; function | " + processa.get(1);
				return protocolo;	
		}
		
		return "type | error ; function | " + processa.get(1);
	}
}

class Synch extends Thread{
	private int server = 0;
	private MulticastSocket socket;
	private DatagramPacket packet;
    private String MULTICAST_ADDRESS = "224.3.2.1";
	private int PORT_MANAGE = 4323;
	private String i = "1";
	
	public Synch(){
		try {
			
			socket = new MulticastSocket(PORT_MANAGE);
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);
			byte[] buffer = new byte[256];
			buffer = i.getBytes();
			packet = new DatagramPacket(buffer, buffer.length, group, PORT_MANAGE);
			socket.send(packet);
			System.out.println("Enviado");
			int p = 0;
			for(int j = 0; j<3; j++){
				try{
					Thread.sleep(500);
				}catch(Exception asdas){
					System.out.println("Erroooo : " + asdas);
				}
				
				packet = new DatagramPacket(buffer, buffer.length, group, PORT_MANAGE);	
				socket.receive(packet);
				if(j==1){
					server = p;
					p++;
					buffer = Integer.toString(p).getBytes();
					packet = new DatagramPacket(buffer, buffer.length, group, PORT_MANAGE);
					socket.send(packet);
					break;
				}
				String pr = new String(packet.getData(), 0, packet.getLength());
				p = Integer.parseInt(pr);
				if(p != Integer.parseInt(i)) break;
			}
			System.out.println("recebido");
					
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
		this.start();
	}
	
	public synchronized int GetServerNumber(){
		while(server==0){
			try{
				Thread.sleep(500);
			}catch(Exception c){
				System.out.println("Erro: " +c);
			}
		}
		return server;		
	}
	
	public void run(){
		while(true){
		}
	}
}


class RecebeMusica extends Thread{
	DataInputStream in;
    Socket clientSocket;
	int PORT_USER = 6000;
    public RecebeMusica() {
        try{
            ServerSocket listenSocket = new ServerSocket(PORT_USER);
			clientSocket = listenSocket.accept();
            in = new DataInputStream(clientSocket.getInputStream());
            this.start();
        }catch(IOException e){System.out.println("Connection:" + e.getMessage());}
    }
    //=============================
    public void run(){
        String resposta;
        try{
			int musica = in.read();
			//Transformar em musica e guardar
			//Acrescentar na BD musica nao partilhada ao utilizador
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);}
    }
}

class EnviaMusica extends Thread{
	int PORT_USER = 6000;
	DataOutputStream out;
    Socket clientSocket;
	
    public EnviaMusica() {
        try{
            ServerSocket listenSocket = new ServerSocket(PORT_USER);
			clientSocket = listenSocket.accept();
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start(); 
        }catch(IOException e){System.out.println("Connection:" + e.getMessage());}
    }
    //=============================
    public void run(){
        String resposta;
        /*try{
			out.write(cenas);
			//byte [] musicbyte  = new byte [(int)musica.length()];
			//Transformar em musica e enviar
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);}*/
    }
}

