import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.io.*;

class CheckingConnection extends Thread {
	private static DropMusic_S_I teste;
	
	public CheckingConnection(){
		super("Connection");
	}
	public void Checking(){
		this.start();
		try{
			ClienteRMI.h = (DropMusic_S_I) Naming.lookup("Drop");
			ClienteRMI.h.NewUser(" ");
			this.join();
		}catch(Exception c2){
			System.out.println("Join error in Checking Thread: " + c2);
		}
	}
	
    public void run() {
		int counter = 0, counter2 = 0;
        while(true){				
			try{
				teste = (DropMusic_S_I) Naming.lookup("Drop");
				teste.NewUser(" ");
				counter++;
			}catch(Exception c){
				try{
					counter2++;
					Thread.sleep(500);
				}catch(Exception c3){
					System.out.println("Join error in Sleep: " + c3);
				}
			}
			if(counter != counter2) break;
		}
	}
}