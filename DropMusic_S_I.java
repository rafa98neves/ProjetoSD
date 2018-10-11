import java.rmi.*;

public interface DropMusic_S_I extends Remote {
	public void NewUser(String s) throws java.rmi.RemoteException;
	public boolean CheckUser(String name, String password,  DropMusic_C_I c) throws RemoteException;
	public boolean RegistUser(String name, String password) throws RemoteException;
	public void Find(String name, String tipo, DropMusic_C_I client) throws RemoteException;
	public void Write(String critica, String album) throws RemoteException;
	public void GivePriv(String username) throws RemoteException;
	public void ShareMusic(String NAOSEI) throws RemoteException;
	public void TransferMusic(String NAOSEI) throws RemoteException;
}