package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.Connection;
import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;
import com.javarush.task.task30.task3008.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected;


    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public class SocketThread extends Thread {
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage("Пользователь " + userName + " присоединился.");
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage("Пользователь " + userName + " покинул чат");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();

                if (message.getType() == MessageType.NAME_REQUEST) {
                    connection.send(new Message(MessageType.USER_NAME, getUserName()));
                } else if (message.getType() == MessageType.NAME_ACCEPTED) {
                    notifyConnectionStatusChanged(true);
                    return;
                } else
                    throw new IOException("Unexpected MessageType");
            }
        }

       protected void clientMainLoop() throws IOException, ClassNotFoundException {

            while (true) {
                Message message = connection.receive();

                if (message.getType() == MessageType.TEXT) {
                    processIncomingMessage(message.getData());
                } else if (message.getType() == MessageType.USER_ADDED) {
                    informAboutAddingNewUser(message.getData());
                } else if (message.getType() == MessageType.USER_REMOVED) {
                    informAboutDeletingNewUser(message.getData());
                } else
                    throw new IOException("Unexpected MessageType");
            }
        }


        @Override
        public void run() {
            try {
                connection = new Connection(new Socket(getServerAddress(), getServerPort()));
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }

        }
    }

        protected String getServerAddress() throws IOException {
            ConsoleHelper.writeMessage("Пожалуйста, введите адрес сервера");
            String serverAddress = ConsoleHelper.readString();
            return serverAddress;
        }

        protected String getUserName() {
            ConsoleHelper.writeMessage("Пожалуйста, введи ваше имя");
            String userName = ConsoleHelper.readString();
            return userName;
        }

        protected int getServerPort() throws IOException {
            ConsoleHelper.writeMessage("Пожалуйста, введите порт сервера");
            int serverPort = ConsoleHelper.readInt();
            return serverPort;
        }

        protected boolean shouldSendTextFromConsole() {
            return true;
        }

        protected SocketThread getSocketThread() {
            return new SocketThread();
        }

        protected void sendTextMessage(String text) {
            try {
                connection.send(new Message(MessageType.TEXT, text));
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Произошла ошибка");
                clientConnected = false;
            }
        }

        public void run() {
            SocketThread thread = getSocketThread();
            thread.setDaemon(true);
            thread.start();
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage("Произошла ошибка. Хаха!");
                    return;
                }
                notify();

                if (clientConnected) {
                    ConsoleHelper.writeMessage("Соединение установлено.Для выхода наберите команду 'exit'.");
                } else {
                    System.out.println("Произошла ошибка во время работы клиента.");
                }

                while (clientConnected) {
                    String text = ConsoleHelper.readString();
                    if (text.equalsIgnoreCase("exit")) break;
                    if (shouldSendTextFromConsole()) {
                        sendTextMessage(text);
                    }
                }


            }
        }
    }


