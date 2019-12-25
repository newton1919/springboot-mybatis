package com.yxy.controller;

import com.yxy.base.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yuxiaoyang
 * 可重入锁测试
 */
@RestController
@RequestMapping("/lock")
public class ReentrantLockController {
  //并发时可以采用加锁的机制解决同步问题
  private Lock lock = new ReentrantLock();

  @GetMapping("/multiLock")
  public RestResponse multiLock() {
    RestResponse restResponse = new RestResponse(200, "");
    lock.lock();
    lock.lock();
    lock.unlock();
    restResponse.setContent("multiLock done!!!");
    return restResponse;
  }

  @GetMapping("/getLock")
  public RestResponse getLock() {
    RestResponse restResponse = new RestResponse(200, "");
    lock.lock();
    lock.unlock();
    restResponse.setContent("getLock done!!!");
    return restResponse;
  }
}
