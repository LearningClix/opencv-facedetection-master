package com.opencv.springboot.controller;

import com.opencv.springboot.entity.FaceEntity;
import com.opencv.springboot.service.FaceDetectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class MainController {

    @Autowired
    private FaceDetectionService faceDetectionService;


    @ResponseBody
    @RequestMapping(value = "/detectFace/img", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] detectFaceImg(@RequestParam("file") MultipartFile imgFile) throws IOException {

       if ( !validateImg(imgFile))
       {
          return new byte[1];
       }

        return faceDetectionService.detectFace(imgFile).toImage();
    }

    @ResponseBody
    @RequestMapping(value = "/detectFace/json", method = RequestMethod.POST)
    public List<FaceEntity> detectFaceJson(@RequestParam("imgFile") MultipartFile imgFile) throws IOException {

        if ( !validateImg(imgFile))
        {
            return new ArrayList<>();
        }

        return faceDetectionService.detectFace(imgFile).toList();
    }

    private Boolean validateImg(MultipartFile img) {
        return img.getContentType().equals("image/jpeg");
    }


}
