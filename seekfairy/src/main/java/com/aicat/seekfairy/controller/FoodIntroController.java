package com.aicat.seekfairy.controller;

import com.aicat.seekfairy.entity.FoodIntroduced;
import com.aicat.seekfairy.service.FoodIntroService;
import com.aicat.common.utils.R;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("foodIntro")
public class FoodIntroController {
    @Autowired
    FoodIntroService foodIntroService;

    @GetMapping
    R list(@RequestParam Map<String, Object> params){
        int currentPage=Integer.parseInt(params.getOrDefault("page","1").toString());
        int pageSize=Integer.parseInt(params.getOrDefault("size","10").toString());
        Page<FoodIntroduced> page = PageHelper.startPage(currentPage,pageSize);
        foodIntroService.findAll();
        PageInfo<FoodIntroduced> pageInfo = new PageInfo<>(page);
        //PageUtils pageUtils = new PageUtils(pageInfo.getList(),(int)pageInfo.getTotal());
        return R.ok().put("page",pageInfo);
    }
    @PostMapping("add")
    R add(@RequestBody FoodIntroduced foodIntroduced){
        int i = foodIntroService.create(foodIntroduced);
        return R.operate(i>0);
    }
    @PutMapping("edit")
    R update(@RequestBody FoodIntroduced foodIntroduced){
        int i = foodIntroService.create(foodIntroduced);
        return R.operate(i>0);
    }
    @DeleteMapping("remove/{id}")
    R remove(@PathVariable Long id ){
        int i = foodIntroService.delete(id);
        return R.operate(i>0);
    }
}
