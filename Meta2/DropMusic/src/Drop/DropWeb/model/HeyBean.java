package Drop.DropWeb.model;

import java.util.ArrayList;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import Drop.ServidorRMI.DropMusic_S_I;

public class HeyBean {
	private DropMusic_S_I server;
	private String username; // username and password supplied by the user
	private String password;

	public HeyBean() {
		try {
			server = (DropMusic_S_I) Naming.lookup("Drop1");
		}
		catch(NotBoundException|MalformedURLException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
