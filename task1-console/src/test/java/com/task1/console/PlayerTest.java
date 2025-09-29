package com.task1.console;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Юнит-тесты для класса Player.
 * Проверяют корректность инициализации игрока, определение типа (пользователь/компьютер) и цвета.
 */
public class PlayerTest {

    /**
     * Проверка создания обычного пользователя (не компьютер) и корректного цвета.
     */
    @Test
    public void testUserPlayer() {
        Player player = new Player("user", 'W');

        assertFalse("Пользователь не должен быть компьютером", player.isComputer());
        assertEquals("Цвет должен быть W", 'W', player.getColor());
    }

    /**
     * Проверка создания игрока-компьютера и корректного цвета.
     */
    @Test
    public void testComputerPlayer() {
        Player player = new Player("comp", 'B');

        assertTrue("Компьютер должен быть компьютером", player.isComputer());
        assertEquals("Цвет должен быть B", 'B', player.getColor());
    }

    /**
     * Проверка нечувствительности к регистру при определении типа игрока.
     */
    @Test
    public void testCaseInsensitiveType() {
        Player player1 = new Player("USER", 'W');
        Player player2 = new Player("user", 'W');
        Player player3 = new Player("User", 'W');
        Player player4 = new Player("COMP", 'B');
        Player player5 = new Player("comp", 'B');
        Player player6 = new Player("Comp", 'B');

        assertFalse("USER должен распознаваться как пользователь", player1.isComputer());
        assertFalse("user должен распознаваться как пользователь", player2.isComputer());
        assertFalse("User должен распознаваться как пользователь", player3.isComputer());
        assertTrue("COMP должен распознаваться как компьютер", player4.isComputer());
        assertTrue("comp должен распознаваться как компьютер", player5.isComputer());
        assertTrue("Comp должен распознаваться как компьютер", player6.isComputer());
    }

    /**
     * Проверка корректности установки цвета игрока.
     */
    @Test
    public void testPlayerColors() {
        Player whitePlayer = new Player("user", 'W');
        Player blackPlayer = new Player("comp", 'B');

        assertEquals("Белый игрок должен иметь цвет W", 'W', whitePlayer.getColor());
        assertEquals("Черный игрок должен иметь цвет B", 'B', blackPlayer.getColor());
    }
}
