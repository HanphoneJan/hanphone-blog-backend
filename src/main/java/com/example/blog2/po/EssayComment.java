package com.example.blog2.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_essay_comment")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class EssayComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    // 多对一关联：多个评论属于一篇文章
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "essay_id", nullable = false)
    @JsonIgnoreProperties("essayComments")
    private Essay essay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore //防止循环引用
    private EssayComment parentEssayComment;

    @Column(name = "admin_comment", nullable = false)
    private Boolean adminComment;

    private String content;
}