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
    private static String MULTICAST_ADDRESS = "224.3.2.1";
	private static int PORT_MANAGE = 4323;
    private int PORT_RECEIVE = 4322;
	private int PORT_SEND = 4321;
	private static int name;
	
    public static void main(String[] args) {
		try {
			MulticastSocket socket = new MulticastSocket(PORT_MANAGE);
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);
			
			String request = "Que numero sou?";
			byte[] buffer = new byte[256];
			buffer = request.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT_MANAGE);
			socket.send(packet);
			
		}catch(Exception main){
			System.out.println("Erro a enviar socket manage: " + main);
		}
		Synch s = new Synch();
		try{
			Thread.sleep(1000);
		}catch(Exception main2){
			System.out.println("Erro na thread sleep: " + main2);
		}
		name = s.GetServerNumber();
        ServidorMulti server = new ServidorMulti();
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
		switch(processa.get(1)){
			case "registo":
				return protocolo;
				
			case "login":
				return protocolo;
			case "notifications":
				return protocolo;
			case "add_notifications":
				return protocolo;
			case "search":
				return protocolo;
			case "details":
				return protocolo;
			case "critic":
				return protocolo;
			case "privileges":
				return protocolo;
			case "share":
				return protocolo;
			case "getgeneros":
				return protocolo;
			case "addgenero":
				return protocolo;
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
	}
}

class Synch extends Thread{
	private int server = 1;
	private MulticastSocket socket;
	private DatagramPacket packet;
	private InetAddress group;
    private String MULTICAST_ADDRESS = "224.3.2.1";
	private int PORT_MANAGE = 4323;
	private boolean blocked = false;
	public Synch(){
		this.start();		
	}	
	public synchronized int GetServerNumber(){
		return server;		
	}	
	public void run(){
		try{
			socket = new MulticastSocket(PORT_MANAGE);
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);
			
			while(true){
				byte[] buffer = new byte[256];
				packet = new DatagramPacket(buffer, buffer.length, group, PORT_MANAGE);	
				socket.receive(packet);
				String resposta = new String(packet.getData(), 0, packet.getLength());
				if(resposta.compareTo("Que numero sou?")==0){
					blocked = true;
					String message = Integer.toString(server);
					buffer = message.getBytes();
					packet = new DatagramPacket(buffer, buffer.length, group, PORT_MANAGE);
					try{
						Thread.sleep(300);
					}catch(Exception ww){}
					socket.send(packet);
				}
				else if(!blocked){
					if(server == Integer.parseInt(resposta)) server = Integer.parseInt(resposta)+1;
				}
			}
			
		}catch(Exception c){
			System.out.println("Erro na thread de sincronizacao: " + c);
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

