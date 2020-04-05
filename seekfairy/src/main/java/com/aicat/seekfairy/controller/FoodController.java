package com.aicat.seekfairy.controller;

import com.aicat.seekfairy.entity.Food;
import com.aicat.seekfairy.service.FoodIntroService;
import com.aicat.seekfairy.service.FoodService;
import com.aicat.seekfairy.utils.R;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("food")
public class FoodController {
    @Autowired
    FoodService foodService;
    @Autowired
    FoodIntroService foodIntroService;

    @GetMapping
    R list(@RequestParam Map<String, Object> params){
        int currentPage=Integer.parseInt(params.getOrDefault("page","1").toString());
        int pageSize=Integer.parseInt(params.getOrDefault("size","10").toString());
        Page<Food> page = PageHelper.startPage(currentPage,pageSize);
        foodService.findAll();
        PageInfo<Food> pageInfo = new PageInfo<>(page);
        return R.ok().put("page",pageInfo);
    }
    @PostMapping("add")
    R add(@RequestBody Food food){
        int i = foodService.create(food);
        return R.operate(i>0);
    }
    @PutMapping("edit")
    R update(@RequestBody Food food){
        int i = foodService.update(food);
        return R.operate(i>0);
    }
    @DeleteMapping("remove/{id}")
    R remove(@PathVariable Long id ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("food_id", id);
        if(foodIntroService.count(map)>0) {
            return R.error(1, "包含美食介绍,不允许删除");
        }
        int i = foodService.delete(id);
        return R.operate(i>0);
    }
}
