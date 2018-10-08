
import java.util.Scanner;

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

public class ClienteRMI{
	public static void main(String args[]) {
		/*
		try {
			Hello h = (Hello) Naming.lookup("rmi://localhost:7000/benfica");

			String message = h.sayHello();
			System.out.println("HelloClient: " + message);
		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
			e.printStackTrace();
		}
		*/
                int opcao = MainScreen();
                switch(opcao){
                    case 1: Login();
                            break;
                    case 2: Registo();
                            break;
                    default:
                            break;
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
            DropMusic(online);
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
            DropMusic(online);
        }
        
        public static void DropMusic(User online){
            System.out.println("\n\n\n\n\t\t >>DropMusic<<");
            Scanner sc = new Scanner(System.in);
            int opcao;
            if(online.GetTipo() == 0){
                System.out.println("\n1.Pesquisar Musicas\n2.Pesquisar artistas, álbuns e músicas\n3.Upload de músicas\n4.Partilhar músicas\n5.Donwload de músicas\n0.Log Out");
                while(true){
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
                        case 0: MainScreen();
                            break;
                        default: System.out.println("Opção Inválida");
                    }
                }
            }
            else{
                System.out.println("\n1.Pesquisar Musicas\n2.Pesquisar artistas, álbuns e músicas\n3.Upload de músicas\n4.Partilhar músicas\n5.Donwload de músicas\n6.Dar previlégios\n0.Log Out");
                while(true){
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
                        case 0: MainScreen();
                            break;
                        default: System.out.println("Opção Inválida");
                    }
                }
            }
        }
}