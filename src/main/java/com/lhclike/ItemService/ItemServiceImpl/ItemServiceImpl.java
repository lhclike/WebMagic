package com.lhclike.ItemService.ItemServiceImpl;

import com.lhclike.ItemService.ItemService;
import com.lhclike.Mapper.ItemMapper;
import com.lhclike.pojo.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    @Transactional
    public void save(Item item) {
        this.itemMapper.insert(item);
    }
}

