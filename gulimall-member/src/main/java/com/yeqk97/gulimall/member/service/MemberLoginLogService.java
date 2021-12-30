package com.yeqk97.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqk97.common.utils.PageUtils;
import com.yeqk97.gulimall.member.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author Qiongkai Ye
 * @email yeqk97@gmail.com
 * @date 2021-12-30 17:41:04
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

