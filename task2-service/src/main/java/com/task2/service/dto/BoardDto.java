package com.task2.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO для передачи состояния доски через REST API.
 * Содержит размер доски, её текущее состояние и цвет следующего игрока.
 */
public class BoardDto {

    /**
     * Размер доски (количество строк и столбцов, т.к. доска квадратная).
     */
    @JsonProperty("size")
    private int size;

    /**
     * Состояние доски в виде строки.
     * Символы:
     * 'W' или 'w' - белая фишка
     * 'B' или 'b' - черная фишка
     * '.' - пустая клетка
     * Длина строки должна быть size*size.
     */
    @JsonProperty("data")
    private String data;

    /**
     * Цвет следующего игрока, который должен сделать ход.
     * Допустимые значения: "w" или "b" (независимо от регистра).
     */
    @JsonProperty("nextPlayerColor")
    private String nextPlayerColor;

    /**
     * Пустой конструктор, необходим для сериализации/десериализации JSON.
     */
    public BoardDto() {}

    /**
     * Конструктор с инициализацией всех полей.
     *
     * @param size Размер доски
     * @param data Строковое представление доски
     * @param nextPlayerColor Цвет следующего игрока
     */
    public BoardDto(int size, String data, String nextPlayerColor) {
        this.size = size;
        this.data = data;
        this.nextPlayerColor = nextPlayerColor;
    }

    /**
     * Геттеры и Сеттеры для полей данного класса
     */
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public String getData() {return data;}
    public void setData(String data) {
        this.data = data;
    }


    public String getNextPlayerColor() {
        return nextPlayerColor;
    }
    public void setNextPlayerColor(String nextPlayerColor) {
        this.nextPlayerColor = nextPlayerColor;
    }

    /**
     * Переопределение метода toString для удобного логирования и отладки.
     *
     * @return строковое представление объекта BoardDto
     */
    @Override
    public String toString() {
        return "BoardDto{" +
                "size=" + size +
                ", data='" + data + '\'' +
                ", nextPlayerColor='" + nextPlayerColor + '\'' +
                '}';
    }
}
