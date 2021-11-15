package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;


import java.util.Observable;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


public class Cook extends Observable implements Runnable  {


    private final String name;
    private boolean busy;
    private LinkedBlockingQueue<Order> queue;

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public boolean isBusy() {
        return busy;
    }

    public Cook(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void startCookingOrder(Order order){
        this.busy = true;
            ConsoleHelper.writeMessage(name + " start cooking - " + order);
        CookedOrderEventDataRow row = new CookedOrderEventDataRow(order.getTablet().toString(), name, order.getTotalCookingTime() * 60, order.getDishes());
        StatisticManager.getInstance().register(row);
        try {
            Thread.sleep(order.getTotalCookingTime() * 10);
        } catch (InterruptedException ignore) {
        }
        
            this.busy = false;

    }

    @Override
    public void run() {
        try {
            Thread.sleep(10);
        } catch(InterruptedException ignore){
        }
        while (true) {
            if (!queue.isEmpty()) {

                if (!(this.isBusy())) {
                    try {
                        this.startCookingOrder(queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
