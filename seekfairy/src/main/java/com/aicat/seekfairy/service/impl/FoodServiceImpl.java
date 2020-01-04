package com.aicat.seekfairy.service.impl;

import com.aicat.seekfairy.dao.FoodDao;
import com.aicat.seekfairy.entity.Food;
import com.aicat.seekfairy.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class FoodServiceImpl implements FoodService {
    @Autowired
    FoodDao foodDao;

    @Override
    public List<Food> findAll() {
        return foodDao.selectAll();
    }

    @Override
    public Food findById(Long id) {
        return foodDao.selectByPrimaryKey(id);
    }

    @Override
    public int create(Food food) {
        food.setCreate_date(new Date());
        return foodDao.insert(food);
    }

    @Override
    public int delete(Long id) {
        return foodDao.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long... ids) {
        return 0;
    }

    @Override
    public int update(Food food) {
        food.setCreate_date(new Date());
        return foodDao.updateByPrimaryKey(food);
    }
}
