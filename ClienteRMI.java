import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.io.*;


public class ClienteRMI extends UnicastRemoteObject implements DropMusic_C_I{
	
	private static User online;
	private static DropMusic_S_I h;
	private static ClienteRMI c;
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
			h = (DropMusic_S_I) Naming.lookup("Drop");
			c = new ClienteRMI();
			h.NewUser(args[0]);
		}catch (Exception e) {
			System.out.println("Exception in main: " + e);
		}
		MainScreen();
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
		String nome = new String();
		String password = new String();
		Scanner sc = new Scanner(System.in);
		while(true){
			System.out.printf("\nNome de utlizador (sem espaços ou caracteres especiais): ");
			nome = sc.next();
			System.out.printf("Password: ");
			password = sc.next();
			try {
				if(h.RegistUser(nome,password)){
					System.out.println("Registo efectuado com sucesso!");
					break;
				}
			} catch (Exception e) {
				System.out.println("Exception in main: " + e);
			}
			System.out.println("Username ja esta em uso, escolha outro");
			System.out.printf("Pretende:\n1.Tentar outra vez\n2.Fazer Login\n");
			if(sc.nextInt()==2) Login();
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
			System.out.printf("Pretende:\n1.Tentar outra vez\n2.Registar\n");
			if(sc.nextInt()==2) Registo();
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
			Thread.sleep(1000);
		}catch(Exception c){
			System.out.println("Exception in Loading: "+c);
		}
		Scanner sc = new Scanner(System.in);
		if(online.IsEditor()){
			System.out.println("\n1.Pesquisar Musicas\n2.Consultar detalhes\n3.Upload de músicas\n4.Partilhar músicas\n5.Donwload de músicas\n6.Dar previlégios\n0.Log Out");
			while(!exit){
				opcao = sc.nextInt();
				switch(opcao){
					case 1:	Pesquisar();
							break;
					case 2: Consultar();
							break;
					case 3: Upload();
							break;
					case 4: Partilhar();
							break;
					case 5: Donwload();
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
			System.out.println("\n1.Pesquisar Musicas\n2.Consultar detalhes\n3.Upload de músicas\n4.Partilhar músicas\n5.Donwload de músicas\n0.Log Out");
			opcao = sc.nextInt();
			while(!exit){
				switch(opcao){
					case 1: Pesquisar();
							break;
					case 2: Consultar();
							break;
					case 3: Upload();
							break;
					case 4: Partilhar();
							break;
					case 5: Donwload();
							break;
					case 0: exit = true;
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
		System.out.println("\n\n\t\t >>Pesquisar Musica<<");
		Scanner sc = new Scanner(System.in);
		System.out.println("\n1.Pesquisar por nome da musica\n2.Pesquisar por album\n3.Pesquisar por genero\n4.Pesquisar por artista\n0.Back");
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
			h.Find(search,escolha,c);
		} catch (Exception re) {
			System.out.println("Exception in Pesquisa(): " + re);
		}
		if(escolha.compareTo("album")==0){
			InputStreamReader input = new InputStreamReader(System.in);
			BufferedReader reader = new BufferedReader(input);
			System.out.println("Prentende fazer um critica [y/n]?");
			try{
				search = reader.readLine();
				if(search.compareTo("y")==0){
					String critica;
					System.out.printf(">>> ");
					while ((critica = reader.readLine()) != null){
						h.Write(online.GetNome(), critica,search);
						break;
					}
				}
			}catch (Exception re) {
				System.out.println("Exception in Pesquisa(): " + re);
			}
		}
	}
	
	
	public static void Consultar(){
		int opcao;
		boolean exit = false;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("\n\n\n\n\t\t >>Consultar detalhes sobre álbum e sobre artista<<");
		while(!exit){
			System.out.println("\n1.Detalhes de um album\n2.Detalhes de um artista\n0.Back");
			opcao = sc.nextInt();
			switch(opcao){
				case 1:	Consultar("album");
					break;
				case 2: Consultar("artista");
					break;
				case 0: DropMusic();
						break;
				default: System.out.println("Opção Inválida");
			}
		}
	}
	public static void Consultar(String escolha){
		String search;
		Scanner sc = new Scanner(System.in);
		System.out.printf("\nNome:");
		search = sc.nextLine(); //Só funciona para uma palavra, resolver!!
		try{
			h.Find(search,escolha,c);
		} catch (Exception re) {
			System.out.println("Exception in Consulta: " + re);
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
				}				
			}
		}
	}
	
	public static void Upload(){
		System.out.println("\n\n\t\t >>Upload de musica<<");
	}
	
	public static void Partilhar(){
		System.out.println("\n\n\t\t >>Partilha de musica<<");
	}
	
	public static void Donwload(){
		System.out.println("\n\n\t\t >>Download de musica<<");
	}
	
	public static void GivePrev(){
		Scanner sc = new Scanner(System.in);
		System.out.println("\n\n\t\t >>Dar previlegios<<");
		System.out.printf("\nNome do utilizador a dar previlegios: ");
		String user = sc.nextLine();
		try{
			h.GivePriv(user, c);
			Thread.sleep(1000);
		} catch (Exception re) {
			System.out.println("Exception in Function Consulta(): " + re);
		} 
		MainScreen();
	}
	
	

	
}