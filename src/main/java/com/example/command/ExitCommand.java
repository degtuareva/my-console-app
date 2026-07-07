package com.example.command;

import java.util.Scanner;

/**
 * Класс ExitCommand - команда для выхода из приложения.
 * <p>
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: обработка команды выхода
 *
 * @author Your Name
 * @version 1.0.0
 */
public class ExitCommand implements Command {

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор по умолчанию.
     */
    public ExitCommand() {
    }

    // ==================== МЕТОДЫ ИНТЕРФЕЙСА Command ====================

    /**
     * Метод для выполнения команды выхода.
     *
     * @param scanner объект Scanner (не используется)
     * @return результат выполнения команды (сообщение о выходе)
     */
    @Override
    public String execute(Scanner scanner) {
        return "Выход из приложения. До свидания!";
    }

    /**
     * Метод для получения названия команды.
     *
     * @return название команды
     */
    @Override
    public String getName() {
        return "Выход";
    }

    /**
     * Метод для получения описания команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "Завершить работу приложения";
    }
}