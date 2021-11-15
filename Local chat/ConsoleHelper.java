package com.javarush.task.task30.task3008;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static String readString() {
        while (true) {
            try {
                String read = reader.readLine();
                if (read != null)
                    return read;
            } catch (IOException ioException) {
                writeMessage("Произошла ошибка при попытке ввода текста. Попробуйте ещё раз.");
            }
        }
    }

    public static int readInt() {
        while (true) {
            try {
                int read = Integer.parseInt(readString().trim());
                return read;
            } catch (NumberFormatException numberFormatException) {
                writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте ещё раз.");
            }
            
        }
    }
}
