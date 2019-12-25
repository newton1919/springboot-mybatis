package com.yxy.controller;

import com.yxy.base.RestResponse;
import com.yxy.service.TransactionTimeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuxiaoyang
 * spring Transaction 相关测试
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {
  /**
   * 事务rollback时，看看是否会打印回滚日志
   * @return
   * @throws Exception
   */
  @Transactional(rollbackFor = Exception.class)
  @GetMapping("/rollback")
  public RestResponse rollback() throws Exception {
    RestResponse restResponse = new RestResponse(200, "");
    throw new Exception("抛出异常，制造事务回滚");
    //return restResponse;
  }
}
