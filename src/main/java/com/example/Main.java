package com.example;

import com.example.command.CommandFactory;
import com.example.dao.BookDao;
import com.example.dao.BookingDao;
import com.example.dao.ReaderDao;
import com.example.model.Book;
import com.example.model.Booking;
import com.example.model.Reader;
import com.example.service.LibraryService;

import java.util.Scanner;

/**
 * Главный класс приложения Library Management Console App.
 * <p>
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: запуск приложения и управление циклом CLI
 * - Не отвечает за бизнес-логику (использует LibraryService)
 * - Не отвечает за хранение данных (использует DAO)
 * - Не отвечает за создание команд (использует CommandFactory)
 * <p>
 * ПРИНЦИП SOLID: Dependency Inversion Principle (DIP)
 * - Класс зависит от абстракций (сервисы, DAO), а не от конкретных реализаций БД
 * <p>
 * ПРИНЦИП SOLID: Open/Closed Principle (OCP)
 * - Класс открыт для расширения (можно добавить новую логику запуска)
 *
 * @author Your Name
 * @version 1.0.0
 */
public class Main {

    // ==================== КОНСТАНТЫ ====================

    /**
     * Версия приложения
     */
    private static final String APP_VERSION = "1.0.0";

    /**
     * Название приложения
     */
    private static final String APP_NAME = "Library Management Console App";

    // ==================== МЕТОДЫ ====================

    /**
     * Главный метод приложения - точка входа.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за запуск приложения
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(String.format("       %s", APP_NAME));
        System.out.println(String.format("       Версия: %s", APP_VERSION));
        System.out.println("========================================");

        // Запуск приложения
        new Main().run();
    }

    /**
     * Метод для запуска приложения.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за запуск CLI-цикла
     * <p>
     * ПРИНЦИП SOLID: Dependency Inversion Principle
     * - Метод использует абстракции (Service, DAO, Command), а не конкретные реализации
     */
    public void run() {
        // Создание DAO (хранилища данных)
        BookDao bookDao = new BookDao();
        ReaderDao readerDao = new ReaderDao();
        BookingDao bookingDao = new BookingDao();

        // Добавление тестовых данных (опционально)
        addTestData(bookDao, readerDao, bookingDao);

        // Создание сервиса (бизнес-логика)
        LibraryService libraryService = new LibraryService(bookDao, readerDao, bookingDao);

        // Создание фабрики команд (CLI)
        CommandFactory commandFactory = new CommandFactory(libraryService);

        // Создание Scanner для чтения ввода пользователя
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nПриложение запущено! Добро пожаловать в библиотеку.\n");

        // Основной цикл CLI
        while (true) {
            try {
                // Отображение меню
                System.out.println(commandFactory.showMenu());

                // Получение выбора пользователя
                System.out.print("Выберите команду (номер): ");
                String input = scanner.nextLine().trim();

                // Проверка на пустой ввод
                if (input.isEmpty()) {
                    System.out.println("Ошибка: Введите номер команды!\n");
                    continue;
                }

                // Парсинг номера команды
                int commandNumber;
                try {
                    commandNumber = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: Введите число!\n");
                    continue;
                }

                // Выполнение команды
                String result = commandFactory.executeCommand(commandNumber, scanner);
                System.out.println(result);

                // Проверка на выход
                if (commandNumber == commandFactory.getCommands().size()) {
                    break;
                }

                System.out.println();

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + "\n");
            }
        }

        // Закрытие Scanner
        scanner.close();

        System.out.println("Приложение завершено.");
    }

    /**
     * Метод для добавления тестовых данных в хранилище.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за добавление тестовых данных
     *
     * @param bookDao    DAO для книг
     * @param readerDao  DAO для читателей
     * @param bookingDao DAO для бронирований
     */
    private void addTestData(BookDao bookDao, ReaderDao readerDao, BookingDao bookingDao) {
        // Добавление тестовых книг
        com.example.model.Book book1 = new Book(
                "Война и мир",
                "Лев Толстой",
                1869,
                "978-5-268-01234-5"
        );
        bookDao.create(book1);

        com.example.model.Book book2 = new Book(
                "Преступление и наказание",
                "Фёдор Достоевский",
                1866,
                "978-5-268-05678-9"
        );
        bookDao.create(book2);

        com.example.model.Book book3 = new Book(
                "Мастер и Маргарита",
                "Михаил Булгаков",
                1967,
                "978-5-268-09012-3"
        );
        bookDao.create(book3);

        // Добавление тестowych читателей
        com.example.model.Reader reader1 = new Reader(
                "Иван Петров",
                "+7-900-123-4567"
        );
        readerDao.create(reader1);

        com.example.model.Reader reader2 = new Reader(
                "Анна Смирнова",
                "+7-900-765-4321"
        );
        readerDao.create(reader2);

        // Добавление тестового бронирования
        com.example.model.Booking booking1 = new Booking(book1, reader1);
        bookingDao.create(booking1);

        System.out.println("✓ Добавлено тестовых данных: 3 книги, 2 читателя, 1 бронирование");
    }
}