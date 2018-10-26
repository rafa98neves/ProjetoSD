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
import java.sql.*;

public class ServidorMulti extends Thread {
	private static String MULTICAST_ADDRESS = "224.3.2.1";
	private static int PORT_MANAGE = 4323;
	private int PORT_RECEIVE = 4322;
	private static int name;
	//private String con = "jdbc:sqlserver://ASUSPEDRO;databaseName=SD_DB;integratedSecurity=true;";
	//private String con = "Data Source=(LocalDB)\\MSSQLLocalDB;AttachDbFilename=D:\\Pedro\\GitHub\\ProjetoSD\\SD_DB.mdf;Integrated Security=True;Connect Timeout=30";
	//String url ="jdbc:sqlserver://PC01\inst01;databaseName=DB01;integratedSecurity=true";

    public static void main(String[] args) {
		try {
			MulticastSocket socket = new MulticastSocket(PORT_MANAGE);
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);

			String request = "Que numero sou?";
			byte[] buffer;
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
        try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

        server.start();
    }
	
    public ServidorMulti() {
        super("Server online");
    }
	
    public void run() {
        MulticastSocket socket = null;
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
				ManageNewRequest manage = new ManageNewRequest(name, socket, group, packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
class ManageNewRequest extends Thread{
	private String con = "jdbc:sqlserver://pedro-sd.database.windows.net:1433;database=SQDB;user=sddb@pedro-sd;password=sd_db123!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30";

	private static String MULTICAST_ADDRESS = "224.3.2.1";
	private int PORT_SEND = 4321;
	private DatagramPacket packet;
	private InetAddress group;
	private MulticastSocket socket;
	private int nome;

	public ManageNewRequest(int nome,MulticastSocket socket,InetAddress group, DatagramPacket packet){
		this.nome = nome;
		this.socket = socket;
		this.group = group;
		this.packet = packet;
		this.start();
	}

	public void run() {
		String message = ManageRequest(packet);

		byte[] buffer = new byte[256];
		buffer = message.getBytes();
		try {
			packet = new DatagramPacket(buffer, buffer.length, group, PORT_SEND);
			socket.send(packet);
			System.out.println("Sent!");

		}catch (Exception e){
			System.out.println("Erro a enviar pacote: " + e);
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
				try {
					String commandText = "{call dbo.Registo(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(5)));
					stmt.setObject(2, new String(processa.get(7)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					System.out.printf("\n OLA: " + stmt.getInt(3));
					System.out.printf("\n ADEUS: " + stmt.getString(4));
					if(stmt.getInt(3) >= 0) protocolo = "type | registo ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | true";
					else protocolo = "type | registo ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | false";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "login":
				try {
					String commandText = "{call dbo.Login(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(5)));
					stmt.setObject(2, new String(processa.get(7)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(3) >= 0) protocolo = "type | login ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | true";
					else protocolo = "type | login ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | false";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
			case "notifications":
				try {
					String commandText = "{call dbo.Notifications(?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.registerOutParameter(2, Types.INTEGER);
					stmt.registerOutParameter(3, Types.VARCHAR); //VARRAY ?
					stmt.execute();
					if(stmt.getInt(2) >= 0 ) protocolo = "type | notifications ; " + processa.get(2) +" | " + processa.get(3) + " ; notification_1 | "+ stmt.getString(3) +" ; notification_2 | " + "?" +" ; "; // (...)" ;
					else protocolo = "type | notifications ; " + processa.get(2) +" | " + processa.get(3) + " ; notification_1 | none";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
			case "add_notifications":
				try {
					String commandText = "{call dbo.AddNoti(?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | confirmation ; " + processa.get(2) +" | " + processa.get(3) + " ; notification | add";
					else protocolo = "type | confirmation ; " + processa.get(2) +" | " + processa.get(3) + " ; notification | error";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "search":
				try {
					String commandText = "{call dbo.Search(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR); //VARRAY ?
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | search ; " + processa.get(2) +" | " + processa.get(3) + " ; possibility_1 | "+ stmt.getString(4) +" ; possibility_2 | " + "?" +" ; "; // (...)" ;
					else protocolo = "type | search ; " + processa.get(2) +" | " + processa.get(3) + " ; possibility_1 | none";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "details":
				try {
					String commandText = "{call dbo.Details(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR); //VARRAY ?
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | details ; " + processa.get(2) +" | " + processa.get(3) + " ; " + stmt.getString(4) + " | " + stmt.getString(5); // (...)" ;
					else protocolo = "type | search ; " + processa.get(2) +" | " + processa.get(3) + " ; details_1 | none";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "critic":
				try {
					String commandText = "{call dbo.WriteCritic(?,?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.setObject(3, new String(processa.get(7)));
					stmt.registerOutParameter(4, Types.INTEGER);
					stmt.registerOutParameter(5, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(4) >= 0 ) protocolo = "type | critic ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | escrito";
					else protocolo = "type | critic ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | negado";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "privileges":
				try {
					String commandText = "{call dbo.Privileges(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | privileges ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | done";
					else protocolo = "type | privileges ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | negado";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "share": //AINDA NAO SEI BEM, O QUE ESTA EM BAIXO ESTA MAL
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.Share @User, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.Share(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | privileges ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | done";
					else protocolo = "type | privileges ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | negado";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "getgeneros":
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.Generos @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.Generos(?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.registerOutParameter(1, Types.INTEGER);
					stmt.registerOutParameter(2, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(1) >= 0 ) protocolo = "type | getgeneros ; " + processa.get(2) +" | " + processa.get(3) + " ; " + stmt.getString(2) + " | " + stmt.getString(2); //(....)
					else protocolo = "type | error ; " + processa.get(2) +" | " + processa.get(3) + " ; function | " + processa.get(1);
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "addgenero":
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.AddGenero @Genero, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.Generos(?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.registerOutParameter(2, Types.INTEGER);
					stmt.registerOutParameter(3, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(2) >= 0 ) protocolo = "type | addgenero; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | true";
					else protocolo = "type | addgenero; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | false";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
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
				protocolo = "type | error ; " + processa.get(2) +" | " + processa.get(3) + " ; function | " + processa.get(1);
				return protocolo;
		}

		return "type | error ; " + processa.get(2) +" | " + processa.get(3) + " ; function | " + processa.get(1);
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

