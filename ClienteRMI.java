import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.io.*;


class User{
    private String nome;
    protected String password;
    // tipo 0 = Utilizador normal | tipo 1 = Editor | tipo 2 = admin
    private int tipo;
    User(String nome,String password, int tipo){
        this.nome = nome;
        this.password = password;
        this.tipo = tipo;
    }
    public int GetTipo(){
        return tipo;
    }
    public String GetNome(){
        return nome;
    }
}

public class ClienteRMI { /*extends UnicastRemoteObject implements DropMusic_C_I {
	
	ClienteRMI() throws RemoteException {
		super();
	}

	public void print_on_client(String s) throws RemoteException {
		System.out.println("> " + s);
	}
	*/
	public static void main(String args[]) {
            boolean exit=false;
            String a;

            System.getProperties().put("java.security.policy", "policy.all");
            System.setSecurityManager(new RMISecurityManager());

            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);
            try {
                    //User user = new User();
                    DropMusic_S_I h = (DropMusic_S_I) Naming.lookup("Drop");
                    ClienteRMI c = new ClienteRMI();
                    h.subscribe(args[0], (DropMusic_C_I) c);
                    System.out.println("Client sent subscription to server");
                    while (true) {
                            System.out.print("> ");
                            a = reader.readLine();
                            h.print_on_server(a);
                    }

            } catch (Exception e) {
                    System.out.println("Exception in main: " + e);
            }
            
            System.out.println("YO");
            
            while(!exit){
                int opcao = MainScreen();
                switch(opcao){
                        case 1: Login();
                                break;
                        case 2: Registo();
                                break;
                        case 0: exit = true;
                                break;
                        default:
                                break;
                }
            }
	}
        
	public static int MainScreen(){
		System.out.println("\t\t\t >>DropMusic<< ");
		System.out.println("\n");
		System.out.printf("Pretende:\n\t1.Login\n\t2.Registo\n\t0.Exit\n");
		Scanner sc = new Scanner(System.in);
		int opcao = sc.nextInt();
		while(opcao!=1 & opcao!=2 & opcao!=0){
                    System.out.println("Opção Inválida");
                    opcao = sc.nextInt();
		} 
		return opcao;
	}
	
	public static void Registo(){
		String nome = new String();
		String password = new String();
		Scanner sc = new Scanner(System.in);
		System.out.printf("\nNome de utlizador: ");
		nome = sc.nextLine();
		System.out.printf("Password: ");
		password = sc.nextLine();
		System.out.println("Registo efectuado com sucesso!");
		User online = new User(nome, password, 0);
		if(online.GetTipo()==0) DropMusicUser(online);
		else DropMusicEditor(online);
	}
	
	public static void Login(){
		String nome = new String();
		String password = new String();
		Scanner sc = new Scanner(System.in);
		System.out.printf("\nNome de utlizador: ");
		nome = sc.nextLine();
		System.out.printf("Password: ");
		password = sc.nextLine();
		System.out.println("Login efectuado com sucesso!");
		// Procurar user na BD
		User online = new User(nome, password, 0);
		if(online.GetTipo()==0) DropMusicUser(online);
		else DropMusicEditor(online);
	}
	
	public static void DropMusicUser(User online){
            int opcao;
            boolean exit = false;
            System.out.println("\n\n\n\n\t\t >>DropMusic<<");
            Scanner sc = new Scanner(System.in);
            System.out.println("\n1.Pesquisar Musicas\n2.Pesquisar artistas, álbuns e músicas\n3.Upload de músicas\n4.Partilhar músicas\n5.Donwload de músicas\n0.Log Out");
            opcao = sc.nextInt();
            while(!exit){
                switch(opcao){
                    case 1:
                            break;
                    case 2:
                            break;
                    case 3:
                            break;
                    case 4:
                            break;
                    case 5:
                            break;
                    case 0: exit = true;
                            break;
                    default: System.out.println("Opção Inválida");
                }
            }
	}
	
	public static void DropMusicEditor(User online){
            int opcao;
            boolean exit = false;
            System.out.println("\n\n\n\n\t\t >>DropMusic<<");
            Scanner sc = new Scanner(System.in);
            System.out.println("\n1.Pesquisar Musicas\n2.Pesquisar artistas, álbuns e músicas\n3.Upload de músicas\n4.Partilhar músicas\n5.Donwload de músicas\n6.Dar previlégios\n0.Log Out");
            while(!exit){
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
                    case 5:
                            break;
                    case 6:
                            break;    
                    case 0: exit=true;
                            break;
                    default: System.out.println("Opção Inválida");
                }
            }
	}
        
        
        
        
        
}