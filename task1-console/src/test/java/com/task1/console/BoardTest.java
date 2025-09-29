package com.task1.console;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Юнит-тесты для класса Board.
 * Проверяют корректность создания доски, ходы, проверку на квадраты и состояние заполненности.
 */
public class BoardTest {

    /** Экземпляр доски, который используется в каждом тесте */
    private Board board;

    /**
     * Метод, выполняющийся перед каждым тестом.
     * Создает новую доску размером 5x5.
     */
    @Before
    public void setUp() {
        board = new Board(5);
    }

    /**
     * Проверка корректности работы конструктора копирования.
     * Изменения в оригинальной доске не должны влиять на копию.
     */
    @Test
    public void testCopyConstructorCreatesIndependentBoard() {
        board.makeMove(0, 0, 'W');
        Board copy = new Board(board);

        // Проверяем, что копия имеет тот же цвет в ячейке (0,0)
        assertEquals('W', copy.getCell(0, 0));

        // Изменяем оригинал, проверяем, что копия осталась неизменной
        board.makeMove(1, 1, 'B');
        assertEquals("Копия должна оставаться неизменной", '.', copy.getCell(1, 1));
    }

    /**
     * Проверка метода getCell.
     * Проверяется корректность возвращаемого цвета для пустых и заполненных ячеек,
     * а также поведение при выходе за границы доски.
     */
    @Test
    public void testGetCell() {
        assertEquals("В пустой ячейке должен быть '.'", '.', board.getCell(2, 2));
        board.makeMove(2, 2, 'B');
        assertEquals("После хода должен быть цвет", 'B', board.getCell(2, 2));

        // Проверка выхода за границы
        assertEquals("Выход за границу должен вернуть '#'", '#', board.getCell(-1, 0));
        assertEquals("Выход за границу должен вернуть '#'", '#', board.getCell(0, -1));
        assertEquals("Выход за границу должен вернуть '#'", '#', board.getCell(5, 0));
        assertEquals("Выход за границу должен вернуть '#'", '#', board.getCell(0, 5));
    }

    /**
     * Проверка того, что пустая доска не содержит квадраты.
     */
    @Test
    public void testEmptyBoardHasNoSquare() {
        assertFalse("Пустая доска не должна содержать квадратов", board.hasSquare('W'));
        assertFalse("Пустая доска не должна содержать квадратов", board.hasSquare('B'));
    }

    /**
     * Проверка распознавания повернутого квадрата 2x2 (крест).
     */
    @Test
    public void testRotatedSquare2x2Cross() {
        board.makeMove(2, 1, 'W');
        board.makeMove(2, 3, 'W');
        board.makeMove(1, 2, 'W');
        board.makeMove(3, 2, 'W');

        assertTrue("Должен распознаваться повернутый квадрат 2x2 (крест)", board.hasSquare('W'));
    }

    /**
     * Проверка распознавания повернутого квадрата 3x3 (крест).
     */
    @Test
    public void testRotatedSquare3x3Cross() {
        board.makeMove(2, 0, 'B');
        board.makeMove(2, 4, 'B');
        board.makeMove(0, 2, 'B');
        board.makeMove(4, 2, 'B');

        assertTrue("Должен распознаваться повернутый квадрат 3x3 (крест)", board.hasSquare('B'));
    }

    /**
     * Проверка корректности выполнения ходов.
     * Должны работать валидные ходы и отвергаться некорректные.
     */
    @Test
    public void testMakeValidAndInvalidMoves() {
        assertTrue(board.makeMove(0, 0, 'W'));
        assertEquals('W', board.getCell(0, 0));

        // Попытка хода в занятую клетку
        assertFalse("Не должен ходить в занятую клетку", board.makeMove(0, 0, 'B'));

        // Попытка хода за пределы доски
        assertFalse("Не должен ходить за пределы", board.makeMove(-1, 0, 'W'));
        assertFalse("Не должен ходить за пределы", board.makeMove(0, 5, 'W'));
    }

    /**
     * Проверка метода isFull.
     * Должен возвращать true только тогда, когда доска полностью заполнена.
     */
    @Test
    public void testIsFullBoard() {
        assertFalse(board.isFull());

        // Заполняем всю доску
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board.makeMove(i, j, (i + j) % 2 == 0 ? 'W' : 'B');
            }
        }
        assertTrue(board.isFull());
    }

    /**
     * Проверка метода getFreeCells.
     * Должен возвращать все пустые клетки, корректно обновляться после ходов.
     */
    @Test
    public void testGetFreeCells() {
        List<int[]> free = board.getFreeCells();
        assertEquals(25, free.size());

        board.makeMove(2, 2, 'W');
        free = board.getFreeCells();
        assertEquals(24, free.size());
    }
}
