package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ruoyi.web.domain.Comment;
import com.ruoyi.web.exception.CustomException;
import com.ruoyi.web.form.CommentForm;
import com.ruoyi.web.mapper.CommentMapper;
import com.ruoyi.web.service.CommentService;
import com.ruoyi.web.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author hht
 * @Date 2021/1/29 16:14
 */
@Service("CommentServiceImpl")
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;


    @Override
    public void addComment(String token, CommentForm commentForm) throws ExpiredJwtException,CustomException {
        if(commentForm==null){
            throw new CustomException("评论内容添加失败");
        }
        String avatar = JwtUtil.getSubject(token, "avatar");
        String nickname = JwtUtil.getSubject(token, "nickname");
        Comment comment = new Comment();
        comment.setAvatar(avatar);
        comment.setContent(commentForm.getContent());
        comment.setLevel(commentForm.getLevel());
        comment.setNickname(nickname);
        log.info("{}",commentForm);
        for (String foodName : commentForm.getFoodNames()) {
            Comment tempComment = new Comment();
            BeanUtils.copyProperties(comment,tempComment);
            tempComment.setFoodName(foodName);
            commentMapper.insert(tempComment);
        }

    }

    @Override
    public List<Comment> list(Integer tagId,Integer page,Integer size) {
        Page<Comment> pageInfo = new Page<>(page,size);
        if(tagId!=null&&tagId==1){
            //查询好评
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("level", 3);
            Page<Comment> commentPage = baseMapper.selectPage(pageInfo, queryWrapper);
            return commentPage.getRecords();

        }else if(tagId!=null&&tagId == 0){
            //查询差评
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lt("level", 3);
            Page<Comment> commentPage = baseMapper.selectPage(pageInfo, queryWrapper);
            return commentPage.getRecords();
        }else{
            Page<Comment> commentPage = baseMapper.selectPage(pageInfo, null);
            return commentPage.getRecords();
        }

    }


}
