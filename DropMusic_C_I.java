import java.rmi.*;

public interface DropMusic_C_I extends Remote{
	public void print_on_client(String s) throws java.rmi.RemoteException;
}