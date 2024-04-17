package com.opencv.springboot.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencv.springboot.entity.FaceEntity;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FaceDetectionService {


    private Resource faceResource = new ClassPathResource("haarcascades/haarcascade_frontalface_alt.xml");

    private List<FaceEntity> faceDimention;
    private Mat img;

    public FaceDetectionService detectFace(MultipartFile imgFile) throws IOException {

        faceDimention=new ArrayList<>();
        MatOfRect faceDetections = new MatOfRect();
        CascadeClassifier faceDetector = new CascadeClassifier(faceResource.getFile().getAbsolutePath());


        img= Imgcodecs.imdecode(new MatOfByte(imgFile.getBytes()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        faceDetector.detectMultiScale(img, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        for (Rect rect : faceDetections.toArray()) {
            faceDimention.add(new FaceEntity(rect.x, rect.y, rect.width, rect.height, 0));
        }

        return this;
    }

    public List<FaceEntity> toList() {
        return faceDimention;
    }


    public byte[] toImage() {
        for (FaceEntity fc : faceDimention) {
            Imgproc.rectangle(img, new Point(fc.getX(), fc.getY()), new Point(fc.getX() + fc.getWidth(), fc.getY() + fc.getHeight()), new Scalar(0, 255, 0));
        }
        return mat2Image(img);
    }

    private byte[] mat2Image(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, buffer);
        return buffer.toArray();
    }
}
