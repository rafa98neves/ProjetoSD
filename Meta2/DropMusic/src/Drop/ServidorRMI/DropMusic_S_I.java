package Drop.ServidorRMI;

import java.rmi.*;
import Drop.ServidorMulti.DropMusic_C_I;
/**
 * Description: Funções que o User pode chamar no Servidor RMI
 *
 * @param
 * @return
 */

public interface DropMusic_S_I extends Remote {
	
	public void ping() throws java.rmi.RemoteException;
	public void NewUser(DropMusic_C_I c, String username) throws java.rmi.RemoteException;
	
	public void UserQuit(DropMusic_C_I c, String username) throws RemoteException;
	public void CheckNotifications(String ID, DropMusic_C_I c) throws RemoteException;
	public String[] RegistUser(String name, String password) throws RemoteException;
	public void Criar(String ID, String tipo, String nome, String Info, String Info2) throws RemoteException;
	public String[] CheckUser(String username, String password) throws RemoteException;
	public String[] Find(String ID, String name, String tipo) throws RemoteException;
	public String[] GetDetails(String ID,String name, String tipo) throws RemoteException;
	public boolean Write(String ID,String username, int pont, String critica, String album) throws RemoteException;
	public void AddRemoveSomething(String ID,String tipo, String nome, String nome_musica, boolean remove) throws RemoteException;
	public String[] GetGeneros(String ID) throws RemoteException;
	public void AddGenero(String ID,String genero) throws RemoteException;
	public void AlterarDados(String ID,String username, String tipo, String alvo, String alteracao, String alterado) throws RemoteException;
	public void ShareMusic(String ID,String musica) throws RemoteException;
	public String[] ShareMusic(String ID) throws RemoteException;
	public String[] TransferMusic(String ID,String tipo, String musica) throws RemoteException;
	public boolean GivePriv(String ID,boolean editor, String username) throws RemoteException;
	public boolean AddToken(String ID, String token) throws  RemoteException;
	public String GetToken(String ID) throws RemoteException;
}