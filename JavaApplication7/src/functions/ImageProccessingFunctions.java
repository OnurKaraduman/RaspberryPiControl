package functions;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import constants.*;

public class ImageProccessingFunctions {

    //<editor-fold desc="methods">
    public BufferedImage matToBufferedImage(Mat mat) throws IOException {
        MatOfByte byteMat = new MatOfByte();
        Highgui.imencode(Constants.IMAGE_TYPE, mat, byteMat);
        byte[] bytes = byteMat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage bufImage = ImageIO.read(in);
        return bufImage;
    }

    public Mat morphOps(Mat threshold) {
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(5, 5));

        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(5, 5));

        Imgproc.erode(threshold, threshold, erodeElement);
        Imgproc.erode(threshold, threshold, erodeElement);

        Imgproc.dilate(threshold, threshold, dilateElement);
        Imgproc.dilate(threshold, threshold, dilateElement);

        return threshold;
    }
        //</editor-fold>
}
