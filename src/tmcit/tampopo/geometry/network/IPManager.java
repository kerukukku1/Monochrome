package tmcit.tampopo.geometry.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IPManager {

	private static Thread receiveThread;
	private String ipAddress,broadcastAddress;
	private int PORT = 10001;
	private int PORT2 = 10002;
	private List<String> allReceiveIP;

	public IPManager(){
		init();
	}

	private void init(){
		this.allReceiveIP = new ArrayList<String>();
		try {
			this.ipAddress = getIPAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.broadcastAddress = getBroadcastAddress();
	}

	public void exec(){
		try {
			System.out.println(this.broadcastAddress);
			makeReceiver();
			send(this.ipAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getAllRecieveIP(){
		return this.allReceiveIP;
	}

	private void makeReceiver(){
		if(receiveThread != null){
			receiveThread.stop();
			receiveThread = null;
		}
		receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                	DatagramSocket dgSocket = new DatagramSocket(PORT2);

                    byte buffer[] = new byte[1024];
                    DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);

                    while (true) {
                    	System.out.println("WAIT!");
                        dgSocket.receive(packet);
                        System.out.println("GET!");
                        System.out.print (new String(packet.getData(),
                                        0, packet.getLength()));
                        allReceiveIP.add(new String(packet.getData(),
                                        0, packet.getLength()));
                    }                } catch (IOException e) {
                }
            }
        });
		receiveThread.start();

	}

	private void send(String msg) throws Exception{
		DatagramChannel sendCh = DatagramChannel.open();
        sendCh.socket().setBroadcast(true);
        ByteBuffer sendBuf = ByteBuffer.wrap(msg.getBytes("UTF-8"));
        InetSocketAddress portISA = new InetSocketAddress(broadcastAddress, PORT);
        sendCh.send(sendBuf, portISA);
        System.out.println("send");
        sendBuf.clear();
        sendCh.close();
        System.out.println("sendCh close");
	}

	private static final String getBroadcastAddress() {
        try {
            for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
                NetworkInterface ni = niEnum.nextElement();
                if (!ni.isLoopback()) {
                    for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                        if (interfaceAddress != null) {
                            InetAddress broadcastAddress = interfaceAddress.getBroadcast();
                            if (broadcastAddress != null) {
                                return broadcastAddress.toString().substring(1);
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            // ignore;
        }
        return null;
    }

	private static final String getIPAddress() throws Exception{
		return InetAddress.getLocalHost().getHostAddress();
	}

}
