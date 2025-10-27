package com.example.blog2.service;

import com.example.blog2.po.Essay;

import java.util.List;

public interface EssayService {
    Essay getEssayById(Long id);

    List<Essay> listEssay(Long userId);

    void deleteEssay(Long id);

    Essay saveEssay(Essay essay);

    Essay updateEssay(Long id,Essay essay);

    /**
     * 更新文章点赞数
     * @return 更新后的文章
     */
    Essay updateLikes(Long userId,Long essayId, boolean isLike);
    Boolean changeRecommend(Long essayId, Boolean recommend);
}
