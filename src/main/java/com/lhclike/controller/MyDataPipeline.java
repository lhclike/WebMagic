package com.lhclike.controller;

import com.lhclike.ItemService.ItemService;
import com.lhclike.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyDataPipeline implements Pipeline {

    @Autowired
    private ItemService itemService;


    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的商品详情对象
        ArrayList<Item> items = resultItems.get("itemInfo");
        if (items != null) {
            for (Item item : items) {
                this.itemService.save(item);
            }
        }
    }
}

