import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.io.*;

public class ClienteRMI extends UnicastRemoteObject implements DropMusic_C_I{
	private static User online;
	private static ClienteRMI c;
	public static DropMusic_S_I h;
	
	ClienteRMI() throws RemoteException {
		super();
	}
	
	public void ChangeUserToEditor(){
		online.ChangeUserToEditor(true);
	}
	public void ping(){
	}
	
	public void Print(String s){
		System.out.println(s);
	}
	public static void main(String args[]) {
		boolean exit=false;
		String a;

		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		try {
			h = (DropMusic_S_I) Naming.lookup("Drop");
		}catch (Exception e1) {
			BackUp();
		}
		
		try{
			c = new ClienteRMI();
		}catch(Exception e2){
			System.out.println("Problemas a criar o cliente: " + e2);
		}
		MainScreen();
	}
     
	public static void LogOut(){
		while(true){
			try{
				h.UserQuit(c, online.GetNome());
				break;
			}catch(Exception c){
				BackUp();
			}
		}
		System.exit(0);
	}
	
	
	public static void BackUp(){
		int connection = 0;	
		int counter = 0;
		do{
			counter++;
			try {
				h = (DropMusic_S_I) Naming.lookup("Drop");
				h.ping();
				h.NewUser(c, online.GetNome());
			}catch(Exception e1){
				try{
				h = (DropMusic_S_I) Naming.lookup("Drop_Backup");
				h.ping();
				h.NewUser(c, online.GetNome());
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
	
	public static void MainScreen(){
		System.out.println("\t\t\t >>DropMusic<< ");
		System.out.println("\n");
		System.out.printf("Pretende:\n\t1.Login\n\t2.Registo\n\t0.Exit\n");
		Scanner sc = new Scanner(System.in);
		
		while(true){
			int opcao = sc.nextInt();
			switch(opcao){
				case 1: Login();
					break;
				case 2: Registo();
					break;
				case 0: LogOut();
					break;
				default: System.out.println("Opção Inválida");
					break;
			}
		}
	}
	
	public static void Registo(){
		boolean registou = false;
		String nome = new String();
		String password = new String();
		Scanner sc = new Scanner(System.in);
		while(true){
			System.out.printf("\nNome de utlizador (sem espaços ou caracteres especiais): ");
			nome = sc.next();
			System.out.printf("Password: ");
			password = sc.next();
			while(true){
				try {
					if(h.RegistUser(nome,password)){
						System.out.println("Registo efectuado com sucesso!");
						registou = true;
					}
					break;
				} catch (Exception e) {
					BackUp();
				}
			}
			if(!registou){
				System.out.printf("Username ja esta em uso, escolha outro");
				System.out.printf("Pretende:\n1.Tentar outra vez\n2.Fazer Login\n\n0.Exit");
				if(sc.nextInt()==2) Login();
				else if(sc.nextInt()==0) LogOut();
			}
			else{
				break;
			}
		}
		online = new User(nome, password);
		DropMusic();
	}
	
	public static void Login(){
		String nome = new String();
		String password = new String();
		Scanner sc = new Scanner(System.in);
		System.out.printf("\nNome de utlizador: ");
		nome = sc.next();
		System.out.printf("Password: ");
		password = sc.next();
		while(true){
			try {
				if(h.CheckUser(nome, password,c)){
					System.out.println("Login efectuado com sucesso!");
					break;
				}
			} catch (Exception e) {
				BackUp();
			}
		}
		System.out.println("\nUsername ou password errados!");
		System.out.println("\nPretende:\n1.Tentar outra vez\n2.Registar\n\n0.Exit");
		if(sc.nextInt()==2) Registo();
		else if(sc.nextInt()==0) LogOut();
		online = new User(nome,password);
		DropMusic();
	}
	
	public static void DropMusic(){	
		
		int opcao;
		boolean exit = false;
		System.out.println("\n\n\n\n\t\t >>DropMusic<<");
		while(true){
			try{
				h.NewUser(c, online.GetNome());
				h.CheckNotifications(online.GetNome(), c); 
				Thread.sleep(500);
				break;
			}catch(Exception c){
				BackUp();
			}
		}
		Scanner sc = new Scanner(System.in);
		if(online.IsEditor()){
			System.out.println("\n1.Pesquisar\n2.Criar Playlist\n3.Partilhar músicas\n4.Donwload de músicas\n5.Upload de músicas\n6.Dar previlégios\n0.Log Out");
			while(!exit){
				opcao = sc.nextInt();
				switch(opcao){
					case 1:	Pesquisar();
							break;
					case 2: Playlist();
							break;
					case 3: Partilhar();
							break;
					case 4: Download();
							break;
					case 5: Upload();
							break;
					case 6: GivePrev();
							break;    
					case 0: exit=true;
							break;
					default: System.out.println("Opção Inválida");
				}
			}
		}
		else{
			System.out.println("\n1.Pesquisar\n2.Criar Playlist\n3.Partilhar músicas\n4.Donwload de músicas\n5.Upload de músicas\n0.Log Out");
			while(!exit){
				opcao = sc.nextInt();
				switch(opcao){
					case 1:	Pesquisar();
							break;
					case 2: Playlist();
							break;
					case 3: Partilhar();
							break;
					case 4: Download();
							break;
					case 5: Upload();
							break;
					case 0: exit=true;
							break;
					default: System.out.println("Opção Inválida");
				}
			}
		}
		MainScreen();
	}
	
	public static void Pesquisar(){
		int opcao;
		boolean exit = false;
		System.out.println("\n\n\t\t >>Pesquisar<<");
		Scanner sc = new Scanner(System.in);
		System.out.println("\n1.Pesquisar musica\n2.Pesquisar album\n3.Pesquisar genero\n4.Pesquisar artista\n0.Back");
		opcao = sc.nextInt();
		switch(opcao){
			case 1:	Pesquisar("musica");
					break;
			case 2: Pesquisar("album");
					break;
			case 3: Pesquisar("genero");
					break;
			case 4: Pesquisar("artista");
					break;
			case 0: DropMusic();
					break;
			default: System.out.println("Opção Inválida");
		}	
	}
	public static void Pesquisar(String escolha){
		String search;
		String[] respostas;
		Scanner sc = new Scanner(System.in);
		
		System.out.printf("\nNome:");
		search = sc.nextLine();
		while(true){
			try{
				respostas = h.Find(search,escolha,c);
				break;
			} catch (Exception re) {
				BackUp();
			}
		}
		
		if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + search);
		else{
			for(int possibilidades = 1; possibilidades<=respostas.length; possibilidades++){
				System.out.println(possibilidades + ". ->" + respostas[possibilidades-1]);
			}
			int numero = sc.nextInt();
			String[] details;
			while(true){
				try{
					details = h.GetDetails(respostas[numero-1],escolha,c);
					break;
				}catch(Exception re2){
					BackUp();
				}
			}
			if(details[0].compareTo("none")!=0){
				for(int i = 0; i<details.length; i+=2){
					System.out.println(details[i] + ": " + details[i+1]);
				}
			}			
		if(escolha.compareTo("album")==0) FazerCritica(respostas[numero-1]);		
		if(online.IsEditor()) FazerAlteracoes(escolha);
		}
	}
	
	public static void FazerCritica(String album){
		Scanner sc = new Scanner(System.in);
		String choice = new String();
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		System.out.println("Prentende fazer um critica [y/n]?");
		try{
			choice = reader.readLine();
		}catch(Exception re){
			System.out.println("Erro a escrever: " + re);
		}
		if(choice.compareTo("y")==0){
			String critica;
			int pontuacao;
			System.out.printf("Pontuacao [0 a 10]: ");
			while(true){
				pontuacao = sc.nextInt();
				if(pontuacao <= 10 || pontuacao >= 0) break;
				else System.out.printf("Escolha uma pontuacao entre 0 e 10!\n");
			}
			System.out.printf("\nComentario >>> ");
			try{
				while ((critica = reader.readLine()) != null){
					try{
						h.Write(online.GetNome(), pontuacao, critica, album);
					}catch(Exception t){
						BackUp();
						try{
							h.Write(online.GetNome(), pontuacao, critica, album);
						}catch(Exception tt){
							System.out.println("Nao foi possivel fazer o seu comentario, tenta mais tarde");
						}
					}
				}
			}catch(Exception re4){
				System.out.println("Não foi possivel fazer o seu comentario, tente mais tarde");
			}
		}
	}
	
	public static void FazerAlteracoes(String escolha){
		String choice = new String();
		Scanner sc = new Scanner(System.in);
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		System.out.printf("Pretende fazer alterações?[y/n]");
		try{
			choice = reader.readLine();
		}catch(Exception re){
			System.out.println("Erro a escrever: " + re);
		}
		if(choice.compareTo("y")==0){
			String alterado, alteracao; 
			while(true){
				if(escolha.compareTo("album") == 0){
					System.out.printf("O que quer alterar?[nome/ano/RemoverMusica/AdicionarMusica/Exit]: ");
					alterado = sc.nextLine().toLowerCase();
					if(alterado.compareTo("exit")==0) break;
					System.out.printf("\n>>> ");
					alteracao = sc.nextLine();
				}
				else if(escolha.compareTo("musica") == 0) {
					System.out.printf("O que quer alterar?[titulo/artistas/genero/Exit]: ");
					alterado = sc.nextLine().toLowerCase();
					if(alterado.compareTo("exit")==0) break;
					System.out.printf("\n>>> ");
					alteracao = sc.nextLine();
				}
				else if(escolha.compareTo("artista") == 0) {
					System.out.printf("O que quer alterar?[nome/Compositor/Exit]: ");
					alterado = sc.nextLine().toLowerCase();
					if(alterado.compareTo("exit")==0) break;
					System.out.printf("\n>>> ");
					alteracao = sc.nextLine();
				}
				else{
					System.out.printf("O que quer alterar?[AcrescentarGenero/Exit]: ");
					alterado = sc.nextLine().toLowerCase();
					if(alterado.compareTo("exit")==0) break;
					System.out.printf("\n>>> ");
					alteracao = sc.nextLine();
				}
				//Comunicar com RMI para fazer alteracoes <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			}				
		}
	}
	
	public static void Playlist(){
		/*System.out.println("\n\n\n\n\t\t >>Playlist<<");
		String[] playlists = h.GetPlaylists();
		int counter = 1;
		if(playlist[0].compareTo("none")!=0){
			for(int i = 0; i<playlists.length; i++){
				
				System.out.println(counter + ". " + playlists[i]);
				counter++;
			}
		}
		System.out.println(counter +". Criar nova");*/
		System.out.println("Funcionalidade a ser desenvolvida.");
	}
	
	
	
	
	public static void Upload(){
		System.out.println("\n\n\t\t >>Upload de musica<<");
	}
	
	public static void Partilhar(){
		System.out.println("\n\n\t\t >>Partilha de musica<<");
	}
	
	public static void Download(){
		System.out.println("\n\n\t\t >>Download de musica<<");
	}
	
	public static void GivePrev(){
		Scanner sc = new Scanner(System.in);
		System.out.println("\n\n\t\t >>Dar previlegios<<");
		System.out.printf("\nNome do utilizador a dar previlegios: ");
		String user = sc.nextLine();
		while(true){
			try{
				h.GivePriv(online.IsEditor(), user, c);
				Thread.sleep(500);
				break;
			} catch (Exception re) {
				BackUp();
			}
		}
		MainScreen();
	}
}