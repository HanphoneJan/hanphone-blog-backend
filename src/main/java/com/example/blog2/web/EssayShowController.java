package com.example.blog2.web;

import com.example.blog2.po.Essay;
import com.example.blog2.po.EssayComment;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.po.User;
import com.example.blog2.service.EssayCommentService;
import com.example.blog2.service.EssayService;
import com.example.blog2.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class EssayShowController {
    private final EssayService essayService;
    private final UserService userService;
    private final EssayCommentService essayCommentService;

    public EssayShowController(EssayService essayService, UserService userService, EssayCommentService essayCommentService) {
        this.essayService = essayService;
        this.userService = userService;
        this.essayCommentService = essayCommentService;
    }

    @GetMapping("/essays")
    public Result<List<Essay>> essays(@RequestParam(required = false) Long userId) {
        return new Result<>(true, StatusCode.OK, "获取随笔列表成功", essayService.listEssay(userId));
    }

    /**
     * 处理文章点赞/取消点赞请求
     * 接收包含userId、essayId和isLike字段的JSON数据
     */
    @PostMapping("/essays/{id}/like")
    public Result<Void> handleLike(@RequestBody Map<String, Object> requestData, @PathVariable String id) {
        try {
            // 从请求数据中提取参数
            Long userId = Long.valueOf(requestData.get("userId").toString());
            Long essayId = Long.valueOf(requestData.get("essayId").toString());
            boolean isLike = (Boolean) requestData.get("isLike");
            Essay e = essayService.updateLikes(userId, essayId, isLike);
            // 执行更新操作
            if (e != null) {
                return new Result<>(true, StatusCode.OK, "点赞成功");
            }
            return new Result<>(false, StatusCode.ERROR, "点赞失败");
        } catch (Exception e) {
            return new Result<>(false, StatusCode.ERROR, "点赞失败");
        }
    }

    @PostMapping("/essays/{id}/comments")
    public Result<EssayComment> post(@PathVariable Long id, @RequestBody Map<String, Object> para) {
        System.out.println(para);
        String content = (String) para.get("content");
        Long userId = Long.parseLong(para.get("userId").toString());
        long parentId = -1;
        if (para.get("parentCommentId") != null) {
            parentId = Long.parseLong(para.get("parentCommentId").toString());
        }
        User user = userService.findUserById(userId);
        EssayComment essayComment = new EssayComment();
        essayComment.setContent(content);
        essayComment.setEssay(essayService.getEssayById(id));
        essayComment.setUser(user);
        essayComment.setAdminComment(user.getType().equals("1"));
        if (parentId != -1) {
            essayComment.setParentEssayComment(essayCommentService.getEssayCommentById(parentId));
        }
        EssayComment newEssayComment = essayCommentService.saveEssayComment(essayComment);
        return new Result<>(true, StatusCode.OK, "评论发表成功！", newEssayComment);
    }

    //删除评论
    @GetMapping("/essays/comments/{id}/delete")
    public Result<Void> delete(@PathVariable Long id) {
        essayCommentService.deleteEssayComment(id);
        return new Result<>(true, StatusCode.OK, "删除评论成功", null);
    }
}