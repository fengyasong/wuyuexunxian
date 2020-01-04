package com.aicat.seekfairy.controller;

import com.aicat.seekfairy.entity.FoodIntroduced;
import com.aicat.seekfairy.service.FoodIntroService;
import com.aicat.seekfairy.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("foodIntro")
public class FoodIntroController {
    @Autowired
    FoodIntroService foodIntroService;

    @GetMapping
    R list(){
        List<FoodIntroduced> list = foodIntroService.findAll();
        return R.ok().put("list",list);
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
    R remove(@PathVariable("id") Long id ){
        int i = foodIntroService.delete(id);
        return R.operate(i>0);
    }
}
