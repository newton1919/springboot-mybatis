package com.yxy.controller;

import com.yxy.base.BusinessException;
import com.yxy.base.RestResponse;
import com.yxy.utils.FieldNoGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/common2")
public class Common2Controller {
  @Autowired
  private FieldNoGenerator fieldNoGenerator;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @GetMapping("/testNoGenerator")
  @Transactional(rollbackFor = Exception.class)
  public RestResponse testNoGenerator() throws Exception {
    System.err.println("start testNoGenerator...");
    RestResponse restResponse = new RestResponse(200, "");
    String no = fieldNoGenerator.generator("qq", "00000", "_");
    System.err.println("generator no ----->" + no);
    restResponse.setContent(no);
    //模拟主业务流程
    jdbcTemplate.update("update yxy_test set name='yxy' where id=1");
    return restResponse;
  }

  @GetMapping("/multiInsert")
  @Transactional(rollbackFor = Exception.class)
  public RestResponse multiInsert() throws Exception {
    RestResponse restResponse = new RestResponse(200, "");
    int count = jdbcTemplate.update("INSERT INTO UNIQUE_KEY VALUES ('qq_20191203-00000', '2')");
    System.err.println("count ----->" + count);
    Thread.sleep(10000);
    restResponse.setContent(count);
    return restResponse;
  }

  @GetMapping("/hello")
  public RestResponse sayHello() throws Exception {
    RestResponse restResponse = new RestResponse(200, "");
    restResponse.setContent("hello world!!!");
    return restResponse;
  }
}
