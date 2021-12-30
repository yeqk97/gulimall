package com.yeqk97.gulimall.product.dao;

import com.yeqk97.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author Qiongkai Ye
 * @email yeqk97@gmail.com
 * @date 2021-12-30 11:13:14
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
