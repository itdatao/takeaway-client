package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.Comment;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Huhuitao
 * @Date 2021/1/29 16:07
 */
public interface CommentMapper extends BaseMapper<Comment> {


    int updateStatusById(@Param("id") String id);
}
