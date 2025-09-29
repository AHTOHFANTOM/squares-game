package com.task1.console;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Board представляет игровое поле для игры "Квадраты".
 * Каждая клетка может быть пустой ('.'), белой ('W') или черной ('B').
 * Предоставляет методы для выполнения ходов, проверки победы и получения свободных клеток.
 */
public class Board {
    /** Размер доски (NxN) */
    public final int size;

    /** Двумерный массив, представляющий клетки доски */
    public final char[][] grid;

    /**
     * Конструктор для создания пустой доски заданного размера.
     * Все клетки инициализируются как пустые ('.').
     *
     * @param size размер доски (должен быть > 2)
     */
    public Board(int size) {
        this.size = size;
        this.grid = new char[size][size];
        // Инициализация всех клеток пустыми значениями
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '.';
            }
        }
    }

    /**
     * Конструктор копирования для создания новой доски на основе существующей.
     * Используется для симуляции ходов без изменения оригинальной доски.
     *
     * @param other исходная доска для копирования
     */
    public Board(Board other) {
        this.size = other.size;
        this.grid = new char[size][size];
        // Копируем каждую клетку
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.grid[i][j] = other.grid[i][j];
            }
        }
    }

    /**
     * Выполняет ход на доске.
     *
     * @param x координата X (столбец)
     * @param y координата Y (строка)
     * @param color цвет фишки ('W' или 'B')
     * @return true, если ход выполнен успешно; false, если клетка занята или координаты некорректны
     */
    public boolean makeMove(int x, int y, char color) {
        if (x < 0 || y < 0 || x >= size || y >= size || grid[y][x] != '.') {
            return false; // некорректный ход
        }
        grid[y][x] = color;
        return true;
    }

    /**
     * Возвращает значение клетки по координатам.
     *
     * @param x координата X
     * @param y координата Y
     * @return символ клетки ('W', 'B', '.'), или '#' если координаты вне доски
     */
    public char getCell(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) {
            return '#'; // за пределами доски
        }
        return grid[y][x];
    }

    /**
     * Проверяет, заполнена ли доска полностью.
     *
     * @return true, если все клетки заняты; false, если есть свободные
     */
    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == '.') return false;
            }
        }
        return true;
    }

    /**
     * Возвращает список свободных клеток на доске.
     *
     * @return список координат [x, y] всех пустых клеток
     */
    public List<int[]> getFreeCells() {
        List<int[]> freeCells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == '.') {
                    freeCells.add(new int[]{j, i}); // добавляем координаты в формате [x, y]
                }
            }
        }
        return freeCells;
    }

    /**
     * Проверяет, образована ли фигура квадрат для заданного цвета.
     *
     * @param color цвет фишек ('W' или 'B')
     * @return true, если квадрат найден; false в противном случае
     */
    public boolean hasSquare(char color) {
        // Проверяем квадраты по осям
        if (checkAxisSquares(color)) return true;

        // Проверяем "поворотные" квадраты (по диагонали и смещенные)
        return checkRotatedSquares(color);
    }

    /**
     * Проверяет наличие квадратов, выровненных по осям доски.
     *
     * @param color цвет фишки
     * @return true, если найден квадрат
     */
    private boolean checkAxisSquares(char color) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != color) continue;

                // Малый квадрат 2x2
                if (i + 1 < size && j + 1 < size) {
                    if (grid[i + 1][j] == color &&
                            grid[i][j + 1] == color &&
                            grid[i + 1][j + 1] == color) {
                        return true;
                    }
                }

                // Больший квадрат с шагом 2
                if (i + 2 < size && j + 2 < size) {
                    if (grid[i + 2][j] == color &&
                            grid[i][j + 2] == color &&
                            grid[i + 2][j + 2] == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Проверяет наличие "поворотных" квадратов (крестом вокруг центральной точки).
     *
     * @param color цвет фишки
     * @return true, если найден квадрат
     */
    private boolean checkRotatedSquares(char color) {
        // Малые крестовые квадраты с шагом 1
        for (int r = 1; r < size - 1; r++) {
            for (int c = 1; c < size - 1; c++) {
                if (grid[r - 1][c] == color &&
                        grid[r + 1][c] == color &&
                        grid[r][c - 1] == color &&
                        grid[r][c + 1] == color) {
                    return true;
                }
            }
        }

        // Большие крестовые квадраты с шагом 2
        for (int r = 2; r < size - 2; r++) {
            for (int c = 2; c < size - 2; c++) {
                if (grid[r - 2][c] == color &&
                        grid[r + 2][c] == color &&
                        grid[r][c - 2] == color &&
                        grid[r][c + 2] == color) {
                    return true;
                }
            }
        }

        return false;
    }
}
