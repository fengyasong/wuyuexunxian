package com.aicat.mybatisplus.controller;

import com.aicat.mybatisplus.dao.FoodDao;
import com.aicat.mybatisplus.dao.FoodIntroducedDao;
import com.aicat.mybatisplus.entity.Food;
import com.aicat.mybatisplus.utils.R;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("food")
public class FoodController {
    @Autowired
    FoodDao foodService;
    @Autowired
    FoodIntroducedDao foodIntroService;

    @GetMapping
    R list(@RequestParam Map<String, Object> params){
        /*int currentPage=Integer.parseInt(params.getOrDefault("page","1").toString());
        int pageSize=Integer.parseInt(params.getOrDefault("size","10").toString());
        Page<Food> page = PageHelper.startPage(currentPage,pageSize);
        foodService.findAll();
        PageInfo<Food> pageInfo = new PageInfo<>(page);*/
        //物理分页查询，查询的时候有limit语句
        List<Food> page1 = foodService.selectPage(new Page<Food>(1,3),
                new EntityWrapper<Food>()
                        .between("create_date","2017-03-01","2020-03-01")
                        .eq("status",0)
                        .eq("name","tom")
        );
        return R.ok().put("page",page1);
    }
    @GetMapping("test")
    R test(@RequestParam Map<String, Object> params){
        //根据id查询：
        Food food = foodService.selectById(1);

        //根据条件查询一条数据：
        Food foodCondition = new Food();
        foodCondition.setName("烧鸡");
        foodCondition.setStatus((byte)1);
        Food shaoji = foodService.selectOne(foodCondition);
        //这个方法的sql语句就是where status = 1 and name = "烧鸡"，若是符合这个条件的记录不止一条，那么就会报错。

        //根据查询条件返回多条数据
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("name","烧鸡");//写表中的列名
        columnMap.put("status",1);
        List<Food> list = foodService.selectByMap(columnMap);
        System.out.println(list.size());
        //查询条件用map集合封装，columnMap，写的是数据表中的列名，而非实体类的属性名。比如属性名为lastName，数据表中字段为last_name，这里应该写的是last_name。selectByMap方法返回值用list集合接收。

        //通过id批量查询
        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        idList.add(2);
        idList.add(3);
        List<Food> foods = foodService.selectBatchIds(idList);
        System.out.println(foods.size());

        //内存分页查询，查询的时候并没有limit语句
        List<Food> neicunPage = foodService.selectPage(new Page<>(1,2),null);
        System.out.println(neicunPage);

        //条件构造器(EntityWrapper)
        List<Food> employees = foodService.selectList(
                new EntityWrapper<Food>()
                        .eq("gender",0)
                        .like("last_name","老师")
                        //.or()//和or new 区别不大
                        .orNew()
                        .like("email","a")
        );
        List<Food> employees2 = foodService.selectList(
                new EntityWrapper<Food>()
                        .eq("gender",0)
                        .orderBy("age")//直接orderby 是升序，asc
                        .last("desc limit 1,3")//在sql语句后面追加last里面的内容(改为降序，同时分页)
        );

        //物理分页查询，查询的时候有limit语句
        List<Food> page1 = foodService.selectPage(new Page<Food>(1,3),
                new EntityWrapper<Food>()
                        .between("age",18,50)
                        .eq("gender",0)
                        .eq("last_name","tom")
        );
        List<Food> page2 = foodService.selectPage(
                new Page<Food>(1,2),
                Condition.create()
                        .between("age",18,50)
                        .eq("gender","0")
        );
        //Condition和EntityWrapper的区别就是，创建条件构造器时，EntityWrapper是new出来的，而Condition是调create方法创建出来。

        //根据条件更新
        Food employee = new Food();
        employee.setName("热干面");
        employee.setStatus((byte)1);
        foodService.update(employee,
                new EntityWrapper<Food>()
                        .eq("last_name","tom")
                        .eq("age",25)
        );

        //根据条件删除
        foodService.delete(
                new EntityWrapper<Food>()
                        .eq("last_name","tom")
                        .eq("age",16)
        );
        return R.ok().put("page",page1);
    }
    @PostMapping("add")
    R add(@RequestBody Food food){
        int i = foodService.insert(food);
        return R.operate(i>0);
    }
    @PutMapping("edit")
    R update(@RequestBody Food food){
        int i = foodService.updateById(food);//根据id进行更新，没有传值的属性就不会更新
        //int i2 = foodService.updateAllColumnById(food);//根据id进行更新，没传值的属性就更新为null
        return R.operate(i>0);
    }
    @DeleteMapping("remove/{id}")
    R remove(@PathVariable Long id ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("food_id", id);
        if(foodIntroService.selectByMap(map).size()>0) {
            return R.error(1, "包含美食介绍,不允许删除");
        }
        //根据id删除
        int i = foodService.deleteById(id);

        //根据条件删除
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("gender",0);
        columnMap.put("age",18);
        foodService.deleteByMap(columnMap);

        //根据id批量删除
        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        idList.add(2);
        foodService.deleteBatchIds(idList);
        return R.operate(i>0);
    }
}
