package com.task1.console;

import java.util.*;

/**
 * Класс Main - точка входа в консольное приложение игры "Квадраты".
 * Считывает команды от пользователя, парсит их и вызывает соответствующие методы GameEngine.
 */
public class Main {

    /**
     * Основной метод запуска приложения.
     * Организует цикл ввода команд от пользователя.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        // Создаем сканер для считывания пользовательского ввода с консоли
        Scanner scanner = new Scanner(System.in);

        // Создаем игровой движок для управления логикой игры
        GameEngine engine = new GameEngine();

        // Создаем парсер команд, чтобы интерпретировать ввод пользователя
        CommandParser parser = new CommandParser();

        // Главный цикл программы
        while (true) {
            // Считываем строку команды от пользователя
            String line = scanner.nextLine();

            // Парсим введённую команду
            Command cmd = parser.parse(line);

            // Выполняем действие в зависимости от типа команды
            switch (cmd.getType()) {
                case GAME:
                    // Начало новой игры с указанными аргументами
                    engine.startNewGame(cmd.getArgs());
                    break;

                case MOVE:
                    // Выполнить ход пользователя
                    engine.makeUserMove(cmd.getArgs());
                    break;

                case HELP:
                    // Показать справку по командам
                    printHelp();
                    break;

                case EXIT:
                    // Завершение работы программы
                    System.out.println("Выход из игры.");
                    return;

                default:
                    // Некорректная или неизвестная команда
                    engine.isIncorrectCommand();
            }
        }
    }

    /**
     * Выводит все доступные команды в консоль.
     * Команды:
     * - GAME N, TYPE1 C1, TYPE2 C2 - начать новую игру
     * - MOVE X, Y - сделать ход игрока
     * - HELP - показать справку
     * - EXIT - завершить игру
     */
    private static void printHelp() {
        System.out.println("Доступные команды:");
        System.out.println("GAME N, TYPE1 C1, TYPE2 C2 - начать новую игру (пример: GAME 5, user W, comp B)");
        System.out.println("MOVE X, Y - сделать ход (пример: MOVE 2, 3)");
        System.out.println("HELP - список команд");
        System.out.println("EXIT - выход из программы");
    }
}
