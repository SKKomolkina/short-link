package org.example;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void start(MakeShortUrl makeShortUrl) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    1. Создать новую сессию
                    2. Войти в сессию
                    3. Выйти из приложения
                    
                    Введите число:""");

            String scan = scanner.next();
            if (Objects.equals(scan, "3")) System.exit(0);
            String userId = identification(scan, scanner);
            Session.run(userId, scanner, makeShortUrl);
        }
    }

    private static String identification(String scan, Scanner scanner) {
        String userId;
        switch (scan) {
            case "2":
                System.out.print("Введите ваш идентификатор: ");
                userId = scanner.next();
                return userId;
            case "1":
            default:
                userId = String.valueOf(UUID.randomUUID());
                System.out.println("Ваш идентификатор: " + userId);
                return userId;
        }
    }

    private static class Session {

        public static void run(String userId, Scanner scanner, MakeShortUrl makeShortUrl) {
            while (true) {
                System.out.println("""
                        1. Введите "1" для создания короткой ссылки
                        2. Введите "2" для получения оригинальной ссылки из короткой
                        3. Введите "3" для внесения изменений параметров короткой ссылки
                        4. Введите "4" для выхода из программы
                        
                        Введите число:""");

                String scan = scanner.next();
                if (scan.equals("4")) break;

                switch (scan) {
                    case "1":
                        System.out.print("Введите основную ссылку: ");
                        String originalUrl = scanner.next();
                        System.out.print("Введите количество переходов: ");
                        int clickLimit = scanner.nextInt();
                        System.out.println("Короткая ссылка: " + makeShortUrl.encode(originalUrl, userId, clickLimit));
                        System.out.println();
                        break;

                    case "2":
                        System.out.print("Введите короткую ссылку: ");
                        String shortUrl = scanner.next();
                        ShortUrl decode = makeShortUrl.decode(userId, shortUrl);

                        if (decode != null) {
                            String url = decode.getOriginalUrl();
                            System.out.println("Оригинальная ссылка: " + url);

                            try {
                                Desktop.getDesktop().browse(new URI(url));
                                decode.setUrlClickValue();
                            } catch (IOException | URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        System.out.println();
                        break;

                    case "3":
                        System.out.print("Введите короткую ссылку: ");
                        String shortUrlNew = scanner.next();

                        if (!makeShortUrl.urlCheck(userId, shortUrlNew)) {
                            System.out.println("По короткой ссылке: " + shortUrlNew + " не найдена оригинальная");
                            break;
                        }

                        System.out.print("Введите новое количество переходов: ");
                        int urlClickLimitNew = scanner.nextInt();
                        ShortUrl urlNew = makeShortUrl.decode(userId, shortUrlNew);

                        if (urlNew != null) {
                            urlNew.setUrlClickLimit(urlClickLimitNew);
                            System.out.println("Обновлено");
                        }
                        System.out.println();
                        break;
                }
            }
        }
    }
}
