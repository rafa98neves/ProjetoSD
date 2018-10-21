import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.io.*;




class CheckingConnection extends Thread {
	private static DropMusic_S_I teste;
    
	public CheckingConnection(){
		super("Connection");
	}
	public void Checking(){
		this.start();
		ClienteRMI.Connected = "Drop";
		try{
			this.join();
		}catch(Exception c){
			System.out.println("Join error in Checking Thread: " + c);
		}
	}
	
    public void run() {
        while(true){				
			try{
				teste = (DropMusic_S_I) Naming.lookup("Drop");
				break;
			}catch(Exception c){}
		}
	}
}


public class ClienteRMI extends UnicastRemoteObject implements DropMusic_C_I{
	private static User online;
	private static ClienteRMI c;
	private static DropMusic_S_I h;
	public static String Connected = "Drop";
	
	ClienteRMI() throws RemoteException {
		super();
	}
	
	public void ChangeUserToEditor(){
		online.ChangeUserToEditor(true);
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
			h = (DropMusic_S_I) Naming.lookup(Connected);
		}catch (Exception e1) {
			BackUp();
			CheckingConnection n = new CheckingConnection();
			n.start();
		}
		
		try{
			c = new ClienteRMI();
		}catch(Exception e2){
			System.out.println("Problemas a criar o cliente: " + e2);
		}
		MainScreen();
	}
        
	public static void BackUp(){
		int connection = 0;	
		System.out.println("\n\t\tA ligar ao servidor, aguarde enquanto a ligacao e estabelecida...");
		do{
			try {
			h = (DropMusic_S_I) Naming.lookup("Drop");
			Connected ="Drop";
			break;
			}catch(Exception e1){
				try{
				h = (DropMusic_S_I) Naming.lookup("Drop_Backup");
				Connected = "Drop_Backup";
				break;
				}catch(Exception e2){
					connection++;
					try{
						Thread.sleep(1000);
					}catch(Exception e3){
						System.out.println("Problemas com a thread main: " + e3);
					}
				}
			}	
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
				case 0: System.exit(0);
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
				System.out.println("Username ja esta em uso, escolha outro");
				System.out.printf("Pretende:\n1.Tentar outra vez\n2.Fazer Login\n\n0.Exit");
				if(sc.nextInt()==2) Login();
				else if(sc.nextInt()==0) System.exit(0);
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
		while(true){
			System.out.printf("\nNome de utlizador: ");
			nome = sc.next();
			System.out.printf("Password: ");
			password = sc.next();
			try {
				if(h.CheckUser(nome, password,c)){
					System.out.println("Login efectuado com sucesso!");
					break;
				}
			} catch (Exception e) {
				System.out.println("Exception in Login: " + e);
			}
			System.out.println("Username ou password errados!");
			System.out.printf("Pretende:\n1.Tentar outra vez\n2.Registar\n\n0.Exit");
			if(sc.nextInt()==2) Registo();
			else if(sc.nextInt()==0) System.exit(0);
		}
		online = new User(nome,password);
		DropMusic();
	}
	
	public static void DropMusic(){			
		int opcao;
		boolean exit = false;
		System.out.println("\n\n\n\n\t\t >>DropMusic<<");
		try{
			h.CheckNotifications(online.GetNome(), c); 
			Thread.sleep(500);
		}catch(Exception c){
			System.out.println("Exception in Loading: "+c);
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
		Scanner sc = new Scanner(System.in);
		
		System.out.printf("\nNome:");
		search = sc.nextLine();
		try{
			String[] respostas = h.Find(search,escolha,c);
			if(respostas[0].compareTo("none")==0) System.out.println("Nada encontrado para: " + search);
			else{
				for(int possibilidades = 1; possibilidades<=respostas.length; possibilidades++){
					System.out.println(possibilidades + ". ->" + respostas[possibilidades-1]);
				}
				int numero = sc.nextInt();			
				String[] details = h.GetDetails(respostas[numero-1],escolha,c);
				if(details[0].compareTo("none")!=0){
					for(int i = 0; i<details.length; i+=2){
						System.out.println(details[i] + ": " + details[i+1]);
					}
				}
			}
		} catch (Exception re) {
		System.out.println("Exception in Pesquisar.Details(): " + re);
		}	
			
		if(escolha.compareTo("album")==0){
			InputStreamReader input = new InputStreamReader(System.in);
			BufferedReader reader = new BufferedReader(input);
			System.out.println("Prentende fazer um critica [y/n]?");
			try{
				search = reader.readLine();
				if(search.compareTo("y")==0){
					String critica;
					int pontuacao;
					System.out.printf("Pontuacao [0 a 10]: ");
					pontuacao = sc.nextInt();
					System.out.printf("\nComentario >>> ");
					while ((critica = reader.readLine()) != null){
						h.Write(online.GetNome(), pontuacao, critica,search);
						break;
					}
				}
			}catch (Exception re) {
				System.out.println("Exception in Pesquisa(): " + re);
			}
		}
			
		if(online.IsEditor()){
			System.out.printf("Pretende fazer alterações?[y/n]");
			search = sc.nextLine();
			if(search.compareTo("y")==0){
				String alterado, alteracao; 
				System.out.printf("Para sair escreva <exit>\n");
				while(true){
					System.out.printf("O que quer alterar?[Ex. artistas]: ");
					alterado = sc.nextLine().toLowerCase();
					if(alterado.compareTo("exit")==0) break;
					System.out.printf("\n>>> ");
					alteracao = sc.nextLine();
					//Comunicar com RMI para fazer alteracoes <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
				}				
			}
		}
	}

	public static void Playlist(){
		System.out.println("\n\n\n\n\t\t >>Consultar detalhes sobre álbum e sobre artista<<");
		Pesquisar("musica");
		//?
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
		try{
			h.GivePriv(online.IsEditor(), user, c);
			Thread.sleep(500);
		} catch (Exception re) {
			System.out.println("Exception in Function Consulta(): " + re);
		} 
		MainScreen();
	}
}