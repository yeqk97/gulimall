package com.yeqk97.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeqk97.gulimall.product.entity.BrandEntity;
import com.yeqk97.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {

//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setDescript("desc");
//        brandEntity.setName("Atm");
//        brandService.save(brandEntity);
//        System.out.println("Brand saved");

//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setBrandId(15L);
//        brandEntity.setName("Another brand");
//        brandService.updateById(brandEntity);

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 15L));
        for (BrandEntity brandEntity : list) {
            System.out.println(brandEntity);
        }
    }

}
