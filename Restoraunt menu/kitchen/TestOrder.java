package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestOrder extends Order{
    @Override
    protected void initDishes() throws IOException {
       this.dishes = new ArrayList<>();
       Dish[] dish = Dish.values();
       int rand = (int) (Math.random()*3 + 2);
        for (int i = 0; i < rand; i++) {
            int dishIndex = (int) (Math.random()*dish.length);
            dishes.add(dish[dishIndex]);
        }
    }

    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

}
