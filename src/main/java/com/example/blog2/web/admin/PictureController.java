package com.example.blog2.web.admin;


import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.UploadService;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


//这个类暂时没有用
@RestController
@CrossOrigin
@RequestMapping("/upload")
public class PictureController {
    private final UploadService uploadService;
    private static final Log log = LogFactory.getLog(PictureController.class);
    String path = null;
    String finalUrl=null;

    public PictureController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(value = "/project")
    @ResponseBody
    public Result<String> uploadProject(@RequestParam("file") MultipartFile file) {

    path ="project/";
    finalUrl = uploadService.upload(file,path);
        log.info("上传成功");
        return new Result<>(true, StatusCode.OK,"上传图片成功",finalUrl);
    }


    @PostMapping(value = "/essays")
    @ResponseBody
    public Result<String> uploadEssays(@RequestParam("file") MultipartFile file) {
        path ="essays/";
        finalUrl = uploadService.upload(file,path);
        log.info("上传成功");
        return new Result<>(true, StatusCode.OK,"上传图片成功",finalUrl);
    }

    @PostMapping(value = "/pictures")
    @ResponseBody
    public Result<String> uploadPicture(@RequestParam("file") MultipartFile file) {
        path ="pictures/";
        finalUrl = uploadService.upload(file,path);
        log.info("上传成功");
        return new Result<>(true, StatusCode.OK,"上传图片成功",finalUrl);
    }

    @PostMapping(value = "/user")
    @ResponseBody
    public Result<String> uploadUser(@RequestParam("file") MultipartFile file) {
        path ="user/";
        finalUrl = uploadService.upload(file,path);
        log.info("上传成功");
        return new Result<>(true, StatusCode.OK, "上传图片成功", finalUrl);
    }

    @PostMapping(value = "/type")
    @ResponseBody
    public Result<String> uploadType(@RequestParam("file") MultipartFile file) {
        path ="type/";
        finalUrl = uploadService.upload(file,path);
        log.info("上传成功");
        return new Result<>(true, StatusCode.OK,"上传图片成功",finalUrl);
    }

    @PostMapping(value = "/blogs")
    @ResponseBody
    public Result<String> uploadBlogs(@RequestParam("file") MultipartFile file) {
        path ="blogs/";
        finalUrl = uploadService.upload(file,path);
        log.info("上传成功");
        return new Result<>(true, StatusCode.OK,"上传图片成功",finalUrl);
    }







}
