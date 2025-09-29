package com.task2.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO для передачи статуса игры через REST API.
 * Содержит информацию о текущем состоянии игры и её результате.
 */
public class GameStatusDto {

    /**
     * Статус игры:
     * - "ongoing" - игра продолжается
     * - "finished" - игра завершена
     */
    @JsonProperty("status")
    private String status;

    /**
     * Результат игры, может быть null, если игра продолжается.
     * Примеры значений:
     * - "W wins" - победа белого
     * - "B wins" - победа черного
     * - "Draw" - ничья
     */
    @JsonProperty("result")
    private String result;

    /**
     * Пустой конструктор, необходимый для сериализации/десериализации JSON.
     */
    public GameStatusDto() {}

    /**
     * Конструктор с инициализацией всех полей.
     *
     * @param status Статус игры ("ongoing" или "finished")
     * @param result Результат игры (например, "W wins", "B wins", "Draw")
     */
    public GameStatusDto(String status, String result) {
        this.status = status;
        this.result = result;
    }

    /**
     * Геттеры и Сеттеры для полей данного класса
     */
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Переопределение метода toString для удобного логирования и отладки.
     * @return строковое представление объекта GameStatusDto
     */
    @Override
    public String toString() {
        return "GameStatusDto{" +
                "status='" + status + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
