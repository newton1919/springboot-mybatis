package com.yxy.service;

import com.yxy.dao.YxyTestMapper;
import com.yxy.model.YxyTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author yuxiaoyang
 * spring Transaction Timeout测试
 */
@Service
public class TransactionTimeout {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Resource
  private YxyTestMapper yxyTestMapper;

  @Transactional(timeout = 7)
  public void timeoutTest() throws Exception{
    //Thread.sleep(9000);
    //jdbcTemplate.queryForRowSet("select * from yxy_test");
    YxyTest queryObj = new YxyTest();
    queryObj.setId(1L);
    queryObj.setName("caijunhua");
    yxyTestMapper.updateByPrimaryKeySelective(queryObj);
  }
}
