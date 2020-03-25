package com.aicat.seekfairy.service.impl;

import com.aicat.seekfairy.dao.FoodIntroducedDao;
import com.aicat.seekfairy.entity.FoodIntroduced;
import com.aicat.seekfairy.service.FoodIntroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FoodIntroServiceImpl implements FoodIntroService {
    @Autowired
    FoodIntroducedDao foodIntroducedDao;

    @Override
    public int count(Map<String, Object> map) {
        return foodIntroducedDao.count(map);
    }

    @Override
    public List<FoodIntroduced> findAll() {
        return foodIntroducedDao.selectAll();
    }

    @Override
    public FoodIntroduced findById(Long id) {
        return foodIntroducedDao.selectByPrimaryKey(id);
    }

    @Override
    public int create(FoodIntroduced foodIntroduced) {
        foodIntroduced.setCreate_date(new Date());
        return foodIntroducedDao.insert(foodIntroduced);
    }

    @Override
    public int delete(Long id) {
        return foodIntroducedDao.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long... ids) {
        return foodIntroducedDao.batchRemove(ids);
    }

    @Override
    public int update(FoodIntroduced foodIntroduced) {
        foodIntroduced.setUpdate_date(new Date());
        return foodIntroducedDao.updateByPrimaryKey(foodIntroduced);
    }

    @Override
    public Set<Integer> getIds() {
        return foodIntroducedDao.getIds();
    }
}
