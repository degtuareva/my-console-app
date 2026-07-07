package com.example.command;

import com.example.service.LibraryService;

import java.util.Scanner;

/**
 * Класс AddBookCommand - команда для добавления новой книги.
 * <p>
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: обработка команды добавления книги
 * - Не отвечает за бизнес-логику (использует LibraryService)
 * - Не отвечает за хранение данных (использует сервис)
 * <p>
 * ПРИНЦИП SOLID: Dependency Inversion Principle (DIP)
 * - Класс зависит от абстракций (LibraryService, Command), а не от конкретных реализаций
 *
 * @author Your Name
 * @version 1.0.0
 */
public class AddBookCommand implements Command {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /**
     * Сервис для работы с библиотекой
     */
    private final LibraryService libraryService;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор с зависимостью (LibraryService).
     *
     * @param libraryService сервис для работы с библиотекой
     */
    public AddBookCommand(LibraryService libraryService) {
        if (libraryService == null) {
            throw new IllegalArgumentException("LibraryService не может быть null");
        }
        this.libraryService = libraryService;
    }

    // ==================== МЕТОДЫ ИНТЕРФЕЙСА Command ====================

    /**
     * Метод для выполнения команды добавления книги.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за обработку команды добавления книги
     * - Собирает данные от пользователя, вызывает сервис, возвращает результат
     *
     * @param scanner объект Scanner для чтения ввода пользователя
     * @return результат выполнения команды
     */
    @Override
    public String execute(Scanner scanner) {
        System.out.println("\n=== Добавление новой книги ===");

        // Сбор данных о книге от пользователя
        System.out.print("Введите название книги: ");
        String title = scanner.nextLine().trim();

        System.out.print("Введите имя автора: ");
        String author = scanner.nextLine().trim();

        System.out.print("Введите год издания: ");
        String yearInput = scanner.nextLine().trim();
        Integer year = parseYear(yearInput);

        System.out.print("Введите ISBN книги: ");
        String isbn = scanner.nextLine().trim();

        // Проверка ввода
        if (title.isEmpty() || author.isEmpty() || year == null || isbn.isEmpty()) {
            return "Ошибка: Все поля должны быть заполнены!";
        }

        // Добавление книги через сервис
        try {
            com.example.model.Book book = libraryService.addBook(title, author, year, isbn);
            return String.format(
                    "✓ Книга успешно добавлена!\n%s",
                    book.toString()
            );
        } catch (IllegalArgumentException e) {
            return "Ошибка: " + e.getMessage();
        } catch (Exception e) {
            return "Ошибка при добавлении книги: " + e.getMessage();
        }
    }

    /**
     * Метод для получения названия команды.
     *
     * @return название команды
     */
    @Override
    public String getName() {
        return "Добавить книгу";
    }

    /**
     * Метод для получения описания команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "Добавить новую книгу в библиотеку";
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Метод для парсинга года издания из строки.
     *
     * @param yearInput строка с годом
     * @return Integer с годом или null если ошибка
     */
    private Integer parseYear(String yearInput) {
        try {
            Integer year = Integer.parseInt(yearInput);
            // Проверка на диапазон (можно усилить)
            if (year < 1000 || year > 2027) {
                System.out.println("Примечание: Год вне типичного диапазона, но продолжим...");
            }
            return year;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}