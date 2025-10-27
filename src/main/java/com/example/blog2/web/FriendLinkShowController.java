package com.example.blog2.web;

import com.example.blog2.po.FriendLink;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.service.FriendLinkService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class FriendLinkShowController {
    private final FriendLinkService friendLinkService;

    public FriendLinkShowController(FriendLinkService friendLinkService) {
        this.friendLinkService = friendLinkService;
    }

    @GetMapping("/friendLinks")
    public Result<List<FriendLink>> friendLinks() {  // 明确指定泛型类型为 List<FriendLink>
        return new Result<>(true, StatusCode.OK, "获取友链列表成功", friendLinkService.listFriendLink());
    }
}
