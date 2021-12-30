package com.yeqk97.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqk97.common.utils.PageUtils;
import com.yeqk97.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author Qiongkai Ye
 * @email yeqk97@gmail.com
 * @date 2021-12-30 13:46:15
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

