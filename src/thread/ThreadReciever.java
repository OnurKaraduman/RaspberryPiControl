package thread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import constants.Constants;
import constants.MessageConstant;
import gpio.IPINController;
import gpio.PINController;
import java.util.concurrent.ArrayBlockingQueue;
import model.Client;
import protocol.MessagePacket;

/*! 
 *  \brief     ThreadReciever
 *  \details   This class for accept client and recieving data
 *  \author    Onur Karaduman
 *  \date      01.03.2015
 */
public class ThreadReciever extends Thread {

    //<editor-fold desc="members">
	/*Socket defined*/
    private DatagramSocket socket;
    private ArrayBlockingQueue<Client> clientList;
    private int clientID = 0;
    private IPINController pinController;
        //</editor-fold>

    //<editor-fold desc="constructor">
    public ThreadReciever(DatagramSocket socket) {
        // TODO Auto-generated constructor stub
        this.socket = socket;
        pinController = new PINController();
        clientList = new ArrayBlockingQueue<Client>(100000);
        this.start();

    }
        //</editor-fold>

    //<editor-fold desc="properties">
    public IPINController getPinController() {
        return pinController;
    }

    //</editor-fold>
    //<editor-fold desc="run">
    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();

        while (true) {
            byte[] byteCommand = new byte[1024];
            DatagramPacket packet = new DatagramPacket(byteCommand,
                    byteCommand.length);
            try {
                socket.receive(packet);
                System.out.println(packet.getAddress());
                System.out.println(packet.getPort());
                /* client vertitaban�m�za kay�tl� m� sorgusu gibi dusunebiliriz*/
                // eger degilse yeni bir client olustururuz
                if (!controlClient(packet.getAddress())) {
                    System.out.println("client baglandi.");
                    Client client = new Client();
                    client.setClientID(clientID);
                    client.setClientIPAddress(packet.getAddress());
                    client.setClientPort(packet.getPort());
                    if (clientList.size() == 0) {
                        client.setAuthority(Constants.AUTHORITY_ADMIN);
                    } else {
                        client.setAuthority(Constants.AUTHORITY_GUEST);
                    }
                    clientList.add(client);
                    clientID++;

                }
                String command = new String(packet.getData());
                System.out.println(command.trim());
                controlCommand(command.trim());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("->recieving failed!! " + e.toString());
                e.printStackTrace();
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="methods">
	/*! \brief This method gets all client */
    public ArrayBlockingQueue<Client> getClientList() {
        return clientList;
    }

    public boolean controlClient(InetAddress clientIPAddress) {
        if (clientList.size() > 0) {
            for (Client client : clientList) {
                if (client.getClientIPAddress() == clientIPAddress) {
                    return true;
                }
            }
        }
        return false;
    }

    public void controlCommand(String command) {
        MessagePacket mPacket = new MessagePacket();
        if (mPacket.toClass(command)) {
            if (mPacket.getCode().equals(MessageConstant.MESSAGE_COMMAND)) {
                //<editor-fold desc="COMMAND">
                if (mPacket.getContent().equals(Constants.COMMAND_GO)) {
                    pinController.go();
                } else if (mPacket.getContent().equals(Constants.COMMAND_LEFT)) {
                    pinController.left();
                } else if (mPacket.getContent().equals(Constants.COMMAND_RIGHT)) {
                    pinController.right();
                } else if (mPacket.getContent().equals(Constants.COMMAND_STOP)) {
                    pinController.stop();
                } else if (mPacket.getContent().equals(Constants.COMMAND_BACK)) {
                    pinController.back();
                }
                //</editor-fold>
                //<editor-fold desc="SPEED">
            } else if (mPacket.getCode().equals(MessageConstant.MESSAGE_SPEED)) {
                int incomingSpeed = 0;
                try {
                    incomingSpeed = Integer.parseInt(mPacket.getContent());

                } catch (Exception e) {
                    System.out.println("<---Error string can not cast to integer--->" + e);
                }
                try {
                    pinController.setSpeed(incomingSpeed);
                } catch (Exception e) {
                    System.out.println("<-----Error pincontroller.setSpeed-----> " + e);
                }
                //</editor-fold>
                //<editor-fold desc="GEAR">
            } else if (mPacket.getCode().equals(MessageConstant.MESSAGE_GEAR)) {
                try {
                    boolean autoGear = Boolean.parseBoolean(mPacket.getContent());
                    System.out.println(autoGear);
                    Constants.setAUTO_GEAR(autoGear);
                } catch (Exception e) {
                    System.out.println("<---Error string can not cast to boolean--->");
                }
            } //</editor-fold>
                //<editor-fold desc="COLOR">
            else if (mPacket.getCode().equals(MessageConstant.COLOR_H_MIN_VALUE)) {
                try {
                    Constants.H_MIN = Integer.parseInt(mPacket.getContent());
                } catch (Exception e) {
                    System.out.println("cannot parse to int h_min" + e);
                }

            } else if (mPacket.getCode().equals(MessageConstant.COLOR_H_MAX_VALUE)) {
                try {
                    Constants.H_MAX = Integer.parseInt(mPacket.getContent());
                } catch (Exception e) {
                    System.out.println("cannot parse to int h_max" + e);
                }

            } else if (mPacket.getCode().equals(MessageConstant.COLOR_S_MIN_VALUE)) {
                try {
                    Constants.S_MIN = Integer.parseInt(mPacket.getContent());
                } catch (Exception e) {
                    System.out.println("cannot parse to int s_min" + e);
                }
            } else if (mPacket.getCode().equals(MessageConstant.COLOR_S_MAX_VALUE)) {
                try {
                    Constants.S_MAX = Integer.parseInt(mPacket.getContent());
                } catch (Exception e) {
                    System.out.println("cannot parse to int s_max" + e);
                }
            } else if (mPacket.getCode().equals(MessageConstant.COLOR_V_MIN_VALUE)) {
                try {
                    Constants.V_MIN = Integer.parseInt(mPacket.getContent());
                } catch (Exception e) {
                    System.out.println("cannot parse to int v_min" + e);
                }
            } else if (mPacket.getCode().equals(MessageConstant.COLOR_V_MAX_VALUE)) {
                try {
                    Constants.V_MAX = Integer.parseInt(mPacket.getContent());
                } catch (Exception e) {
                    System.out.println("cannot parse to int v_max" + e);
                }
            }
            //</editor-fold>
        } else {
            System.out.println("Incoming message cannot recognized");
        }

    }

    //</editor-fold>
}
