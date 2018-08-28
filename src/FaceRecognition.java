import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

import java.io.File;
import java.io.FilenameFilter;
import com.googlecode.javacv.cpp.opencv_contrib.*;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.MatVector;
import static com.googlecode.javacv.cpp.opencv_contrib.*;

public class FaceRecognition {

	FaceRecognizer faceRecognizer;
	String mPath="";
	String imgname="";
	int count=0;
	private int mProb=999;
        File[] imageFiles;

	public FaceRecognition(String path){
		 mPath=path;
		 //faceRecognizer =  createLBPHFaceRecognizer(2,8,8,8,200);
                 faceRecognizer=createEigenFaceRecognizer();
		 
	}
	public void setmPath(String mPath) {
		this.mPath = mPath;
	}
	public void setImgName(String name)
	{
            imgname=name;
	}
        public boolean train() {
                File root = new File(mPath);
                FilenameFilter jpgFilter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return true;// name.equalsIgnoreCase(imgname);
                 }
                };
                imageFiles = root.listFiles(jpgFilter);
                MatVector images = new MatVector(imageFiles.length);
                int[] labels = new int[imageFiles.length];
                int counter = 0;
                IplImage img=null;
                IplImage grayImg;
                for (File image : imageFiles) {
                    String p = image.getAbsolutePath();
                    img = cvLoadImage(p);
                    grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);
                    cvCvtColor(img, grayImg, CV_BGR2GRAY);
                    images.put(counter, grayImg);
                    labels[counter] = counter+1;
                    counter++;
                }	
                System.out.println("path"+mPath+" number of images"+imageFiles.length);
                faceRecognizer.train(images, labels);
                return true;
            }
        
        public String predict(IplImage img) {
                IplImage grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);
                cvCvtColor(img, grayImg, CV_BGR2GRAY);
                int predictedLabel = faceRecognizer.predict(grayImg);
                if(predictedLabel!=-1)
                {
                    String name=imageFiles[predictedLabel-1].getName();
                    int index=name.indexOf(".jpg");
                    name=name.substring(0, index);
                    return name;
                }
                else
                {
                    return "Unknown person";
                }
        }

        public int getProb() {
                return mProb;
        }
}