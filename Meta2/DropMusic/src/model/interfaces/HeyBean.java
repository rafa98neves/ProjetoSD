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

	public static String[] Procura(String ID, String nome, String tipo){
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

	public static boolean Cria(String ID, String nome, String tipo, String info1, String info2){
		while(true) {
			try {
				server.Criar("0", tipo, nome, info1,info2);
				return true;
			} catch (Exception c) {
				BackUp();
			}
		}
	}

	public static String[] GetDetails(String ID, String nome, String tipo){
		while(true) {
			try {
				return server.GetDetails("0",nome,tipo);
			} catch (Exception c) {
				BackUp();
			}
		}
	}

	public static boolean ChangeInfo(String ID, String nome,String tipo,String alvo,String alteracao,String alterado){
		while(true) {
			try {
				server.AlterarDados(ID,nome,tipo,alvo,alteracao,alterado);
				return true;
			} catch (Exception c) {
				BackUp();
			}
		}
	}
	public static boolean Critica(String ID,String user,int pontuacao, String texto, String alvo){
		while(true) {
			try {
				return server.Write(ID,user,pontuacao,texto,alvo);
			} catch (Exception c) {
				BackUp();
			}
		}
	}
	public static void GivePrev(String ID,String user){
		while(true) {
			try {
				server.GivePriv(ID,true,user);
				break;
			} catch (Exception c) {
				BackUp();
			}
		}
	}
	public static String[] CheckNotifications(String ID){
		while(true) {
			try {
				return server.CheckNotifications(ID);
			} catch (Exception c) {
				BackUp();
			}
		}
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
