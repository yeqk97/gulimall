package com.yeqk97.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqk97.common.utils.PageUtils;
import com.yeqk97.gulimall.product.entity.ProductAttrValueEntity;

import java.util.Map;

/**
 * spu属性值
 *
 * @author Qiongkai Ye
 * @email yeqk97@gmail.com
 * @date 2021-12-30 11:13:14
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

