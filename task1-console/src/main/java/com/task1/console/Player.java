package com.task1.console;

/**
 * Класс Player представляет игрока в игре "Квадраты".
 * Игрок может быть пользователем или компьютером и имеет цвет фишек.
 */
public class Player {

    /** Флаг, указывающий, является ли игрок компьютером */
    private final boolean computer;

    /** Цвет фишек игрока: 'W' (белые) или 'B' (черные) */
    private final char color;

    /**
     * Конструктор игрока.
     *
     * @param type  строка, определяющая тип игрока: "user" или "comp"
     * @param color символ, определяющий цвет фишек ('W' или 'B')
     */
    public Player(String type, char color) {
        // Игрок считается компьютером, если type = "comp" (регистр не важен)
        this.computer = type.equalsIgnoreCase("comp");

        // Цвет фишек задается напрямую
        this.color = color;
    }

    /**
     * Метод для проверки, является ли игрок компьютером.
     *
     * @return true, если игрок компьютер, иначе false
     */
    public boolean isComputer() {
        return computer;
    }

    /**
     * Метод для получения цвета фишек игрока.
     *
     * @return цвет фишек ('W' или 'B')
     */
    public char getColor() {
        return color;
    }
}
