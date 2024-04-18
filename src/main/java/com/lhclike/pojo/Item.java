package com.lhclike.pojo;


import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //商品标题
    private String title;

    //商品图片
    private String pic;
    //商品详情地址
    private String url;

    //创建时间
    private Date created;
}

