package com.yxy.utils;

import com.alibaba.fastjson.JSONObject;
import com.yxy.base.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FieldNoGenerator {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  /**
   * 拼接内部凭证号
   * example CL_20190624-00000
   * param cl （unique_key 需要小写）, number 00000(多少位)，flag（_）
   * @param param
   * @return
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public String generator(String param, String number,String flag) throws Exception {
    Map<String, Object> clNo = null;
    try {
      clNo = getNo(param, number, flag);
    } catch (org.springframework.dao.DuplicateKeyException ex) {
      clNo = getNo(param, number, flag);
    }
    String no = clNo.get("no") == null ? null : clNo.get("no").toString() ;
    String hi = clNo.get("hi") == null ? null : clNo.get("hi").toString();
    if(StringUtils.isEmpty(no) || StringUtils.isEmpty(hi)){
      throw new RuntimeException("FieldNoGenerator编号生成错误");
    }
    int len = hi.length();
    StringBuilder noSb = new StringBuilder(no);
    noSb.replace(noSb.length() - len,noSb.length(),hi);

    //Thread.sleep(2000);
    return noSb.toString().toUpperCase();
  }

  protected Map<String, Object> getNo(String param,String number,String flag) throws BusinessException{
    Map<String, Object> _map = new HashMap<>();

    //当前日期的年月日,要取数据库时间
    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    String date = null;
    try {
      date = sf.format(new Date());
    } catch (Exception e) {
      log.error("解析数据库时间失败", e);
      throw new BusinessException(501, "解析数据库时间失败");
    }
    param = param + flag + date;

    param = param + "-" + number;

    String sql = " select * from unique_key where OBJECT_NAME = '" + param + "' for update "; //锁定读


    List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
    if (result.size() > 1) {
      throw new RuntimeException("数据错误,出现重复数据:" + result);
    }

    if (result == null || result.size() == 0) {
      int insert = jdbcTemplate.update("INSERT INTO unique_key (OBJECT_NAME,NEXT_HI) VALUES(?, ?)", new Object[]{param, 2});
      if (insert == 1) {
        _map.put("no", param);
        _map.put("hi", 1);
        return _map;
      } else {
        throw new RuntimeException("INSERT INTO unique_key 操作失败,请重试!");
      }

    } else {
      Map<String, Object> map = result.get(0);
      if (map.get("NEXT_HI") == null) {
        throw new RuntimeException("数据错误:NEXT_HI为null");
      }
      if (map != null) {
        _map.put("no", map.get("OBJECT_NAME").toString());
        _map.put("hi", map.get("NEXT_HI").toString());
      } else {
        throw new RuntimeException("select from unique_key操作失败,请重试!");
      }
      Integer num = Integer.valueOf(map.get("NEXT_HI").toString()) + 1;
      jdbcTemplate.update("UPDATE UNIQUE_KEY set NEXT_HI =? where OBJECT_NAME =? ", new Object[]{num, param});
      return _map;
    }
  }
}
