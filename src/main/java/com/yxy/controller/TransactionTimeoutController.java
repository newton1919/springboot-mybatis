package com.yxy.controller;

import com.yxy.base.RestResponse;
import com.yxy.service.TransactionTimeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuxiaoyang
 * spring Transaction Timeout测试
 */
@RestController
public class TransactionTimeoutController {
  @Autowired
  private TransactionTimeout functionService;

  @GetMapping("/transactionTimeout")
  public RestResponse transactionTimeout() throws Exception {
    RestResponse restResponse = new RestResponse(200, "");
    functionService.timeoutTest();
    return restResponse;
  }
}
