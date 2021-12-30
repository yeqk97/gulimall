package com.yeqk97.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqk97.common.utils.PageUtils;
import com.yeqk97.gulimall.member.entity.MemberStatisticsInfoEntity;

import java.util.Map;

/**
 * 会员统计信息
 *
 * @author Qiongkai Ye
 * @email yeqk97@gmail.com
 * @date 2021-12-30 17:41:04
 */
public interface MemberStatisticsInfoService extends IService<MemberStatisticsInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

