package com.task1.console;

/**
 * Класс Command представляет команду, введённую пользователем или системой.
 * Команда состоит из типа (CommandType) и аргументов (String[] args).
 */
public class   Command {
    /** Тип команды (GAME, MOVE, EXIT, HELP и т.д.) */
    private final CommandType type;

    /** Массив аргументов команды (например, координаты или параметры игроков) */
    private final String[] args;

    /**
     * Конструктор для создания команды с типом и аргументами.
     *
     * @param type тип команды
     * @param args массив аргументов команды
     */
    public Command(CommandType type, String[] args) {
        this.type = type;
        this.args = args;
    }

    /**
     * Получает тип команды.
     *
     * @return тип команды (CommandType)
     */
    public CommandType getType() {
        return type;
    }

    /**
     * Получает массив аргументов команды.
     *
     * @return массив аргументов (String[])
     */
    public String[] getArgs() {
        return args;
    }
}
