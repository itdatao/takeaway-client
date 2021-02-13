package com.ruoyi.web.controller;


import com.ruoyi.web.common.AjaxResult;
import com.ruoyi.web.form.CommentForm;
import com.ruoyi.web.service.CommentService;
import com.ruoyi.web.vo.CommentVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 客户端评论接口
 *
 * @Author Huhuitao
 * @Date 2021/2/3 15:21
 */
@CrossOrigin
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    @Qualifier("CommentServiceImpl")
    private CommentService commentService;

    @ApiOperation("分页查询客户所有评论")
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(value = "tagId",required = false) Integer tagId,
                           @RequestParam(value = "page",defaultValue = "1") Integer page,
                           @RequestParam(value = "size",defaultValue = "10") Integer size) {
        return AjaxResult.success(commentService.list(tagId,page,size));
    }

    //添加评论
    @ApiOperation("添加评论")
    @PostMapping("/add")
    public AjaxResult addComment(HttpServletRequest request, @RequestBody CommentForm commentForm){
        String token = request.getHeader("token");
        commentService.addComment(token,commentForm);
        return AjaxResult.success("添加评论成功！");
    }

    


}
