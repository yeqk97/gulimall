package com.yeqk97.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqk97.common.utils.PageUtils;
import com.yeqk97.common.utils.Query;

import com.yeqk97.gulimall.product.dao.CategoryDao;
import com.yeqk97.gulimall.product.entity.CategoryEntity;
import com.yeqk97.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends
        ServiceImpl<CategoryDao, CategoryEntity> implements
        CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page =
                this.page(new Query<CategoryEntity>().getPage(params),
                        new QueryWrapper<>());

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 1. Retrieve all categories
        List<CategoryEntity> entities = baseMapper.selectList(null);

        // 2. Build a parent child tree
        return entities.stream()
                .filter(e -> e.getParentCid() == 0L)
                .peek(e -> {
                    Map<Long, List<CategoryEntity>> allByParentId =
                            entities.stream()
                                    .filter(subent -> !Objects.equals(
                                            subent.getParentCid(),
                                            e.getParentCid()))
                                    .collect(Collectors.groupingBy(
                                            CategoryEntity::getParentCid));
                    e.setChildren(getChildren(e, allByParentId));
                })
                .sorted(Comparator.comparingInt(
                        CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    private static List<CategoryEntity> getChildren(CategoryEntity root,
                                                    Map<Long, List<CategoryEntity>> allByParentId) {
        List<CategoryEntity> children = allByParentId.get(root.getCatId());
        if (children != null) {
            return children.stream()
                    .peek(c -> c.setChildren(getChildren(c, allByParentId))).sorted(Comparator.comparingInt(
                            CategoryEntity::getSort))
                    .collect(
                            Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO check if used by other entities
        baseMapper.deleteBatchIds(asList);
    }
}