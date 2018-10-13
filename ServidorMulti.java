import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

public class ServidorMulti extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 4321;

    public static void main(String[] args) {
        ServidorMulti server = new ServidorMulti();
        server.start();
    }

    public ServidorMulti() {
        super("Server online" );
    }

    public void run() {
        MulticastSocket socket = null;
        long counter = 0;
        System.out.println(this.getName() + " running...");
        try {
            socket = new MulticastSocket();
            while (true) {
				byte[] buffer = new byte[256];
				InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
				try{
					socket.receive(packet);
				}catch(Exception c){
					System.out.println("Exception in send receive : " + c);
				}
				ManageRequest(packet);

				String message = "Packet " + counter++;
				buffer = message.getBytes();
				
				packet = new DatagramPacket(buffer, buffer.length);
				try{
					socket.send(packet);
				}catch(Exception c){
					System.out.println("Exception in send packet : " + c);
				}
                socket.send(packet);
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