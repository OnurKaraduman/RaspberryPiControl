/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import constants.Constants;
import functions.ImageProccessingFunctions;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import thread.ThreadReciever;
import thread.ThreadSender;

/**
 *
 * @author Onur
 */
public class Test2 {

    //<editor-fold desc="members">
    public static DatagramSocket socket;
    private static ThreadSender threadSender;

    static int distanceToObject = 0;
    static Mat cameraFeed;
    static ThreadReciever threadReciever;
    //</editor-fold>

    //<editor-fold desc="main">
    public static void main(String args[]) throws InterruptedException, SocketException {
        ImageProccessingFunctions imgFunction = new ImageProccessingFunctions();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        cameraFeed = new Mat();
        Mat matHSV = new Mat();
        Mat matThresHold = new Mat();
        Mat matSender = new Mat();
        VideoCapture camera = new VideoCapture(0);
        camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1080);
        camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 910);
        if (camera.isOpened()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                System.out.println(Constants.errorCode + e);
            }
        }
        socket = new DatagramSocket(Constants.PORT_ADDRESS);
        threadSender = new ThreadSender(socket);
        threadReciever = new ThreadReciever(socket);
        /*
         waiting until client accepted
         */
        while (threadReciever.getClientList().size() == 0) {
            Thread.sleep(100);
        }
        while (true) {
            camera.read(cameraFeed);
            //<editor-fold desc="Auto_control active">
            if (Constants.getAUTO_GEAR()) {
                Imgproc.cvtColor(cameraFeed, matHSV, Imgproc.COLOR_BGR2HSV);
                Core.inRange(matHSV, new Scalar(Constants.H_MIN, Constants.S_MIN, Constants.V_MIN),
                        new Scalar(Constants.H_MAX, Constants.S_MAX, Constants.V_MAX),
                        matThresHold);
                matThresHold = imgFunction.morphOps(matThresHold);
                dontCrash(trackFilteredObject(matThresHold));
            } //</editor-fold>
            //<editor-fold desc="Follow Object Active">
            else if (Constants.getFOLLOW_OBJECT()) {
                Imgproc.cvtColor(cameraFeed, matHSV, Imgproc.COLOR_BGR2HSV);
                Core.inRange(matHSV, new Scalar(Constants.H_MIN, Constants.S_MIN, Constants.V_MIN),
                        new Scalar(Constants.H_MAX, Constants.S_MAX, Constants.V_MAX),
                        matThresHold);
                matThresHold = imgFunction.morphOps(matThresHold);
                followObject(trackFilteredObject(matThresHold));
            }
            //</editor-fold>
            //<editor-fold desc="Image Sending">
            Imgproc.resize(cameraFeed, matSender, new Size(
                    Constants.IMAGE_WIDTH, Constants.IMAGE_HEIGHT));
            BufferedImage bufImage = null;
            try {
                bufImage = imgFunction.matToBufferedImage(matSender);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            threadSender.setBufImageForSend(bufImage);
            threadSender.setClientList(threadReciever.getClientList());
            threadSender.run();
            //</editor-fold>
        }
    }
    //</editor-fold>

    public static List<MatOfPoint> trackFilteredObject(Mat threshold) {
        // Vector<Vector<Point>> contours;
        Mat threshold2 = new Mat();
        threshold.copyTo(threshold2);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        // Vector<MatOfInt4> hierarchy;
        Mat hierarchy = new Mat();
        Imgproc.findContours(threshold2, contours, hierarchy,
                Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
//            System.out.println("goruntu yok----<");

//        if (contours.size() > 0) {
//            for (int i = 0; i < 3; i++) {
//                Rect rect = Imgproc.boundingRect(contours.get(contours.size() - 1 - i));
//                drawObject(new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height));
//
//            }
//        }
//        for (MatOfPoint contour : contours) {
////            if (counterOfCountour > 2) {
////                break;
////            }
//
//            Rect rect = Imgproc.boundingRect(contour);
//            drawObject(new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height));
////            counterOfCountour++;
//        }
//        if (contours.size() > 0) {
//            Rect rect = Imgproc.boundingRect(contours.get(contours.size() - 1));
//            drawObject(new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height));
//            System.out.println("object var");
//            System.out.println("x: " + rect.x + "  y: " + rect.y);
//        }
        // Imgproc.drawContours(cameraFeed, contours, -1, new Scalar(0, 0, 0),
        // 2);
//        for (MatOfPoint matOfPoint : contours) {
//
//            Rect rect = Imgproc.boundingRect(matOfPoint);
//
//            // System.out.println("genislik: "+rect.width+"  yukseklik: "+rect.height);
////            drawObject(new Point(rect.x, rect.y), new Point(
////                    rect.x + rect.width, rect.y + rect.height));
//            int dist = cameraFeed.height() - (rect.y + rect.height);
//            if (distanceToObject == 0) {
//                distanceToObject = dist;
//                // System.out.println("distance------ " + dist);
//            } else {
//                if ((distanceToObject == dist)
//                        || ((distanceToObject - 1) == dist)
//                        || (distanceToObject == (dist - 1))) {
//
//                } else {
//                    distanceToObject = dist;
//                    // System.out.println("distance------ " + dist);
//                }
//            }
//
//        }
    }

    public static void dontCrash(List<MatOfPoint> contours) {
        if (contours.size() > 0) {
            Rect rect = Imgproc.boundingRect(contours.get(contours.size() - 1));
            if ((cameraFeed.height() - (rect.y + rect.height)) < Constants.DISTANCE_TO_OBJECT) {
                threadReciever.getPinController().right();
            } else {
//                System.out.println("Yeterince yaklasmadik hala--->");
                threadReciever.getPinController().go();
            }
            drawObject(new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height));

        } else {
            threadReciever.getPinController().go();
        }
    }

    public static void followObject(List<MatOfPoint> contours) {
        if (contours.size() > 0) {
            Rect rect = Imgproc.boundingRect(contours.get(contours.size() - 1));
            int distanceVertical = cameraFeed.height() - rect.y;
            int distanceHorizontalToRight = cameraFeed.width() - rect.x;
            int distanceHorizontalToLeft = rect.x;
            if (distanceVertical > 500) {
                threadReciever.getPinController().go();
            }
            if (distanceHorizontalToRight < 200) {
                threadReciever.getPinController().right();
            }
            if (distanceHorizontalToLeft < 200) {
                threadReciever.getPinController().left();
            }
            drawObject(new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height));
        }
    }

    public static void drawObject(Point p1, Point p2) {

        Core.circle(cameraFeed, p1, 4, new Scalar(0, 0, 0));
//        Core.rectangle(cameraFeed, p1, p2, new Scalar(0, 0, 0));
    }
}
