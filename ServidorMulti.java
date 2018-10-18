import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

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
				
				ManageRequest(packet);
				
				buffer = new byte[256];
				String message = "Packet do multi";
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
	
	public void ManageRequest(DatagramPacket packet){
		String message = new String(packet.getData(), 0, packet.getLength());
		System.out.println(message);
	}
	
	
}