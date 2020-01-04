package com.aicat.seekfairy.service;

import java.util.List;

public interface BaseService<T>  {
    /**
     * 查询所有
     *
     * @return
     */
    List<T> findAll();

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    T findById(Long id);

    /**
     * 添加
     *
     * @param t
     */
    int create(T t);

    /**
     * 删除
     *
     * @param id
     */
    int delete(Long id);
    /**
     * 删除（批量）
     *
     * @param ids
     */
    int batchRemove(Long... ids);

    /**
     * 修改
     *
     * @param t
     */
    int update(T t);
}
