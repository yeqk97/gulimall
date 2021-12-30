package com.yeqk97.gulimall.member.dao;

import com.yeqk97.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author Qiongkai Ye
 * @email yeqk97@gmail.com
 * @date 2021-12-30 17:41:04
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
