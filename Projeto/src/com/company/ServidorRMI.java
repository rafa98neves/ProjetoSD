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
import java.util.regex.Pattern;

class MulticastConnection extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT_SEND = 4322;
	private int PORT_RECEIVE = 4321;
	private String protocolo;

	public MulticastConnection(String protocolo){
		super("Multicast Conection");
		this.protocolo = protocolo;
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
			System.out.println("Bloqueado");
			socket.receive(packet);
			System.out.println("Desbloqueado");

			String received = new String(packet.getData(), 0, packet.getLength());
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
	private static String[] usernames = new String[100];
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
		usernames[i] = username;
	}

	public void UserQuit(DropMusic_C_I c, String username) throws RemoteException {
		int i = 0;
		while(usernames[i].compareTo(username) != 0) i++;
		online[i] = null;
		usernames[i] = " ";
	}

	public void CheckNotifications(String username, DropMusic_C_I c) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | notifications ; username | " + username;
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
			if(processa.get(3).compareTo("none")==0) c.Print("\nInformacao: Nenhuma notificacao pendente");
			else{
				int n = (processa.size() - 2)/2;
				c.Print("\nInformacao: Tem " + n + " notificacoes pendentes");
				for(int i = 3; i <= processa.size() ; i+=2){
					c.Print("\n\t ." + processa.get(i));
				}
			}
		} catch (Exception re) {
			System.out.println("Exception in Find(): " + re);
		}
	}

	public void AddNotification(String username, String notificacao){
		String protocolo = new String();
		protocolo = "type | add_notifications ; username | " + username + " ; notification | " + notificacao;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
	}

	public boolean RegistUser(String username, String password) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | registo ; username | " + username + " ; password | " + password;
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
		if(processa.get(3).compareTo("true")==0) return true;
		else return false;
	}

	public boolean CheckUser(String username, String password,  DropMusic_C_I c) throws RemoteException{
		return true;
		/*String protocolo = new String();
		protocolo = "type | login ; username | " + username + " ; password | " + password;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
		String[] processar = protocolo.split(Pattern.quote(" ; "));
		ArrayList<String> processa = new ArrayList<String>();
		String[] aux;
		for(String s : processar){
                    aux = s.split(Pattern.quote(" | "));
                    processa.add(aux[1]);
                    processa.add(aux[2]);
		}
		if(processa.get(3).compareTo("true")==0) return true;
		else return false;*/
	}

	public String[] Find(String name, String tipo, DropMusic_C_I c) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | search ; name | " + name + " ; from | " + tipo;
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
		if(processa.get(3).compareTo("none")==0){
			resposta[0] = "none";
			return resposta;
		}
		else{
			int counter=0;
			for(int i = 3; i <= processa.size() ; i+=2){
				resposta[counter] = processa.get(i);
				counter++;
			}
			return resposta;
		}
	}

	public String[] GetDetails(String name, String tipo, DropMusic_C_I c) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | details ; name | " + name + " ; from | " + tipo;
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
		if(processa.get(3).compareTo("none")==0){
			resposta[0] = "none";
			return resposta;
		}
		else{
			int counter=0;
			for(int i = 2; i <= processa.size() ; i++){
				resposta[counter] = processa.get(i);
				counter++;
			}
			return resposta;
		}
	}

	public void AddRemoveSomething(String tipo, String nome, String dado_add_removido, boolean remove) throws RemoteException{
		String protocolo = new String();
		if(remove) protocolo = "type | remove ; from | " + tipo + " ; named | " + nome +" ; this | " + dado_add_removido; //Remove do 'tipo' (album, artista,..) com este 'nome' este dado
		else protocolo = "type | add ; from | " + tipo + " ; named | " + nome +" ; this | " + dado_add_removido; //Adiciona do 'tipo' (album, artista,..) com este 'nome' este dado
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
	}

	public void AlterarDados(String username, String tipo, String alvo, String alteracao, String alterado) throws RemoteException{ //tipo = album, musica, (...) ||  alvo = nome // alteracao = genero, artista (...) // alterado = texto novo
		String protocolo = new String();
		protocolo = "type | alteration ; username | " + username + " ; " + tipo + " | " + alvo + " ; " + alteracao + " | " + alterado;
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
		if(aux[1].compareTo("alterado")==0){
			int counter = 3;
			int i = 0;
			while(processa.get(counter).compareTo("none") != 0){ //Notificar editores anteriores
				while(usernames[i] != null){
					if(usernames[i].compareTo(username) == 0){
							try{
								online[i].Print("O editor " + username + " alterou informacao + " + alteracao + " no(a) " + tipo + " - " + alvo);
							}catch(Exception c1){
								AddNotification(username, ("O editor " + username + " alterou informacao + " + alteracao + " no(a) " + tipo + " - " + alvo));
							}
					}
					i++;
				}
				counter++;
			}
		}
	}

	public String[] GetGeneros() throws RemoteException{
		String protocolo = new String();
		protocolo = "type | getgeneros";
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" | "));

		return processar;
	}

	public void AddGenero(String genero) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | addgenero ; genero | " + genero;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
	}

	public boolean Write(String username, int pont, String critica, String album) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | critic ; username | " + username + " ; album | " + album + " ; score | " + Integer.toString(pont) + " ; write | " + critica;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String [] processa = protocolo.split(Pattern.quote(" ; "));
		String aux[] = processa[1].split(Pattern.quote(" | "));
		if(aux[1].compareTo("escrito")==0) return true;
		else return false;
	}

	public void ShareMusic(String username) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | share ; username | " + username;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
	}

	public String[] TransferMusic(String username) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | GetAddress  ; username | " + username;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
		String[] aux = protocolo.split(Pattern.quote(" ; "));
		String[] endereco = aux[1].split(Pattern.quote(" | "));
		return endereco;
	}


	public boolean GivePriv(boolean editor, String username, DropMusic_C_I c) throws RemoteException{
		int i = 0;
		String protocolo = new String();
		protocolo = "type | privileges  ; username | " + username + " ; editor | " + editor;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();

		String[] processar = protocolo.split(Pattern.quote(" ; "));
		String[] aux = processar[1].split(Pattern.quote(" | "));
		if(aux[1].compareTo("done")==0){
			while(usernames[i] != null){
				if(usernames[i].compareTo(username) == 0){
					if(editor){
						try{
							online[i].Print("Parabens, foi promovido a editor!");
						}catch(Exception c1){
							AddNotification(username, "Parabens, foi promovido a editor!");
						}
					}
					else{
						try{
							online[i].Print("Um editor tirou os seu previlegios");
						}catch(Exception c2){
							AddNotification(username, "Um editor tirou os seu previlegios");
						}
					}
					return true;
				}
				i++;
			}
			if(usernames[i] == null){
				if(editor) AddNotification(username, "Parabens, foi promovido a editor!");
				else AddNotification(username, "Um editor tirou os seu previlegios");
				return true;
			}
		}
		return false;
	}

	// ============================MAIN===========================

	public static void main(String args[]) {
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
						Thread.sleep(30000);
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
						Thread.sleep(1000);
					}catch(Exception erro2){
						System.out.println("Exception ThreadSleep.main: " + erro2);
					}
					try{
						if(port == 7000) h = (DropMusic_S_I) LocateRegistry.getRegistry(7001).lookup("Drop2");
						else if(port == 7001) h = (DropMusic_S_I) LocateRegistry.getRegistry(7000).lookup("Drop1");
						else;
					}catch(Exception c4){
						try{
							secundario = false;
							System.out.println("Server is now primary");
							break;
						}catch(Exception c5){
							System.out.println("Exception in DropMusicImpl.main: " + c5);
						}
					}
				}
			}
		}
	}



}
