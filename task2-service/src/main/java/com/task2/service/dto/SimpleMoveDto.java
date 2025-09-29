package com.task2.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO для передачи информации о конкретном ходе в игре.
 * Используется в REST API для передачи координат и цвета игрока, который делает ход.
 */
public class SimpleMoveDto {

    /**
     * Координата X хода на доске (столбец).
     */
    @JsonProperty("x")
    private int x;

    /**
     * Координата Y хода на доске (строка).
     */
    @JsonProperty("y")
    private int y;

    /**
     * Цвет игрока, сделавшего ход:
     * - "W" - белый
     * - "B" - черный
     */
    @JsonProperty("color")
    private String color;

    /**
     * Пустой конструктор, необходимый для сериализации/десериализации JSON.
     */
    public SimpleMoveDto() {}

    /**
     * Конструктор с инициализацией всех полей.
     *
     * @param x координата X (столбец) хода
     * @param y координата Y (строка) хода
     * @param color  цвет игрока ("W" или "B")
     */
    public SimpleMoveDto(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Геттеры и Сеттеры для полей данного класса
     */
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Переопределение метода toString для удобного логирования и отладки.
     * @return строковое представление объекта SimpleMoveDto
     */
    @Override
    public String toString() {
        return "SimpleMoveDto{" +
                "x=" + x +
                ", y=" + y +
                ", color='" + color + '\'' +
                '}';
    }
}
