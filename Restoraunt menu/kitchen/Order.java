package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.List;

public class Order {
    private final Tablet tablet;
    protected List<Dish> dishes;

    public List<Dish> getDishes() {
        return dishes;
    }

    public Tablet getTablet() {
        return tablet;
    }
    protected void initDishes() throws IOException {
        this.dishes = ConsoleHelper.getAllDishesForOrder();
    }

    public Order(Tablet tablet) throws IOException {
        this.tablet = tablet;
        initDishes();
        ConsoleHelper.writeMessage(toString());
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       if (dishes.size() == 0) return sb.toString();
       sb.append("Your order: [" + dishes.get(0));

        for (int i = 1; i < dishes.size(); i++) {
            sb.append(", " + dishes.get(i).name());
        }
        sb.append("] of " + tablet);
        sb.append(", cooking time " + getTotalCookingTime() + "min");
        return sb.toString();
    }

    public int getTotalCookingTime(){
            return dishes.stream().mapToInt(Dish::getDuration).sum();
    }

    public boolean isEmpty(){
        return dishes.isEmpty();
    }
}
