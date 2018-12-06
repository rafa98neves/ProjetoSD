package Drop.DropWeb.model;

import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import Drop.ServidorRMI.DropMusic_S_I;

public class HeyBean {
	private static DropMusic_S_I server;
	private static String Server = "Drop1";
	private static int PORT = 7000;
	private String username; // username and password supplied by the user
	private String password;

	public HeyBean() {
		try {
			server = (DropMusic_S_I) LocateRegistry.getRegistry(PORT).lookup(Server);
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}

	public static void BackUp(){
		int connection = 0;
		int counter = 0;

		do{
			counter++;
			try {
				Server = "Drop2";
				PORT = 7001;
				server = (DropMusic_S_I)LocateRegistry.getRegistry(PORT).lookup(Server);
			}catch(Exception e1){
				try{
					Server = "Drop1";
					PORT = 7000;
					server = (DropMusic_S_I) LocateRegistry.getRegistry(PORT).lookup(Server);
				}catch(Exception e2){
					connection++;
					try{
						Thread.sleep(1000);
					}catch(Exception e3){
						System.out.println("Problemas com a thread main: " + e3);
					}
				}
			}
			if(connection != counter) break;
		}while(connection != 30);

		if(connection == 30){
			System.out.println("Nao foi possivel estabelecer a ligacao ao servidor, tente mais tarde");
			System.exit(0);
		}
	}

	public static boolean CheckUser(String user, String password){
		String[] resposta = new String[3];
		while(true) {
			try {
				server.ping();
				resposta = server.CheckUser(user, password);
				break;
			} catch (Exception c) {
				BackUp();
			}
		}
		if (resposta[0].compareTo("true") == 0) return true;
		else return false;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
