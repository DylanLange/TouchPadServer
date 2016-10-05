import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TouchPadServer {

	private static final int PORT = 6969;
	
	public static void main(String[] args) throws Exception{
		Robot robot = new Robot();
		
		DatagramSocket serverSocket = new DatagramSocket(PORT);
		
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		System.out.println("Listening on port: " + PORT);
		while(true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			
			String sentence = new String( receivePacket.getData());
			System.out.println("RECEIVED: " + sentence);
			
			float dx = Float.parseFloat(sentence.split(":")[0]);
			float dy = Float.parseFloat(sentence.split(":")[1]);
			
			Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
			robot.mouseMove((int)(mouseCoord.getX() + dx), (int)(mouseCoord.getY() + dy));
			//System.out.println("X: " + dx + " | Y:" + dy);
			
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			
			String capitalizedSentence = sentence.toUpperCase();
			sendData = capitalizedSentence.getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
		
	}

}
