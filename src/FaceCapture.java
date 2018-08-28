/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author jaydutt
 */

public class FaceCapture implements Runnable
{
    String path="C:\\Users\\jaydutt\\ui\\gallery";
    JLabel label;
    JLabel cropImage;
    Thread t;
    volatile boolean flag; 
    static CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad("C:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml"));
    FaceRecognition recognize;
    
    public FaceCapture(JLabel temp,JLabel crop)
    {
        recognize=new FaceRecognition(path);
        recognize.train();
        label=temp;
        cropImage=crop;
        start();
    }
    
    public void start()
    {
        flag=true;
        t=new Thread(this);
        t.start();
    }

    public void stop()
    {
        flag=false;
    }
    
    @Override
    public void run() {
        CvCapture capture=opencv_highgui.cvCreateCameraCapture(0);
        
        IplImage img=opencv_highgui.cvQueryFrame(capture);
        
        while((img=opencv_highgui.cvQueryFrame(capture))!=null && flag )
        {
            detect(img);
        }
    }
    
    /**
     *
     * @param src
     * @return
     */
    public void detect(IplImage src)
    {
        BufferedImage bimg=src.getBufferedImage();
        Graphics2D graphics=(Graphics2D) bimg.getGraphics();
        graphics.setPaint(Color.red);
        graphics.setFont(new Font("Arial Black", Font.BOLD, 20));
                        
                        
		CvMemStorage storage = CvMemStorage.create();
		CvSeq sign = cvHaarDetectObjects(
				src,
				cascade,
				storage,
				1.5,
				3,
				CV_HAAR_DO_CANNY_PRUNING);
		cvClearMemStorage(storage);
		int total_Faces = sign.total();		
		for(int i = 0; i < total_Faces; i++){
			CvRect r = new CvRect(cvGetSeqElem(sign, i));
                        
                        BufferedImage img=bimg.getSubimage(r.x(),r.y(),r.width(),r.height());
                        
                        IplImage ip=IplImage.createFrom(img);
                        String name=recognize.predict(ip);
                        System.out.println(name);
                        
                                
                        graphics.drawString(name, r.x()-50, r.y()-50);
                        graphics.drawRect(r.x(), r.y(), r.width(), r.height());
                        ImageIcon icon=new ImageIcon(ip.getBufferedImage());
                        cropImage.setIcon(icon);
                }  
                label.setIcon(new ImageIcon(bimg));
	}
   
}