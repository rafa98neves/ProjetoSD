package com.company;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.io.*;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

class MulticastConnection extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT_SEND = 4322;
	private int PORT_RECEIVE = 4321;
	private String protocolo;
	private String ID;
	public MulticastConnection(String protocolo){
		super("Multicast Conection");
		this.protocolo = protocolo;
		String[] aux = protocolo.split(Pattern.quote(" ; "));
		String[] aux2 = aux[1].split(Pattern.quote(" | "));
		this.ID = aux2[1];
	}
	public String GetResponse(){
		this.start();
		try{
			this.join();
		}catch(Exception c){
			System.out.println("Join error in Multicast Thread: " + c);
		}
		return protocolo;
	}

    public void run() {

        MulticastSocket socket = null;
        long counter = 0;
		byte[] buffer = protocolo.getBytes();
		try{
			socket = new MulticastSocket(PORT_RECEIVE);
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT_SEND);
			socket.send(packet);

			buffer = new byte[256];

			packet = new DatagramPacket(buffer, buffer.length, group, PORT_RECEIVE);
			String received = "erro";
			String ID_received = "no";
			while(ID.compareTo(ID_received) != 0) {
				System.out.println("Bloqueado");
				socket.receive(packet);
				System.out.println("Desbloqueado");
				received = new String(packet.getData(), 0, packet.getLength());
				System.out.println(received);
				String[] aux = received.split(Pattern.quote(" ; "));
				String[] aux2 = aux[1].split(Pattern.quote(" | "));
				ID_received = aux2[1];
			}
			protocolo = received;
		}catch(Exception c){
			System.out.println("Exception in send/receive : " + c);
		}finally{
			socket.close();
		}
    }
}

public class ServidorRMI extends UnicastRemoteObject implements DropMusic_S_I{

	private static DropMusic_C_I[] online = new DropMusic_C_I[100];
	private static String[] users_online = new String[100];
	public ServidorRMI() throws RemoteException {
		super();
	}

	public void ping() throws RemoteException {}

	public void NewUser(DropMusic_C_I c, String username) throws RemoteException {
		int i = 0;
		while(online[i] != null){
			if(online[i] != c) i++;
			else return;
		}
		online[i] = c;
		users_online[i] = username;
	}

	public void UserQuit(DropMusic_C_I c, String username) throws RemoteException {
		int i = 0;
		while(users_online[i].compareTo(username) != 0) i++;
		online[i] = null;
		users_online[i] = " ";
	}

	public void CheckNotifications(String ID , DropMusic_C_I c) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | notifications ; user_id  | " + ID;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" ; "));
		ArrayList<String> processa = new ArrayList<String>();
		String[] aux;
		for(String s : processar){
                    aux = s.split(Pattern.quote(" | "));
                    processa.add(aux[0]);
                    processa.add(aux[1]);
		}

