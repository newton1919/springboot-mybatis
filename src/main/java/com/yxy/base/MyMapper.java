package com.xjgj.show.common.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * Created by TF on 2018/1/25.
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
    /**
     * 按实体类或自定义条件查询，返回自定义结果
     * @param objectForQuery
     * @return
     */
    public <R, P extends Object> List<R> selectList(P objectForQuery);

    /**
     * 按主键查询某一条记录详情，返回自定义结果
     * @param pk 主键
     * @return 自定义结果
     */
    public Object selectOneDetail(String pk);
}
