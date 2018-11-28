package com.company;

import javax.jws.soap.SOAPBinding;
import javax.xml.transform.Result;
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

/**
 * Description: Classe onde está localizada a configuração de cada Multicast
 *
 * @param
 * @return
 */

public class ServidorMulti extends Thread {
	private static String MULTICAST_ADDRESS = "224.3.2.1";
	private static int PORT_MANAGE = 4323;
	private static int  PORT_SEND = 4321;
	private int PORT_RECEIVE = 4322;
	public static ServidorMulti server;
	private static int name;
	private static int InCharge = 1;
	private static int onlineServer = 0;
	public static String[] historico = new String[1000];
	public static int counting = 0;

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

		server = new ServidorMulti();
        try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

        server.start();
		int c = 0;
		int previous = 0;
        while(true){
        	try {
				Thread.sleep(5000);
				onlineServer = s.GetMaxServers();
				if(c == 0) previous = onlineServer;

				if(onlineServer == previous) c++;
				else c=0;

				if(c == 10){
					CleanHist();
				}
			}catch (Exception e){
				System.out.println("erro: " + e);
			}
		}

    }
	
    public ServidorMulti() {
        super("Server online");
    }

    public synchronized void addHist(String protocolo_before, String protocolo_new){
    	historico[counting] = protocolo_before;
    	counting++;
    	historico[counting] = protocolo_new;
    	counting++;
	}
	public static synchronized void CleanHist(){
    	historico = new String[1000];
    	counting = 0;
	}

    public void run() {
        MulticastSocket socket = null;
        String last = " ";
        System.out.println(this.getName() + " " + name + " running...");
        try {
            socket = new MulticastSocket(PORT_RECEIVE);
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);
            while (true) {
				byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT_RECEIVE);
				socket.receive(packet);

				String Check = new String(packet.getData(), 0, packet.getLength());
				if(Check.compareTo("TimeOutReached") == 0){
					System.out.println("AVISO: O Servidor " + InCharge + " está inativo!");
					if(InCharge < onlineServer){
						InCharge++;
						if(InCharge == name){
							System.out.println("Informacao: Este servidor e agora responsavel pelo envio de respostas");
							for(int i = 0; i<counting; i+=2) {
								if (historico[i].compareTo(last) == 0) {
									int j = i + 1;
									buffer = historico[j].getBytes();
									try {
										packet = new DatagramPacket(buffer, buffer.length, group, PORT_SEND);
										socket.send(packet);
									} catch (Exception aaa) {
										System.out.println("Erro: " + aaa);
									}
								}
							}
						}
					}
					else{
						InCharge--;
						if(InCharge == name){
							System.out.println("Informacao: Este servidor e agora responsavel pelo envio de respostas");
							for(int i = 0; i<counting; i+=2) {
								if (historico[i].compareTo(last) == 0) {
									int j = i + 1;
									buffer = historico[j].getBytes();
									try {
										packet = new DatagramPacket(buffer, buffer.length, group, PORT_SEND);
										socket.send(packet);
									} catch (Exception aaa) {
										System.out.println("Erro: " + aaa);
									}
								}
							}
						}
					}
				}
				else{
					last =  new String(packet.getData(), 0, packet.getLength());
					ManageNewRequest manage = new ManageNewRequest(name, socket, group, packet, InCharge);
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

/**
 * Description: Classe onde é tratada a configuração da ligação à base de dados e onde é efectuada os pedidos de
 * informação à mesma
 *
 * @param
 * @return
 */

class ManageNewRequest extends Thread{
	private String con = "jdbc:sqlserver://pedro-sd.database.windows.net:1433;database=SQDB;user=sddb@pedro-sd;password=sd_db123!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30";

	private static String MULTICAST_ADDRESS = "224.3.2.1";
	private int PORT_SEND = 4321;
	private DatagramPacket packet;
	private InetAddress group;
	private MulticastSocket socket;
	private int nome,InCharge;

	public ManageNewRequest(int nome,MulticastSocket socket,InetAddress group, DatagramPacket packet, int InCharge){
		this.nome = nome;
		this.socket = socket;
		this.group = group;
		this.packet = packet;
		this.InCharge = InCharge;
		this.run();
	}

	public void run(){
		String recived = new String(packet.getData(), 0, packet.getLength());
		String	message = ManageRequest(packet);

		if(nome == InCharge) {
			try {
				byte[] buffer = new byte[256];
				buffer = message.getBytes();
				packet = new DatagramPacket(buffer, buffer.length, group, PORT_SEND);
				socket.send(packet);
			} catch (Exception e) {
				System.out.println("Erro a enviar pacote: " + e);
			}
		}
		else{
			ServidorMulti.server.addHist(recived,message);
		}
	}

	/**
	 * Description: Esta função tem como objetivo a ligação da aplicação à base de dados SQL Server.
	 * É esta que entra em contacto direto com os scripts presentes na base de dados e que envia os argumentos
	 * necessários para que esta possa dar a devida informação que o User se encontra à espera
	 *
	 * @param pacote com a informação a tratar pela base de dados
	 * @return String com a respetiva resposta da base de dados
	 */

	public String ManageRequest(DatagramPacket packet){
		Connection conn = null;
		String strCmd;
		String protocolo = new String(packet.getData(), 0, packet.getLength());
		System.out.println(protocolo);
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
					String commandText = "{call dbo.Registo(?,?,?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(5)));
					stmt.setObject(2, new String(processa.get(7)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.registerOutParameter(5, Types.INTEGER);
					stmt.registerOutParameter(6, Types.BIT);
					stmt.execute();
					if(stmt.getInt(3) >= 0) protocolo = "type | registo ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | true ; id | " + stmt.getInt(5) + " ; editor | " + stmt.getBoolean(6);
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
					String commandText = "{call dbo.Login(?,?,?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(5)));
					stmt.setObject(2, new String(processa.get(7)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.registerOutParameter(5, Types.INTEGER);
					stmt.registerOutParameter(6, Types.BIT);
					stmt.execute();
					if(stmt.getInt(3) >= 0) protocolo = "type | login ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | true ; id | " + stmt.getInt(5) + " ; editor | " + stmt.getBoolean(6);
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
					String commandText = "{call dbo.Notifications(?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3)));
					stmt.registerOutParameter(2, Types.VARCHAR);
					stmt.execute();
					protocolo = "type | notifications ; user_id | " + processa.get(3) + stmt.getString(2);
					System.out.println(protocolo);
					stmt.close();
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
			case "add_notifications":
				try {
					String commandText = "{call dbo.AddNoti(?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(5)));
					stmt.setObject(2, new String(processa.get(7)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					protocolo = "type | confirmation ; " + processa.get(2) +" | " + processa.get(3) + " ; notification | " + stmt.getString(4);
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
			case "criar":
				try {
					String commandText = "{call dbo.Criar(?,?,?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(5))); //tipo
					stmt.setObject(2, new String(processa.get(7))); //nome
					stmt.setObject(3, new String(processa.get(9))); //Info
					stmt.setObject(4, new String(processa.get(11))); //Info2
					stmt.registerOutParameter(5, Types.INTEGER);
					stmt.registerOutParameter(6, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(5) >= 0 ) protocolo = "type | criar ; user_id | " + processa.get(3) + " ; confirmation | true";
					else protocolo = "type | criar ; user_id | " + processa.get(3) + " ; confirmation | false";
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
					stmt.setObject(1, new String(processa.get(5)));
					stmt.setObject(2, new String(processa.get(7)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();
					protocolo = "type | search ; " + processa.get(2) +" | " + processa.get(3) + stmt.getString(4);

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
					stmt.setObject(1, new String(processa.get(5)));
					stmt.setObject(2, new String(processa.get(7)));
					stmt.registerOutParameter(3, Types.INTEGER);
					stmt.registerOutParameter(4, Types.VARCHAR);
					stmt.execute();

					protocolo = "type | details ; " + processa.get(2) +" | " + processa.get(3) + stmt.getString(4);
					System.out.println(protocolo);
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "critic":
				try {
					String commandText = "{call dbo.WriteCritic(?,?,?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new Integer(Integer.parseInt(processa.get(3))));
					stmt.setObject(2, new String(processa.get(7)));
					stmt.setObject(3, new Integer(Integer.parseInt(processa.get(9))));
					stmt.setObject(4, new String(processa.get(11)));
					stmt.registerOutParameter(5, Types.INTEGER);
					stmt.registerOutParameter(6, Types.VARCHAR);
					stmt.execute();
					if(stmt.getInt(5) >= 0 ) protocolo = "type | critic ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | escrito";
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
					System.out.println("Linha 413 " + protocolo);
					if(stmt.getInt(3) >= 0 ) protocolo = "type | privileges ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | done";
					else protocolo = "type | privileges ; " + processa.get(2) +" | " + processa.get(3) + " ; confirmation | negado";
					System.out.println("Linha 416 " + protocolo);
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "alteration":
				try {
					String commandText = "{call dbo.Alteration(?,?,?,?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3))); //User_id
					stmt.setObject(2, new String(processa.get(6))); //Tipo
					stmt.setObject(3, new String(processa.get(7))); //nome
					stmt.setObject(4, new String(processa.get(8))); //O que vai ser alterado
					stmt.setObject(5, new String(processa.get(9))); //alteracao
					stmt.registerOutParameter(6, Types.INTEGER);
					stmt.registerOutParameter(7, Types.VARCHAR);
					stmt.execute();
					protocolo = "type | alteration ; " + processa.get(2) +" | " + processa.get(3) + stmt.getString(7);
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "add":
				try {
					String commandText = "{call dbo.AddSome(?,?,?,?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3))); //User_id
					stmt.setObject(2, new String(processa.get(5))); //Tipo
					stmt.setObject(3, new String(processa.get(7))); //nome
					stmt.setObject(4, new String(processa.get(8))); //o que vai ser adicionado
					stmt.setObject(5, new String(processa.get(9))); //adiciona

					stmt.registerOutParameter(6, Types.INTEGER);
					stmt.registerOutParameter(7, Types.VARCHAR);
					stmt.execute();
					protocolo = "type | add ; " + processa.get(2) +" | " + processa.get(3) + stmt.getString(7);
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "remove":
				try {
					String commandText = "{call dbo.RemoveSome(?,?,?,?,?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(3))); //User_id
					stmt.setObject(2, new String(processa.get(5))); //Tipo
					stmt.setObject(3, new String(processa.get(7))); //nome
					stmt.setObject(4, new String(processa.get(8))); //o que vai ser adicionado
					stmt.setObject(5, new String(processa.get(9))); //adiciona

					stmt.registerOutParameter(6, Types.INTEGER);
					stmt.registerOutParameter(7, Types.VARCHAR);
					stmt.execute();
					protocolo = "type | remove ; " + processa.get(2) +" | " + processa.get(3) + stmt.getString(7);
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;
			case "share1":
				File folder = new File("C:\\Users\\santa\\Desktop\\Musicas_DropMusic\\privado\\" + processa.get(3));
				File[] listOfFiles = folder.listFiles();
				int counter = 0;
				String[] respostas = new String[100];
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						respostas[counter] = listOfFiles[i].getName();
						counter++;
					}
				}
				protocolo = "type | share1 ; user_id | " + processa.get(3) + " ; ";
				for(int i = 0; i<counter ; i++){
					protocolo = protocolo + respostas[i] + " | ";
				}
				protocolo = protocolo + "none";
				return protocolo;

			case "share2":
				File musicFile = new File("C:\\Users\\santa\\Desktop\\Musicas_DropMusic\\privado\\" + processa.get(3) + "\\" + processa.get(5));
				musicFile.renameTo(new File("C:\\Users\\santa\\Desktop\\Musicas_DropMusic\\publico\\" + processa.get(5)));
				protocolo = "type | share2 ; user_id | " + processa.get(3) + " ; confirmation | true";
				return protocolo;

			case "getgeneros":
				try {
					String commandText = "{call dbo.GetGeneros(?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.registerOutParameter(1, Types.INTEGER);
					stmt.registerOutParameter(2, Types.VARCHAR);
					stmt.execute();
					protocolo = "type | getgeneros ; "+ processa.get(2) + " | " + processa.get(3) + stmt.getString(2) + "none";
					return protocolo;
				} catch (SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
				break;

			case "addgenero":
				try {
					String commandText = "{call dbo.AddGenero(?,?,?)}";
					conn = DriverManager.getConnection(con);
					CallableStatement stmt = conn.prepareCall(commandText);
					stmt.setObject(1, new String(processa.get(5)));
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
				protocolo = "type | GetAddress ; user_id | " + processa.get(3) + " ; 0.0.0.0 | 6000";
				if(processa.get(5).compareTo("download")==0){
					EnviaMusica e = new EnviaMusica(processa.get(7),processa.get(3));
					e.start();
				}
				else{
					RecebeMusica r = new RecebeMusica(processa.get(7),processa.get(3));
					r.start();
				}
				return protocolo;
			default:
				protocolo = "type | error ; " + processa.get(2) +" | " + processa.get(3) + " ; function | " + processa.get(1);
				return protocolo;
		}

		return "type | error ; " + processa.get(2) +" | " + processa.get(3) + " ; function | " + processa.get(1);
	}
}

/**
 * Description: Esta classe é utilizada para a comunicação dos servidores e para estabelecimento dos nomes
 *
 * @param
 * @return
 */

class Synch extends Thread{
	private int server = 1;
	private MulticastSocket socket;
	private DatagramPacket packet;
	private InetAddress group;
	private String MULTICAST_ADDRESS = "224.3.2.1";
	private int PORT_MANAGE = 4323;
	private boolean blocked = false;
	private int Max = 0;
	public Synch(){
		this.start();
	}
	public synchronized int GetServerNumber(){
		return server;
	}
	public synchronized int GetMaxServers() {
		return Max;
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
					if(server == Integer.parseInt(resposta)){
						server = Integer.parseInt(resposta)+1;
						Max = server;
					}
				}
				else{
					if(Integer.parseInt(resposta) > Max) Max = Integer.parseInt(resposta);
				}
			}

		}catch(Exception c){
			System.out.println("Erro na thread de sincronizacao: " + c);
		}
	}
}

/**
 * Description: Classe utilizada quando o User faz Upload da música
 *
 * @param
 * @return
 */

class RecebeMusica extends Thread{
	DataInputStream in;
	FileOutputStream out;
	Socket clientSocket;
	String music, User_id;
	int PORT_USER = 6000;
    public RecebeMusica(String music, String User_id) {
    	this.music = music.replaceAll("\\s+","");;
    	this.User_id = User_id;
    }
    //=============================
    public void run(){
        String resposta;
        try{
        	try{
			ServerSocket listenSocket = new ServerSocket(PORT_USER);
			clientSocket = listenSocket.accept();
			in = new DataInputStream(clientSocket.getInputStream());
			}catch(IOException e){System.out.println("Connection:" + e.getMessage());}
			byte[] bytes = new byte[20000];
			File dir = new File("C:\\Users\\santa\\Desktop\\Musicas_DropMusic\\privado\\"+User_id+"\\");
			if(!dir.exists()) {
				try {
					dir.mkdir();
				} catch (Exception x) {
					System.out.println(x);
				}
			}
			out = new FileOutputStream("C:\\Users\\santa\\Desktop\\Musicas_DropMusic\\privado\\" + User_id + "\\"+music+".mp3");
        	int count;
			while ((count = in.read(bytes)) > 0)
			{
				out.write(bytes, 0, count);
			}
			clientSocket.close();
			out.close();
			in.close();
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);}
    }
}

/**
 * Description: Classe utilizada quando o User faz Download da música
 *
 * @param
 * @return
 */

class EnviaMusica extends Thread{
	DataInputStream in;
	FileOutputStream out;
	Socket clientSocket;
	String music, User_id;
	int PORT_USER = 6000;
	
    public EnviaMusica(String music, String User_id) {
    	this.music = music.replaceAll("\\s+","");
    	this.User_id = User_id;
    }
    //=============================
    public void run(){
        String resposta;
		try{
			ServerSocket listenSocket = new ServerSocket(PORT_USER);
			clientSocket = listenSocket.accept();
		}catch(IOException e){System.out.println("Connection:" + e.getMessage());}
        try{
        	File musica = new File("C:\\Users\\santa\\Desktop\\Musicas_DropMusic\\publico\\" +music+".mp3");
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			InputStream is = new FileInputStream(musica);
			byte[] buffer = new byte[8192];
			int count;
			while ((count = is.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();
			in.close();
			clientSocket.close();
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e) { System.out.println("IO:" + e);
		}catch (Exception c) { System.out.println("Erro: " + c);
		}
    }
}

