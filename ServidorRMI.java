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
		String aux = new String("Temos esse " + tipo + " sim");
		try {
			c.Print(aux);
		} catch (Exception re) {
			System.out.println("Exception in DropMusicImpl.main: " + re);
		} 
	}
	public void Get(String name) throws RemoteException{
		
	}
	public void Write(String critica) throws RemoteException{
		
	}
	public void ShareMusic(String NAOSEI) throws RemoteException{
		
	}
	public void TransferMusic(String NAOSEI) throws RemoteException{
		
	}
	
	public void GivePriv(String username) throws RemoteException{
		
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