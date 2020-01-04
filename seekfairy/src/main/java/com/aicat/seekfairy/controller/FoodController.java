package com.aicat.seekfairy.controller;

import com.aicat.seekfairy.entity.Food;
import com.aicat.seekfairy.service.FoodIntroService;
import com.aicat.seekfairy.service.FoodService;
import com.aicat.seekfairy.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("food")
public class FoodController {
    @Autowired
    FoodService foodService;
    @Autowired
    FoodIntroService foodIntroService;

    @GetMapping
    R list(){
        List<Food> list = foodService.findAll();
        return R.ok().put("list",list);
    }
    @PostMapping("add")
    R add(@RequestBody Food food){
        int i = foodService.create(food);
        return R.operate(i>0);
    }
    @PutMapping("edit")
    R update(@RequestBody Food food){
        int i = foodService.create(food);
        return R.operate(i>0);
    }
    @DeleteMapping("remove/{id}")
    R remove(@PathVariable("id") Long id ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("food_id", id);
        if(foodIntroService.count(map)>0) {
            return R.error(1, "包含美食介绍,不允许删除");
        }
        int i = foodService.delete(id);
        return R.operate(i>0);
    }
}
