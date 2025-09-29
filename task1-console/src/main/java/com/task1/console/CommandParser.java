package com.task1.console;

/**
 * Класс CommandParser отвечает за разбор строкового ввода пользователя
 * и преобразование его в объект Command для дальнейшей обработки
 * игровым движком (GameEngine).
 */
public class CommandParser {

    /**
     * Основной метод для парсинга ввода пользователя.
     * Определяет тип команды и выделяет аргументы.
     *
     * @param input строка ввода, например "GAME 3, user W, comp B"
     * @return объект Command с типом команды и массивом аргументов
     */
    public Command parse(String input) {
        // Если ввод пустой или null - возвращаем UNKNOWN команду
        if (input == null || input.trim().isEmpty()) {
            return new Command(CommandType.UNKNOWN, new String[0]);
        }

        input = input.trim();

        // Разделяем строку на ключевое слово и аргументы (максимум 2 части)
        String[] parts = input.split("\\s+", 2);
        String keyword = parts[0].toUpperCase();

        // Определяем тип команды по ключевому слову
        switch (keyword) {
            case "GAME":
                return parseGameCommand(parts);
            case "MOVE":
                return parseMoveCommand(parts);
            case "HELP":
                return new Command(CommandType.HELP, new String[0]);
            case "EXIT":
                return new Command(CommandType.EXIT, new String[0]);
            default:
                // Любая неизвестная команда
                return new Command(CommandType.UNKNOWN, new String[0]);
        }
    }

    /**
     * Разбор команды GAME.
     * Формат: "GAME N, U1, U2"
     * Где N - размер доски, U1 и U2 - параметры игроков.
     *
     * @param parts массив строк, где parts[1] содержит аргументы после "GAME"
     * @return объект Command с типом GAME и массивом аргументов
     */
    private Command parseGameCommand(String[] parts) {
        if (parts.length < 2) {
            // Нет аргументов после GAME
            return new Command(CommandType.UNKNOWN, new String[0]);
        }

        // Разделяем аргументы по запятой
        String[] args = parts[1].split(",");
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim(); // удаляем лишние пробелы
        }

        return new Command(CommandType.GAME, args);
    }

    /**
     * Разбор команды MOVE.
     * Формат: "MOVE X, Y" или "MOVE X Y"
     *
     * @param parts массив строк, где parts[1] содержит координаты хода
     * @return объект Command с типом MOVE и массивом аргументов [X, Y]
     */
    private Command parseMoveCommand(String[] parts) {
        if (parts.length < 2) {
            // Нет аргументов после "MOVE"
            return new Command(CommandType.UNKNOWN, new String[0]);
        }

        // Разделяем аргументы по запятой или пробелу
        String[] args = parts[1].split("[,\\s]+");

        return new Command(CommandType.MOVE, args);
    }
}
