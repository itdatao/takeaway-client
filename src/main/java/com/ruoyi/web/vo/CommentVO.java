package com.ruoyi.web.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Huhuitao
 * @Date 2021/1/29 16:49
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    //评论用户的昵称
    private String nickName;
    //评论对象，是对应餐品
    private String foodName;
    //评分 3以上是好评，3是中评，1，2是差评
    private Integer level;
    //评论内容
    private String content;
    /*默认状态是0代表不显示，1代表显示*/
    private Integer status;
    //用户头像
    private String avatar;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
