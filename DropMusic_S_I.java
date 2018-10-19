import java.rmi.*;

public interface DropMusic_S_I extends Remote {
	
	public void NewUser(String s) throws java.rmi.RemoteException;
	public void CheckNotifications(String username, DropMusic_C_I c) throws RemoteException;
	public boolean RegistUser(String name, String password) throws RemoteException;
	public boolean CheckUser(String username, String password,  DropMusic_C_I c) throws RemoteException;
	public void Find(String name, String tipo, DropMusic_C_I client) throws RemoteException;
	public void Write(String username, int pont, String critica, String album) throws RemoteException;
	public void ShareMusic(String username) throws RemoteException;
	public void TransferMusic(String username) throws RemoteException;
	public void UploadMusic(String username) throws RemoteException;
	public void GivePriv(boolean editor, String username, DropMusic_C_I c) throws RemoteException;
	
}