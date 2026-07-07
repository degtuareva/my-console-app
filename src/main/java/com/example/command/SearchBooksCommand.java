package com.example.command;

import com.example.service.LibraryService;
import com.example.model.Book;
import java.util.List;
import java.util.Scanner;

/**
 * Класс SearchBooksCommand - команда для поиска книг.
 *
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: поиск книг
 *
 * ПРИНЦИП SOLID: Open/Closed Principle (OCP)
 * - Класс открыт для расширения (можно добавить поиск по другим полям)
 *
 * @author Your Name
 * @version 1.0.0
 */
public class SearchBooksCommand implements Command {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /** Сервис для работы с библиотекой */
    private final LibraryService libraryService;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор с зависимостью.
     *
     * @param libraryService сервис для работы с библиотекой
     */
    public SearchBooksCommand(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // ==================== МЕТОДЫ ИНТЕРФЕЙСА Command ====================

    /**
     * Метод для выполнения команды поиска книг.
     *
     * @param scanner объект Scanner для чтения ввода пользователя
     * @return результат выполнения команды (список найденных книг)
     */
    @Override
    public String execute(Scanner scanner) {
        System.out.println("\n=== Поиск книг ===");
        System.out.println("1. Поиск по названию");
        System.out.println("2. Поиск по автору");
        System.out.print("Выберите способ поиска (1-2): ");

        String choice = scanner.nextLine().trim();
        String searchTerm;
        List<Book> results;

        // Выбор способа поиска
        if ("1".equals(choice)) {
            System.out.print("Введите название для поиска: ");
            searchTerm = scanner.nextLine().trim();
            results = libraryService.searchBooksByTitle(searchTerm);
        } else if ("2".equals(choice)) {
            System.out.print("Введите имя автора для поиска: ");
            searchTerm = scanner.nextLine().trim();
            results = libraryService.searchBooksByAuthor(searchTerm);
        } else {
            return "Ошибка: Неверный выбор способа поиска!";
        }

        // Проверка на пустоту
        if (searchTerm.isEmpty()) {
            return "Ошибка: Поисковый запрос не может быть пустым!";
        }

        // Отображение результатов
        if (results.isEmpty()) {
            return String.format("Книг по запросу \"%s\" не найдено.", searchTerm);
        }

        StringBuilder result = new StringBuilder();
        result.append(String.format("Найдено книг: %d\n\n", results.size()));

        for (Book book : results) {
            result.append(String.format(
                    "[%d] %s\n",
                    book.getId(),
                    book.toString()
            ));
        }

        return result.toString();
    }

    /**
     * Метод для получения названия команды.
     *
     * @return название команды
     */
    @Override
    public String getName() {
        return "Поиск книг";
    }

    /**
     * Метод для получения описания команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "Поиск книг по названию или автору";
    }
}