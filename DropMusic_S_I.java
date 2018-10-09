import java.rmi.*;

public interface DropMusic_S_I extends Remote {
	public void print_on_server(String s) throws java.rmi.RemoteException;
	public void subscribe(String name, DropMusic_C_I client) throws RemoteException;
}