package com.yxy.model;


import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;


@Data
@Entity
@Table(name = "yxy_test")
public class YxyTest {

  //null
  @Id
  @Column(name = "id")
  private Long id;

  //null
  @Column(name = "name")
  private String name;


}
