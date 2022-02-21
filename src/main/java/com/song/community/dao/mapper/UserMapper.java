package com.song.community.dao.mapper;

import com.song.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kycni
 * @date 2022/2/20 22:35
 */
@Mapper
public interface UserMapper {
    /**
     * 根据id查询具体用户
     */
    User selectUserById (int id);
}
