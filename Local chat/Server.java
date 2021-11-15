package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();
    

    public static void main(String[] args) {

        ConsoleHelper.writeMessage("Введите порт...");
        int port = ConsoleHelper.readInt();
       try (ServerSocket serverSocket = new ServerSocket(port)) {
        ConsoleHelper.writeMessage("Сервер запущен");
        while (true) {
               Socket socket = serverSocket.accept();
               new Handler(socket).start();
           }
       } catch (Exception e) {
                ConsoleHelper.writeMessage("Произошла ошибка. Попробуйте ещё раз.");
        }
    }


    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("Было установлено соединение с : " + socket.getRemoteSocketAddress());
                String userName = null;
            try {
              Connection  connection = new Connection(socket);
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                notifyUsers(connection, userName);
                serverMainLoop(connection, userName);
                connectionMap.remove(connection, userName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED));
            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Произошла ошибка");
            } 
           
           if (userName != null)
            connectionMap.remove(userName);
            sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));

        }
        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));

                Message message = connection.receive();
                if (message.getType() != MessageType.USER_NAME) {
                    ConsoleHelper.writeMessage("Полученное сообщение от " + connection.getRemoteSocketAddress() + " не соответствует протоколу");
                    continue;
                }

                String userName = message.getData();
                if (userName.isEmpty()) {
                    ConsoleHelper.writeMessage("Полученное сообщение от " + connection.getRemoteSocketAddress() + " не соответствует протоколу");
                    continue;
                }

                if (connectionMap.containsKey(userName)) {
                    ConsoleHelper.writeMessage("Уже имеется " + connection.getRemoteSocketAddress() + " такой пользователь");
                    continue;
                }

                connectionMap.put(userName, connection);
                ConsoleHelper.writeMessage("Имя было добавлено");

                connection.send(new Message(MessageType.NAME_ACCEPTED));
                return userName;
            }
        }


        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (String name : connectionMap.keySet()){
                if (name.equals(userName))
                    continue;
                connection.send(new Message(MessageType.USER_ADDED, name));
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {

            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) {
                     String data = message.getData();
                    sendBroadcastMessage(new Message(MessageType.TEXT, userName + ": " + data));
                } else ConsoleHelper.writeMessage("Произошла ошибка, введите сообщение." + connection.getRemoteSocketAddress());
            }
        }

    }

    public static void sendBroadcastMessage(Message message)  {
        for (Connection connection : connectionMap.values()) {
            try {
                connection.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    }

