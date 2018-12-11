package model.interfaces;

import java.rmi.registry.LocateRegistry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import Drop.ServidorRMI.DropMusic_S_I;

public class HeyBean {
	private static DropMusic_S_I server;
	private static String Server = "Drop1";
	private static int PORT = 7000;
	private String username;

	public HeyBean() {
		try {
			server = (DropMusic_S_I) LocateRegistry.getRegistry(PORT).lookup(Server);
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace();
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

	public static String[] CheckUser(String user, String password, boolean registar){
		String[] resposta = new String[3];
		while(true) {
			try {
				if(registar) resposta = server.RegistUser(user, password);
				else resposta = server.CheckUser(user, password);
				break;
			} catch (Exception c) {
				BackUp();
			}
		}
		return resposta;
	}

	public static String[] Procura(String nome, String tipo){
		String[] respostas;
		while(true){
			try{
				respostas = server.Find("0",nome,tipo);
				break;
			} catch (Exception re) {
				BackUp();
			}
		}
		return respostas;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
