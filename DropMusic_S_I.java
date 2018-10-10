import java.rmi.*;

public interface DropMusic_S_I extends Remote {
	public void NewUser(String s) throws java.rmi.RemoteException;
	public boolean CheckUser(String name, String password) throws RemoteException;
	public boolean RegistUser(String name, String password) throws RemoteException;
	public void FindMusic(String name) throws RemoteException;
	public void Get(String name, DropMusic_C_I user) throws RemoteException;
	public void Write(String critica) throws RemoteException;
	public void GivePriv(DropMusic_C_I user) throws RemoteException;
	public void ShareMusic(String NAOSEI) throws RemoteException;
	public void TransferMusic(String NAOSEI) throws RemoteException;
}