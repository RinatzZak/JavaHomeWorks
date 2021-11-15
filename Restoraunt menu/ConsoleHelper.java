package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static String readString() throws IOException {
        return reader.readLine();
    }

    public static List<Dish> getAllDishesForOrder() throws IOException {
       List<Dish> dishes = new ArrayList<>();
        writeMessage("Пожалуйста, выберите блюдо, которое, Вы, хотите заказать" + Dish.allDishesToString());
        while (true) {
            String answer = readString();
            if (answer.equals("exit")) {
               break;
            }
            try {
                Dish dish = Dish.valueOf(answer);
                dishes.add(dish);
                writeMessage(answer + " Прекрасный выбор! Блюдо было добавлено в заказ!");
            } catch (Exception e) {
                writeMessage("Извините, у нас нет такого в меню.");
            }
        }
        return dishes;
    }
}
