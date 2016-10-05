import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class TouchPadServer {

	private static final int PORT = 6969;
	private static final int SCROLL_DIVISOR = 2;
	
	public static void main(String[] args) throws IOException, AWTException{
		Robot robot = new Robot();
		
		DatagramSocket serverSocket = new DatagramSocket(PORT);
		
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		int scrollCount = 0;
		
		System.out.println("Listening on port: " + PORT);
		while(true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			
			String sentence = new String( receivePacket.getData());
			//System.out.println("RECEIVED: " + sentence);
			
			if(sentence.startsWith("MOVE") || sentence.startsWith("SCROLL")){
				float dx = 0;
				float dy = 0;
				
				if(sentence.startsWith("MOVE")){
					
					
					try{
						dx = Float.parseFloat(sentence.split("MOVE")[1].split(":")[0]);
						dy= Float.parseFloat(sentence.split("MOVE")[1].split(":")[1]);
					} catch (NumberFormatException e){
						e.printStackTrace();
						continue;
					}
					
					Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
					robot.mouseMove((int)(mouseCoord.getX() + dx), (int)(mouseCoord.getY() + dy));
					
					
				} else if(sentence.startsWith("SCROLL")){
					
					
					if(scrollCount > SCROLL_DIVISOR){
						try{
							dx = Float.parseFloat(sentence.split("SCROLL")[1].split(":")[0]);
							dy= Float.parseFloat(sentence.split("SCROLL")[1].split(":")[1]);
						} catch (NumberFormatException e){
							e.printStackTrace();
							continue;
						}
						
						Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
						if((int)(dy) > 0){//if positive scroll
							robot.mouseWheel(-1);
							System.out.println("SCROLLING DOWN");
						} else {
							System.out.println("SCROLLING UP");
							robot.mouseWheel(1);
						}
						scrollCount = 0;
					}
					scrollCount++;
					
					
				}
			} else if(sentence.startsWith("CLICK")){
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}
			//System.out.println("X: " + dx + " | Y:" + dy);
			
			/*InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			
			String capitalizedSentence = sentence.toUpperCase();
			sendData = capitalizedSentence.getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);*/
		}
		
	}

}
