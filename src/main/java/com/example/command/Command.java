package com.example.command;

/**
 * Интерфейс Command - абстракция для команд CLI.
 *
 * ПРИНЦИП SOLID: Interface Segregation Principle (ISP)
 * - Интерфейс имеет минимальное количество методов, только необходимые
 * - Каждая команда реализует только нужные методы
 *
 * ПРИНЦИП SOLID: Dependency Inversion Principle (DIP)
 * - CLI работает с абстракцией (Command), а не с конкретными реализациями
 *
 * @author Your Name
 * @version 1.0.0
 */
public interface Command {

    /**
     * Метод для выполнения команды.
     *
     * @param scanner объект Scanner для чтения ввода пользователя
     * @return результат выполнения команды (строка для отображения)
     */
    String execute(java.util.Scanner scanner);

    /**
     * Метод для получения названия команды (для отображения в меню).
     *
     * @return название команды
     */
    String getName();

    /**
     * Метод для получения описания команды (для отображения в меню).
     *
     * @return описание команды
     */
    String getDescription();
}