package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.Comment;
import com.ruoyi.web.form.CommentForm;
import com.ruoyi.web.vo.CommentVO;


import java.util.List;

/**
 * @Author Huhuitao
 * @Date 2021/1/29 16:14
 */
public interface CommentService  {

    //评价分页
    List<Comment> list(Integer tagId,Integer page,Integer size);

    void addComment(String token, CommentForm commentForm);

    //根据餐品id获取评价列表

}
