package com.ruoyi.web.form;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author Huhuitao
 * @Date 2021/2/10 16:29
 */
@Data
@ToString
public class CommentForm {
    private String content;
    private Integer level;
    private List<String> foodNames;
}
