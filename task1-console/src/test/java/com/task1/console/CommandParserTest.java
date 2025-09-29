package com.task1.console;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Юнит-тесты для класса CommandParser.
 * Проверяют корректность парсинга пользовательских команд: GAME, MOVE, HELP, EXIT и неизвестных.
 */
public class CommandParserTest {

    /** Экземпляр парсера, который используется в каждом тесте */
    private CommandParser parser;

    /**
     * Метод, выполняющийся перед каждым тестом.
     * Создает новый экземпляр CommandParser.
     */
    @Before
    public void setUp() {
        parser = new CommandParser();
    }

    /**
     * Проверка корректного распознавания команды GAME и разбиения аргументов.
     */
    @Test
    public void testParseGameCommand() {
        Command cmd = parser.parse("GAME 5, user W, comp B");

        assertEquals("Должен распознать команду GAME", CommandType.GAME, cmd.getType());
        assertNotNull("Аргументы не должны быть null", cmd.getArgs());
        assertEquals("Должно быть 3 аргумента", 3, cmd.getArgs().length);
        assertEquals("Первый аргумент должен быть '5'", "5", cmd.getArgs()[0]);
        assertEquals("Второй аргумент должен быть 'user W'", "user W", cmd.getArgs()[1]);
        assertEquals("Третий аргумент должен быть 'comp B'", "comp B", cmd.getArgs()[2]);
    }

    /**
     * Проверка корректного распознавания команды MOVE и разбиения аргументов через запятую.
     */
    @Test
    public void testParseMoveCommand() {
        Command cmd = parser.parse("MOVE 2, 3");

        assertEquals("Должен распознать команду MOVE", CommandType.MOVE, cmd.getType());
        assertNotNull("Аргументы не должны быть null", cmd.getArgs());
        assertEquals("Должно быть 2 аргумента", 2, cmd.getArgs().length);
        assertEquals("Первый аргумент должен быть '2'", "2", cmd.getArgs()[0]);
        assertEquals("Второй аргумент должен быть '3'", "3", cmd.getArgs()[1]);
    }

    /**
     * Проверка команды MOVE с пробелами вместо запятой.
     */
    @Test
    public void testParseMoveCommandWithSpaces() {
        Command cmd = parser.parse("MOVE 2 3");

        assertEquals("Должен распознать команду MOVE с пробелами", CommandType.MOVE, cmd.getType());
        assertEquals("Должно быть 2 аргумента", 2, cmd.getArgs().length);
        assertEquals("Первый аргумент должен быть '2'", "2", cmd.getArgs()[0]);
        assertEquals("Второй аргумент должен быть '3'", "3", cmd.getArgs()[1]);
    }

    /**
     * Проверка команды HELP без аргументов.
     */
    @Test
    public void testParseHelpCommand() {
        Command cmd = parser.parse("HELP");

        assertEquals("Должен распознать команду HELP", CommandType.HELP, cmd.getType());
        assertEquals("HELP не должен иметь аргументов", 0, cmd.getArgs().length);
    }

    /**
     * Проверка команды EXIT без аргументов.
     */
    @Test
    public void testParseExitCommand() {
        Command cmd = parser.parse("EXIT");

        assertEquals("Должен распознать команду EXIT", CommandType.EXIT, cmd.getType());
        assertEquals("EXIT не должен иметь аргументов", 0, cmd.getArgs().length);
    }

    /**
     * Проверка того, что команды нечувствительны к регистру.
     */
    @Test
    public void testParseCaseInsensitive() {
        Command cmd1 = parser.parse("game 5, user W, comp B");
        Command cmd2 = parser.parse("Game 5, user W, comp B");
        Command cmd3 = parser.parse("GAME 5, user W, comp B");

        assertEquals("Команды должны быть нечувствительны к регистру",
                CommandType.GAME, cmd1.getType());
        assertEquals("Команды должны быть нечувствительны к регистру",
                CommandType.GAME, cmd2.getType());
        assertEquals("Команды должны быть нечувствительны к регистру",
                CommandType.GAME, cmd3.getType());
    }

    /**
     * Проверка обработки пустой строки, строки с пробелами и null.
     */
    @Test
    public void testParseEmptyString() {
        Command cmd1 = parser.parse("");
        Command cmd2 = parser.parse("   ");
        Command cmd3 = parser.parse(null);

        assertEquals("Пустая строка должна возвращать UNKNOWN",
                CommandType.UNKNOWN, cmd1.getType());
        assertEquals("Строка с пробелами должна возвращать UNKNOWN",
                CommandType.UNKNOWN, cmd2.getType());
        assertEquals("null должен возвращать UNKNOWN",
                CommandType.UNKNOWN, cmd3.getType());
    }

    /**
     * Проверка обработки неизвестной команды.
     */
    @Test
    public void testParseUnknownCommand() {
        Command cmd = parser.parse("INVALID_COMMAND");

        assertEquals("Неизвестная команда должна возвращать UNKNOWN",
                CommandType.UNKNOWN, cmd.getType());
    }

    /**
     * Проверка команды GAME с лишними пробелами и правильного очищения аргументов.
     */
    @Test
    public void testParseGameCommandWithExtraSpaces() {
        Command cmd = parser.parse("GAME   5  ,   user   W  ,   comp   B   ");

        assertEquals("Должен распознать команду GAME с лишними пробелами",
                CommandType.GAME, cmd.getType());
        assertEquals("Должно быть 3 аргумента", 3, cmd.getArgs().length);
        assertEquals("Аргументы должны быть очищены от лишних пробелов",
                "5", cmd.getArgs()[0]);
        assertEquals("Аргументы должны быть очищены от лишних пробелов",
                "user   W", cmd.getArgs()[1]);
    }
}
