package com.yeqk97.gulimall.order.dao;

import com.yeqk97.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author Qiongkai Ye
 * @email yeqk97@gmail.com
 * @date 2021-12-30 17:33:37
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
