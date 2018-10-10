import java.rmi.*;

public interface DropMusic_C_I extends Remote{
	public void Print(String s) throws java.rmi.RemoteException;
	public void ChangeUserToEditor() throws RemoteException;
}