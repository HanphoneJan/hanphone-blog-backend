package com.example.blog2.web;

import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.po.Type; // 导入分类实体类（根据实际类名调整）
import com.example.blog2.service.TypeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin
public class TypeShowController {
    private final TypeService typeService;

    public TypeShowController(TypeService typeService) {
        this.typeService = typeService;
    }

    // 明确指定泛型为 List<Type>（与 service 返回的集合类型一致）
    @GetMapping("/getTypeList")
    public Result<List<Type>> getTypeList() {
        return new Result<>(true, StatusCode.OK, "获取博客分类成功", typeService.listTypeTop(6));
    }

    // 明确指定泛型为 List<Type>
    @GetMapping("/getFullTypeList")
    public Result<List<Type>> getFullTypeList() {
        return new Result<>(true, StatusCode.OK, "获取博客全部分类成功", typeService.listType());
    }

}