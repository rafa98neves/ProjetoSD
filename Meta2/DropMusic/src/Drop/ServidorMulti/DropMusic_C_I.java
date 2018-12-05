package Drop.ServidorMulti;

import java.rmi.*;

/**
 * Description: Funções que o Servidor RMI pode chamar no User
 *
 * @param
 * @return
 */
public interface DropMusic_C_I extends Remote{
	public void Print(String s) throws RemoteException;
	public void ChangeUserToEditor(boolean change) throws RemoteException;
	public void ping() throws RemoteException;
}