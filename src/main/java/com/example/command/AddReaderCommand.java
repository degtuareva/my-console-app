package com.example.command;

import com.example.service.LibraryService;
import java.util.Scanner;

/**
 * Класс AddReaderCommand - команда для добавления нового читателя.
 *
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: обработка команды добавления читателя
 *
 * @author Your Name
 * @version 1.0.0
 */
public class AddReaderCommand implements Command {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /** Сервис для работы с библиотекой */
    private final LibraryService libraryService;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор с зависимостью.
     *
     * @param libraryService сервис для работы с библиотекой
     */
    public AddReaderCommand(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // ==================== МЕТОДЫ ИНТЕРФЕЙСА Command ====================

    /**
     * Метод для выполнения команды добавления читателя.
     *
     * @param scanner объект Scanner для чтения ввода пользователя
     * @return результат выполнения команды
     */
    @Override
    public String execute(Scanner scanner) {
        System.out.println("\n=== Добавление нового читателя ===");

        // Сбор данных о читателе
        System.out.print("Введите имя читателя: ");
        String name = scanner.nextLine().trim();

        System.out.print("Введите телефон читателя: ");
        String phone = scanner.nextLine().trim();

        // Проверка ввода
        if (name.isEmpty() || phone.isEmpty()) {
            return "Ошибка: Все поля должны быть заполнены!";
        }

        // Добавление читателя через сервис
        try {
            com.example.model.Reader reader = libraryService.addReader(name, phone);
            return String.format(
                    "✓ Читатель успешно добавлен!\n%s",
                    reader.toString()
            );
        } catch (IllegalArgumentException e) {
            return "Ошибка: " + e.getMessage();
        } catch (Exception e) {
            return "Ошибка при добавлении читателя: " + e.getMessage();
        }
    }

    /**
     * Метод для получения названия команды.
     *
     * @return название команды
     */
    @Override
    public String getName() {
        return "Добавить читателя";
    }

    /**
     * Метод для получения описания команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "Добавить нового читателя в библиотеку";
    }
}