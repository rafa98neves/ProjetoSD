import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;
import java.sql.*;

public class ServidorMulti extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT_RECEIVE = 4322;
	private int PORT_SEND = 4321;
	private static int name;
	private String con = "jdbc:sqlserver://pedro-sd.database.windows.net:1433;database=SQDB;user=sddb@pedro-sd;password=sd_db123!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30";
	
    public static void main(String[] args) {
        ServidorMulti server = new ServidorMulti();
		//Synch s = new Synch();
		//name = s.GetServerNumber();
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
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.Registo @User, @Password, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.Registo(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					System.out.printf("\n OLA: " + stmt.getInt(3));
					System.out.printf("\n ADEUS: " + stmt.getString(4));
					if(stmt.getInt(3) >= 0) protocolo = "type | registo ; confirmation | false";
					else protocolo = "type | registo ; confirmation | true";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
				
			case "login":
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.Login @User, @Password, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.Login(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(3) >= 0) protocolo = "type | login ; confirmation | true";
					else protocolo = "type | login ; confirmation | false";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
			case "notifications":
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.Notifications @User, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.Notifications(?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.registerOutParameter(2, Types.INTEGER);
					stmt.registerOutParameter(3, Types.VARCHAR); //VARRAY ?
					stmt.execute();
					if(stmt.getInt(2) >= 0 ) protocolo = "type | notifications ; notification_1 | "+ stmt.getString(3) +" ; notification_2 | " + "?" +" ; "; // (...)" ;
					else protocolo = "type | notifications ; notification_1 | none";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
			case "add_notifications":
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.AddNoti @User, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.AddNoti(?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | confirmation ; notification | add";
					else protocolo = "type | confirmation ; notification | error";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
				
			case "search":
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.Search @Nome, @From, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.Search(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR); //VARRAY ?
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | search ; possibility_1 | "+ stmt.getString(4) +" ; possibility_2 | " + "?" +" ; "; // (...)" ;
					else protocolo = "type | search ; possibility_1 | none";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
				
			case "details":
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.Details @Nome, @From, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.Details(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR); //VARRAY ?
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | details ; " + stmt.getString(4) + " | " + stmt.getString(5); // (...)" ;
					else protocolo = "type | search ; details_1 | none";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
				
			case "critic":
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.WriteCritic @User, @Album, @Write, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
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
					if(stmt.getInt(4) >= 0 ) protocolo = "type | critic ; confirmation | escrito";
					else protocolo = "type | critic ; confirmation | negado";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
				
			case "privileges":
				strCmd = "DECLARE @Erro int; " +
						"DECLARE @Description VARCHAR(1000);" +
						"EXECUTE dbo.Privileges @User, @Editor, @Erro OUTPUT, @Description OUTPUT;" +
						"SELECT @Erro erro, @Description description;";
				try {
					String commandText = "{call dbo.Privileges(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | privileges ; confirmation | done";
					else protocolo = "type | privileges ; confirmation | negado";
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
					String commandText = "{call dbo.Privileges(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.setObject(2, new String(processa.get(5)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(3) >= 0 ) protocolo = "type | privileges ; confirmation | done";
					else protocolo = "type | privileges ; confirmation | negado";
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
					if(stmt.getInt(1) >= 0 ) protocolo = stmt.getString(2) + " | " + stmt.getString(3); //(....)
					else protocolo = "type | error ; function | " + processa.get(1);
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
					if(stmt.getInt(2) >= 0 ) protocolo = "type | addgenero; confirmation | true";
					else protocolo = "type | addgenero; confirmation | false";
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

