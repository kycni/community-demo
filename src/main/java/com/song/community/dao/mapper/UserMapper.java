package com.song.community.dao.mapper;

import com.song.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kycni
 * @date 2022/2/20 8:44
 */
@Mapper
public interface UserMapper {
    /**
     * 查询具体用户(id查询)
     */
    User selectUserById (int id);
}
