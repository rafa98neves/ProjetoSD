import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ServidorMulti extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT_RECEIVE = 4322;
	private int PORT_SEND = 4321;
	
    public static void main(String[] args) {
        ServidorMulti server = new ServidorMulti();
        server.start();
    }

    public ServidorMulti() {
        super("Server online");
    }

    public void run() {
        MulticastSocket socket = null;
        long counter = 0;

        System.out.println(this.getName() + " running...");
        try {
            socket = new MulticastSocket(PORT_RECEIVE);
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);
            while (true) {
				byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT_RECEIVE);			
				System.out.println("Bloqueado");
				socket.receive(packet);
			    System.out.println("Desbloqueado");
				
				String message = ManageRequest(packet);
				
				buffer = message.getBytes();
				
				packet = new DatagramPacket(buffer, buffer.length, group, PORT_SEND);
				socket.send(packet);
				System.out.println("Sent!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
	
	public String ManageRequest(DatagramPacket packet){
		String protocolo = new String(packet.getData(), 0, packet.getLength());
		String[] processar = protocolo.split(Pattern.quote(" ; "));
		ArrayList<String> processa = new ArrayList<String>();
		String[] aux;
		for(String s : processar){
                    aux = s.split(Pattern.quote(" ; "));
                    processa.add(aux[1]);
                    processa.add(aux[2]);
		}
		
		switch(processa.get(1)){
			case "registo":
				//Procurar na BD se processa[3] (username) já existe
				//if(existe) protocolo = "type | registo ; confirmation | false";
				//else protocolo = "type | registo ; confirmation | true"; e acrescenta na BD
				return protocolo;
				
			case "login":
				//Procurar na BD se processa[3] (username) se existe e se a password corresponde
				//if(existe) protocolo = "type | login ; confirmation | true";
				//else protocolo = "type | login ; confirmation | false";
				return protocolo;
				
			case "notifications":
				//Procurar na BD se processa[3] (username) tem notificações pendentes
				//if(existe) protocolo = "type | notifications ; notification_1 | blabla* ; notification_2 | blablabla* ; (...)" ;
				//else protocolo = "type | notifications ; notification_1 | noone";
				return protocolo;
				
			case "search":
				//Procurar na BD se processa[5] (tipo de pesquisa (album, genero,...) tem processa[3] (nome)
				//if(existe) protocolo = "type | search ; possibility_1 | zeca* ; possibility_2 | zecaaa*" ;
				//else protocolo = "type | search ; possibility_1 | noone";
				return protocolo;
				
			case "details":
				//Procurar na BD processa[3](nome)
				//protocolo = "type | details ; detail_1 | moreno* ; detail_2 | Nasceu a ..*" ;
				return protocolo;
				
			case "critic":
				//Procurar na BD processa[5](nome do album) e escrever critica
				//protocolo = "type | critic ; confirmation | true";
				return protocolo;
				
			case "privileges":
			
			
				return protocolo;
				
			case "share":
			
			
				return protocolo;
				
			case "upload":
			
			
				return protocolo;
				
			case "download":
			
			
				return protocolo;
			default: 
				protocolo = "type | error ; function | " + processa.get(1);
				return protocolo;	
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}