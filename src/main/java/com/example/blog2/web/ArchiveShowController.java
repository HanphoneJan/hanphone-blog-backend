package com.example.blog2.web;

import com.example.blog2.po.Blog;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.BlogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ArchiveShowController {

    private final BlogService blogService;

    public ArchiveShowController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/archiveBlog")
    public Result<Map<String, List<Blog>>> archives(){
        return new Result<>(true, StatusCode.OK, "查询博客列表成功", blogService.archiveBlog());
    }

    @GetMapping("/countBlog")
    public Result<Long> count(){
        return new Result<>(true, StatusCode.OK, "查询博客列表成功", blogService.countBlog());
    }
}
