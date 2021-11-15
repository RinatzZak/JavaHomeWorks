package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.AdvertisementManager;
import com.javarush.task.task27.task2712.ad.NoVideoAvailableException;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.TestOrder;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablet {
    private final int number;
    private static java.util.logging.Logger logger = Logger.getLogger(Tablet.class.getName());
    private LinkedBlockingQueue<Order> queue;

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public Tablet(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }

    public void createTestOrder(){
        TestOrder order = null;
        try {
            order = new TestOrder(this);
            if (processCreate(order)) return;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        } catch (NoVideoAvailableException e){
            logger.log(Level.INFO, "No video is available for the order " + order);
        }
    }

    public Order createOrder(){
        Order order = null;

        
        try {
            order = new Order(this);
            if (processCreate(order)) return null;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        } catch (NoVideoAvailableException e){
            logger.log(Level.INFO, "No video is available for the order " + order);
        }
        return order;
    }

    private boolean processCreate(Order order) {
        if (order.isEmpty()) {
            return true;
        }
        AdvertisementManager manager = new AdvertisementManager(order.getTotalCookingTime() * 60);
        try {
            queue.put(order);
        } catch (InterruptedException e) {
            System.out.println("добавка не удалась");
        }
        manager.processVideos();

        return false;
    }
    
}
