package com.example.blog2.web;

import com.example.blog2.po.PersonInfo;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.PersonInfoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin
public class PersonInfoShowController {
    private final PersonInfoService personInfoService;

    public PersonInfoShowController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    @GetMapping("/personInfos")
    public Result<List<PersonInfo>> personInfos() {
        return new Result<>(true, StatusCode.OK, "获取个人信息成功", personInfoService.listPersonInfo());
    }
}
