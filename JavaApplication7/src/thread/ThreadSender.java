package thread;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.zip.Deflater;

import javax.imageio.ImageIO;
import model.Client;

public class ThreadSender extends Thread {

    //<editor-fold desc="members">
    private BufferedImage bufImageForSend;
    private DatagramSocket socket;
    private Deflater deflater;
    private ArrayBlockingQueue<Client> clientList;
    //</editor-fold>

    //<editor-fold desc="constructor">
    public ThreadSender(DatagramSocket socket) {
        // TODO Auto-generated constructor stub
        this.socket = socket;
        deflater = new Deflater();
        bufImageForSend = null;
    }
    //</editor-fold>

    //<editor-fold desc="run">
    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
		// video Streaming

        // bufferedImage null degilse islemmleri yap
        if (bufImageForSend != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bufImageForSend, "jpg", bos);
            } catch (Exception e) {
                // TODO: handle exception
                // image write exception ->log dosyasina kaydet
            }
            byte[] byteImage = bos.toByteArray();

            // byte[] byteImage = compress(byteImage2);
//			System.out.println("Total image byte length: " + byteImage.length);
            if (clientList != null) {
//				if (byteImage.length <= 65500) {
//					sendAllClient(byteImage);
//				}
                sendAllClient(byteImage);
            }
        }
    }

    //</editor-fold>
    //<editor-fold desc="methods">
    public void setBufImageForSend(BufferedImage bufImageForSend) {
        this.bufImageForSend = bufImageForSend;
    }

    public void setClientList(ArrayBlockingQueue<Client> clientList) {
        this.clientList = clientList;
    }

    public void sendAllClient(byte[] sendByteImage) {
        for (Client client : clientList) {
            DatagramPacket packet = new DatagramPacket(sendByteImage,
                    sendByteImage.length, client.getClientIPAddress(),
                    client.getClientPort());
            try {
                socket.send(packet);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    //</editor-fold>
}
