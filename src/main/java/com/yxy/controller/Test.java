package com.yxy.controller;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
  //volatile修饰共享变量只具备并发中的可见性,不具备原子性,故不能解决并发时的问题
  //private static volatile long count = 0;
  private static long count = 0;
  //并发时可以采用加锁的机制解决同步问题
  private Lock lock = new ReentrantLock();

  private void add10K() {
    int idx = 0;
    while (idx++ < 10000) {
      lock.lock();
      count += 1;
      lock.unlock();
    }
  }

  public static void main(String[] args) throws Exception {
    final Test test = new Test();
    // 创建两个线程，执行add()操作
    Thread th1 = new Thread(() -> {
      test.add10K();
    });
    Thread th2 = new Thread(() -> {
      test.add10K();
    });
    // 启动两个线程
    th1.start();
    th2.start();
    // 等待两个线程执行结束
    th1.join();
    th2.join();
    System.out.println("count is : " + count);
  }
}
