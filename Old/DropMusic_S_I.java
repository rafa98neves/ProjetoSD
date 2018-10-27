import java.rmi.*;

public interface DropMusic_S_I extends Remote {
	
	public void ping() throws java.rmi.RemoteException;
	public void NewUser(DropMusic_C_I c, String username) throws java.rmi.RemoteException;
	
	public void UserQuit(DropMusic_C_I c, String username) throws RemoteException;
	public void CheckNotifications(String username, DropMusic_C_I c) throws RemoteException;
	public boolean RegistUser(String name, String password) throws RemoteException;
	public boolean CheckUser(String username, String password,  DropMusic_C_I c) throws RemoteException;
	public String[] Find(String name, String tipo, DropMusic_C_I client) throws RemoteException;
	public String[] GetDetails(String name, String tipo, DropMusic_C_I c) throws RemoteException;
	public boolean Write(String username, int pont, String critica, String album) throws RemoteException;
	public void AddRemoveSomething(String tipo, String nome, String nome_musica, boolean remove) throws RemoteException;
	public String[] GetGeneros() throws RemoteException;
	public void AddGenero(String genero) throws RemoteException;
	public void AlterarDados(String username, String tipo, String alvo, String alteracao, String alterado) throws RemoteException;
	public void ShareMusic(String username) throws RemoteException;
	public String[] TransferMusic(String username) throws RemoteException;
	public boolean GivePriv(boolean editor, String username, DropMusic_C_I c) throws RemoteException;
}