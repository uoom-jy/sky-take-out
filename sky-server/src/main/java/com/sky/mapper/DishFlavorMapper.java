package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     */
    void insertBatch(List<DishFlavor> flavors);

//    /**
//     * 根据菜品id删除口味数据
//     */
//    @Delete("delete from dish_flavor where dish_id =${DishIds}")
//    void deleteByDishIds(Long DishId);


    void deleteByDishIds(List<Long> ids);

    /**
     * 根据菜品id查询口味数据
     */
    @Select("select * from dish_flavor where dish_id =${dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
