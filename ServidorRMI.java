import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.io.*;


public class ServidorRMI extends UnicastRemoteObject implements DropMusic_S_I{

	public ServidorRMI() throws RemoteException {
		super();
	}

	public void NewUser(String s) throws RemoteException {
		System.out.println("> " + s);
	}
	
	public boolean RegistUser(String name, String password) throws RemoteException{
		//Ver se o username esta a ser usado
		return false;
	}
	public boolean CheckUser(String name, String password,  DropMusic_C_I c) throws RemoteException{
		//Ver se o user esta na BD
		return true;
	}
	public void Find(String name, String tipo, DropMusic_C_I c) throws RemoteException{
		//Receber da BD toda a informação relatica ao album, artista ou musica.
		String aux = new String("Temos esse " + tipo + " sim");
		try {
			c.Print(aux);
		} catch (Exception re) {
			System.out.println("Exception in Find(): " + re);
		} 
	}
	public void Write(String critica, String album) throws RemoteException{
		//find album
		//escreve critica
	}
	public void ShareMusic(String NAOSEI) throws RemoteException{
		
	}
	public void TransferMusic(String NAOSEI) throws RemoteException{
		
	}
	
	public void GivePriv(String username) throws RemoteException{
		//Receber da BD o ID do Client c com este username
		// c.ChangeUserToEditor()
	}

	// =======================================================

	public static void main(String args[]) {
		String a;

		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		try {
			ServidorRMI s = new ServidorRMI();
			Naming.bind("Drop", s);
			System.out.println("DropMusic RMI Server ready.");
			while (true) {
				
				}
		} catch (Exception re) {
			System.out.println("Exception in DropMusicImpl.main: " + re);
		} 
	}
}