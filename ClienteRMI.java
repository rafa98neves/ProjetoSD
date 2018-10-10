import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.io.*;


class User{
    private String nome;
    protected String password;
    private boolean editor;
    User(String nome,String password){
        this.nome = nome;
        this.password = password;
        this.editor = false;
    }
    public boolean IsEditor(){
        return editor;
    }
	public void ChangeUserToEditor(boolean change){
        this.editor = change;
    }
    public String GetNome(){
        return nome;
    }
}



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
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String nome = new String();
		String password = new String();
		Scanner sc = new Scanner(System.in);
		while(true){
			System.out.printf("\nNome de utlizador: ");
			nome = sc.nextLine();
			System.out.printf("Password: ");
			password = sc.nextLine();
			try {
				if(h.RegistUser(nome,password)){
					System.out.println("Registo efectuado com sucesso!");
					break;
				}
			} catch (Exception e) {
				System.out.println("Exception in main: " + e);
			}
			System.out.println("Username ja esta em uso, escolha outro");
		}
		online = new User(nome, password);
		DropMusic();
	}
	
	public static void Login(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String nome = new String();
		String password = new String();
		Scanner sc = new Scanner(System.in);
		while(true){
			System.out.printf("\nNome de utlizador: ");
			nome = sc.nextLine();
			System.out.printf("Password: ");
			password = sc.nextLine();
			online = new User(nome,password);
			try {
				if(h.CheckUser(nome, password,c)){
					System.out.println("Login efectuado com sucesso!");
					break;
				}
			} catch (Exception e) {
				System.out.println("Exception in Login: " + e);
			}
			System.out.println("Username ou password errados tente outra vez!");
		}
		// Procurar user na BD
		DropMusic();
	}
	
	public static void DropMusic(){
		int opcao;
		boolean exit = false;
		System.out.println("\n\n\n\n\t\t >>DropMusic<<");
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
			case 1:	
					break;
			case 2:
					break;
			case 3:
					break;
			case 4:
					break;
			case 0: DropMusic();
					break;
			default: System.out.println("Opção Inválida");
		}	
	}
	
	public static void Consultar(){
		int opcao;
		boolean exit = false;
		String search;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("\n\n\n\n\t\t >>Consultar detalhes sobre álbum e sobre artista<<");
		while(!exit){
			System.out.println("\n1.Detalhes de um album\n2.Detalhes de um artista\n0.Back");
			opcao = sc.nextInt();
			switch(opcao){
				case 1:	
					System.out.printf("\nNome do album:");
					search = sc.next(); //Só funciona para uma palavra, resolver!!
					try{
						h.Find(search,"album",c);
					} catch (Exception re) {
						System.out.println("Exception in Consulta: " + re);
					} 
					break;
				case 2: 
					System.out.printf("\nNome do artista:");
					search = sc.next();
					try{
						h.Find(search,"album",c);
					} catch (Exception re) {
						System.out.println("Exception in Consulta: " + re);
					} 
					break;
				case 0: DropMusic();
						break;
				default: System.out.println("Opção Inválida");
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
			h.GivePriv(user);
		} catch (Exception re) {
			System.out.println("Exception in Consulta: " + re);
		} 
	}
	
	

	
}