		try {
			if(processa.get(5).compareTo("none")==0) c.Print("\nInformacao: Nenhuma notificacao pendente");
			else{
				int n = (processa.size() - 4)/2;
				c.Print("\nInformacao: Tem " + n + " notificacoes pendentes");
				for(int i = 5; i <= processa.size() ; i+=2){
					c.Print("\n\t ." + processa.get(i));
				}
			}
		} catch (Exception re) {
			System.out.println("Exception in Find(): " + re);
		}
	}

	public void AddNotification(String ID, String notificado, String notificacao){
		String protocolo = new String();
		protocolo = "type | add_notifications ; user_id | " + ID + " ; notificado | " + notificado + " ; notification | " + notificacao;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
	}

	public String[] RegistUser(String username, String password) throws RemoteException{
		String protocolo = new String();
		String proto_id = UUID.randomUUID().toString();
		protocolo = "type | registo ; protocolo_id | " + proto_id +" ; username | " + username + " ; password | " + password;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" ; "));
		ArrayList<String> processa = new ArrayList<String>();
		String[] aux;
		for(String s : processar){
                    aux = s.split(Pattern.quote(" | "));
                    processa.add(aux[0]);
                    processa.add(aux[1]);
		}

		if(processa.get(5).compareTo("true")==0){
			String[] Info = new String[3];
			Info[0] = processa.get(5);
			Info[1] = processa.get(7);
			Info[2] = processa.get(9);
			return Info;
		}
		else{
			String[] Info = new String[1];
			Info[0] = "false";
			return Info;
		}
	}

	public String[] CheckUser(String username, String password) throws RemoteException{
		String protocolo = new String();
		String proto_id = UUID.randomUUID().toString();
		protocolo = "type | login ; protocolo_id | " + proto_id + " ; username | " + username + " ; password | " + password;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" ; "));
		ArrayList<String> processa = new ArrayList<String>();
		String[] aux;
		for(String s : processar){
			aux = s.split(Pattern.quote(" | "));
			processa.add(aux[0]);
			processa.add(aux[1]);
		}
		if(processa.get(5).compareTo("true")==0){
			String[] Info = new String[3];
			Info[0] = processa.get(5);
			Info[1] = processa.get(7);
			Info[2] = processa.get(9);
			return Info;
		}
		else{
			String[] Info = new String[1];
			Info[0] = "false";
			return Info;
		}
	}

	public String[] Find(String ID, String name, String tipo) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | search ; user_id | " + ID + " ; name | " + name + " ; from | " + tipo;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" ; "));
		ArrayList<String> processa = new ArrayList<String>();
		String[] aux;
		for(String s : processar){
			aux = s.split(Pattern.quote(" | "));
			processa.add(aux[0]);
			processa.add(aux[1]);
		}

		String[] resposta = new String[(processa.size() - 4)/2];
		if(processa.get(5).compareTo("none")==0){
			resposta[0] = "none";
			return resposta;
		}
		else{
			int counter=0;
			for(int i = 5; i <= processa.size() ; i+=2){
				resposta[counter] = processa.get(i);
				counter++;
			}
			return resposta;
		}
	}

	public String[] GetDetails(String ID, String name, String tipo) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | details ; user_id | " + ID + " ; name | " + name + " ; from | " + tipo;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" ; "));
		ArrayList<String> processa = new ArrayList<String>();
		String[] aux;
		for(String s : processar){
                    aux = s.split(Pattern.quote(" | "));
                    processa.add(aux[0]);
                    processa.add(aux[1]);
		}

		String[] resposta = new String[(processa.size() - 2)/2];
		if(processa.get(5).compareTo("none")==0){
			resposta[0] = "none";
			return resposta;
		}
		else{
			int counter=0;
			for(int i = 4; i <= processa.size() ; i++){
				resposta[counter] = processa.get(i);
				counter++;
			}
			return resposta;
		}
	}

	public void AddRemoveSomething(String ID,String tipo, String nome, String dado_add_removido, boolean remove) throws RemoteException{
		String protocolo = new String();
		if(remove) protocolo = "type | remove ; user_id | " + ID + " ; from | " + tipo + " ; named | " + nome +" ; this | " + dado_add_removido; //Remove do 'tipo' (album, artista,..) com este 'nome' este dado
		else protocolo = "type | add ; user_id | " + ID +  " ; from | " + tipo + " ; named | " + nome +" ; this | " + dado_add_removido; //Adiciona do 'tipo' (album, artista,..) com este 'nome' este dado
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
	}

	public void AlterarDados(String ID,String username, String tipo, String alvo, String alteracao, String alterado) throws RemoteException{ //tipo = album, musica, (...) ||  alvo = nome // alteracao = genero, artista (...) // alterado = texto novo
		String protocolo = new String();
		protocolo = "type | alteration ; user_id | " + ID + " ; username | " + username + " ; " + tipo + " | " + alvo + " ; " + alteracao + " | " + alterado;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" ; "));
		ArrayList<String> processa = new ArrayList<String>();
		String[] aux = new String[2];
		for(String s : processar){
                    aux = s.split(Pattern.quote(" | "));
                    processa.add(aux[0]);
                    processa.add(aux[1]);
		}
		int counter = 5;
		int i = 0;
		while(processa.get(counter).compareTo("none") != 0){ //Notificar editores anteriores
			while(users_online[i] != null){
				if(users_online[i].compareTo(processa.get(counter)) == 0){
						try{
							online[i].Print("O editor " + username + " alterou informacao + " + alteracao + " no(a) " + tipo + " - " + alvo);
						}catch(Exception c1){
							AddNotification(ID, processa.get(counter), ("O editor " + username + " alterou informacao + " + alteracao + " no(a) " + tipo + " - " + alvo));
						}
				}
				i++;
			}
			counter++;
		}

	}

	public String[] GetGeneros(String ID) throws RemoteException{
		String protocolo;
		protocolo = "type | getgeneros ; user_id | " + ID;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" ; "));
		String[] aux = processar[2].split(Pattern.quote(" | "));

		return aux;
	}

	public void AddGenero(String ID, String genero) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | addgenero ; user_id | " + ID + " ; genero | " + genero;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
	}

	public boolean Write(String ID, String username, int pont, String critica, String album) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | critic ; user_id | " + ID + " ; username | " + username + " ; album | " + album + " ; score | " + Integer.toString(pont) + " ; write | " + critica;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String [] processa = protocolo.split(Pattern.quote(" ; "));
		String aux[] = processa[2].split(Pattern.quote(" | "));
		if(aux[1].compareTo("escrito")==0) return true;
		else return false;
	}

	public void ShareMusic(String ID, String username) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | share ; use_id | " + ID + " ; username | " + username;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
	}

	public String[] TransferMusic(String ID, String username) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | GetAddress ; user_id | " + ID +" ; username | " + username;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
		String[] aux = protocolo.split(Pattern.quote(" ; "));
		String[] endereco = aux[2].split(Pattern.quote(" | "));
		return endereco;
	}

	public boolean GivePriv(String ID, boolean editor, String username) throws RemoteException{
		int i = 0;
		String protocolo = new String();
		protocolo = "type | privileges  ; username | " + username + " ; editor | " + editor;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" ; "));
		String[] aux = processar[1].split(Pattern.quote(" | "));
		if(aux[1].compareTo("done")==0){
			while(users_online[i] != null){
				if(users_online[i].compareTo(username) == 0){
					if(editor){
						try{
							online[i].Print("Parabens, foi promovido a editor!");
							online[i].ChangeUserToEditor(true);
						}catch(Exception c1){
							AddNotification(ID,username, "Parabens, foi promovido a editor!");
						}
					}
					else{
						try{
							online[i].Print("Um editor tirou os seu previlegios");
							online[i].ChangeUserToEditor(false);
						}catch(Exception c2){
							AddNotification(ID,username, "Um editor tirou os seu previlegios");
						}
					}
					return true;
				}
				i++;
			}
			if(users_online[i] == null){
				if(editor) AddNotification(ID,username, "Parabens, foi promovido a editor!");
				else AddNotification(ID,username, "Um editor tirou os seu previlegios");
				return true;
			}
		}
		return false;
	}

	// ============================MAIN===========================

	public static void main(String args[]) {
		int ErrorCounting = 0;
		boolean secundario = false;
		int port = 0;
		DropMusic_S_I h;

		try{
			h = (DropMusic_S_I) LocateRegistry.getRegistry(7000).lookup("Drop1");

			ServidorRMI s2 = new ServidorRMI();
			Registry r2 = LocateRegistry.createRegistry(7001);
			r2.rebind("Drop2", s2);
			port = 7001;
			secundario = true;
			System.out.println("DropMusic RMI Secundary Server ready.");
		}catch(Exception c1){
			try {
				h = (DropMusic_S_I) LocateRegistry.getRegistry(7001).lookup("Drop2");

				ServidorRMI s = new ServidorRMI();
				Registry r = LocateRegistry.createRegistry(7000);
				r.rebind("Drop1", s);
				port = 7000;
				secundario = true;
				System.out.println("DropMusic RMI Secundary Server ready.");
			}catch(Exception c2){
				try{
					ServidorRMI s = new ServidorRMI();
					Registry r = LocateRegistry.createRegistry(7000);
					port = 7000;
					r.rebind("Drop1", s);
					System.out.println("DropMusic RMI Primary Server ready.");
				}catch(Exception c3){
					System.out.println("Erro: " +c3);
				}
			}
		}

		while(true){
			if(!secundario){
				while (true) {	//Fazer testes ao Servidor Secundario
					try{
						Thread.sleep(60000);
					}catch(Exception erro){
						System.out.println("Exception ThreadSleep.main: " + erro);
					}
					try{
						if(port == 7000) h = (DropMusic_S_I) LocateRegistry.getRegistry(7001).lookup("Drop2");
						else if(port == 7001) h = (DropMusic_S_I) LocateRegistry.getRegistry(7000).lookup("Drop1");
						else;
					}catch(Exception c3){
						System.out.println("Servidor de Backup esta indisponivel");
					}
				}
			}
			else{
				while (true) {	//Fazer testes ao Servidor Primario
					try{
						Thread.sleep(500);
					}catch(Exception erro2){
						System.out.println("Exception ThreadSleep.main: " + erro2);
					}
					try{
						if(port == 7000) h = (DropMusic_S_I) LocateRegistry.getRegistry(7001).lookup("Drop2");
						else if(port == 7001) h = (DropMusic_S_I) LocateRegistry.getRegistry(7000).lookup("Drop1");
						else;
					}catch(Exception c4){
						ErrorCounting++;
						System.out.println("Servidor primario nao responde... " + ErrorCounting);
						try{
							if(ErrorCounting == 5) {
								secundario = false;
								System.out.println("Server is now primary");
								break;
							}
						}catch(Exception c5){
							System.out.println("Exception in DropMusicImpl.main: " + c5);
						}
					}
				}
			}
		}
	}



}
