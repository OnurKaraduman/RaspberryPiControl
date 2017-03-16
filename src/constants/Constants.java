package constants;

import java.net.InetAddress;

public class Constants {

    //<editor-fold desc="motor pin numbers">
    public static int pinMotorLeft1 = 2;
    public static int pinMotorLeft2 = 0;
    public static int pinMotorRight1 = 4;
    public static int pinMotorRight2 = 5;
    //</editor-fold>
    
    public static String IMAGE_TYPE = ".png";
    public static int FRAME_WIDTH = 640;
    public static int FRAME_HEIGHT = 480;
    public static int H_MIN = 0;
    public static int H_MAX = 50;
    public static int S_MIN = 70;
    public static int S_MAX = 130;
    public static int V_MIN = 0;
    public static int V_MAX = 255;
    public static String errorCode = "HataVar!! ";
    public static int valueOfFirstMeter = 120;

    //webcam constants
    public static InetAddress IP_ADDRESS = null;
    public static int PORT_ADDRESS = 9999;
    public static int CLIENT_PORT = -1;
    public static int IMAGE_WIDTH = 400;
    public static int IMAGE_HEIGHT = 300;
    public static int AUTHORITY_ADMIN = 1;
    public static int AUTHORITY_GUEST = 2;

    // variables of command
    public static String COMMAND_STOP = "0";
    public static String COMMAND_RIGHT = "1";
    public static String COMMAND_LEFT = "2";
    public static String COMMAND_GO = "3";
    public static String COMMAND_BACK = "4";
    public static String COMMAND_START_VIDEO_STREAM = "";
    public static String COMMAND_STOP_CONNECTION = "0";

    //isaret hiyerarşisi
    /*
     /////// bolum cizgizi
     ::::::: iki nokta
     _ _ _ _ alttan tire
     ------- tire
     */
    public static String SPLIT_CHARACTER_LEVEL_1 = "/";
    public static String SPLIT_CHARACTER_LEVEL_2 = ":";
    public static String SPLIT_CHARACTER_LEVEL_3 = "_";
    public static String SPLIT_CHARACTER_LEVEL_4 = "-";

    public static int DISTANCE_TO_OBJECT = 400;

    private static boolean AUTO_GEAR = false;
    private static boolean FOLLOW_OBJECT = false;

    //<editor-fold desc="properties">
    public synchronized static boolean getAUTO_GEAR() {
        return AUTO_GEAR;
    }

    public synchronized static void setAUTO_GEAR(boolean value) {
        AUTO_GEAR = value;
    }

    public synchronized static boolean getFOLLOW_OBJECT() {
        return FOLLOW_OBJECT;
    }

    public synchronized static void setFOLLOW_OBJECT(boolean FOLLOW_OBJECT) {
        Constants.FOLLOW_OBJECT = FOLLOW_OBJECT;
    }
    //</editor-fold>

}
