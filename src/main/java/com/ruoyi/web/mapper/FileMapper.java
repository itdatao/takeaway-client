package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.StaticFile;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Huhuitao
 * @Date 2021/1/27 15:31
 */

public interface FileMapper extends BaseMapper<StaticFile> {

    int checkFileUnique(@Param("fileName") String fileName);
}
