package com.example.blog2.web;

import com.example.blog2.po.Project;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.ProjectService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin
public class ProjectShowController {
    private final ProjectService projectService;

    public ProjectShowController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public Result<List<Project>> projects() {  // 明确指定泛型类型为 List<Project>
        return new Result<>(true, StatusCode.OK, "获取项目列表成功", projectService.listProject());
    }
}
