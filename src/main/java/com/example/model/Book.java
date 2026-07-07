package com.example.model;

import java.util.Objects;

/**
 * Класс Book (Книга) - представляет сущность книги в библиотеке.
 *
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: описывать книгу
 * - Не отвечает за бизнес-логику, хранение или отображение
 *
 * ПРИНЦИП SOLID: Interface Segregation Principle (ISP)
 * - Класс не зависит от лишних методов, только от необходимых
 *
 * @author Your Name
 * @version 1.0.0
 */
public class Book {

    // ==================== КОНСТАНТЫ ====================

    /** Максимальная длина названия книги */
    private static final int MAX_TITLE_LENGTH = 200;

    /** Максимальная длина имени автора */
    private static final int MAX_AUTHOR_LENGTH = 100;

    /** Минимальный год издания (примерно) */
    private static final int MIN_YEAR = 1000;

    /** Максимальный год издания (текущий + 1) */
    private static final int MAX_YEAR = 2027;

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /** Уникальный идентификатор книги (Primary Key) */
    private Long id;

    /** Название книги */
    private String title;

    /** Имя автора книги */
    private String author;

    /** Год издания книги */
    private Integer year;

    /** ISBN книги (уникальный идентификатор издательства) */
    private String isbn;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор по умолчанию (пустой).
     * Используется для создания объекта без данных, например, при десериализации.
     */
    public Book() {
    }

    /**
     * Конструктор с основными параметрами (title, author, year, isbn).
     *
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Конструктор отвечает только за создание объекта с заданными данными
     *
     * @param title название книги (не null, не пустое, длина <= 200)
     * @param author имя автора (не null, не пустое, длина <= 100)
     * @param year год издания (от 1000 до 2027)
     * @param isbn ISBN книги (не null, не пустое)
     */
    public Book(String title, String author, Integer year, String isbn) {
        // Проверка и установка названия книги
        setTitle(title);

        // Проверка и установка имени автора
        setAuthor(author);

        // Проверка и установка года издания
        setYear(year);

        // Проверка и установка ISBN
        setIsbn(isbn);
    }

    /**
     * Конструктор с полным набором параметров (включая id).
     *
     * @param id уникальный идентификатор книги
     * @param title название книги
     * @param author имя автора
     * @param year год издания
     * @param isbn ISBN книги
     */
    public Book(Long id, String title, String author, Integer year, String isbn) {
        // Установка идентификатора (может быть null для новых объектов)
        this.id = id;

        // Проверка и установка названия книги
        setTitle(title);

        // Проверка и установка имени автора
        setAuthor(author);

        // Проверка и установка года издания
        setYear(year);

        // Проверка и установка ISBN
        setIsbn(isbn);
    }

    // ==================== МЕТОДЫ GETTER И SETTER ====================

    /**
     * Getter для поля id.
     *
     * @return уникальный идентификатор книги (может быть null)
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter для поля id.
     *
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за установку идентификатора
     *
     * @param id уникальный идентификатор книги
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter для поля title.
     *
     * @return название книги
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter для поля title с проверкой данных.
     *
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за установку названия с валидацией
     *
     * @param title название книги
     * @throws IllegalArgumentException если название не пустое, null или слишком длинное
     */
    public void setTitle(String title) {
        // Проверка на null
        if (title == null) {
            throw new IllegalArgumentException("Название книги не может быть null");
        }

        // Проверка на пустое строку
        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("Название книги не может быть пустым");
        }

        // Проверка на длину
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(
                    "Название книги слишком длинное (максимум " + MAX_TITLE_LENGTH + " символов)"
            );
        }

        // Установка названия (очищенного от лишних пробелов)
        this.title = title.trim();
    }

    /**
     * Getter для поля author.
     *
     * @return имя автора книги
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter для поля author с проверкой данных.
     *
     * @param author имя автора книги
     * @throws IllegalArgumentException если автор не пустое, null или слишком длинное
     */
    public void setAuthor(String author) {
        // Проверка на null
        if (author == null) {
            throw new IllegalArgumentException("Имя автора не может быть null");
        }

        // Проверка на пустое строку
        if (author.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя автора не может быть пустым");
        }

        // Проверка на длину
        if (author.length() > MAX_AUTHOR_LENGTH) {
            throw new IllegalArgumentException(
                    "Имя автора слишком длинное (максимум " + MAX_AUTHOR_LENGTH + " символов)"
            );
        }

        // Установка имени автора (очищенного от лишних пробелов)
        this.author = author.trim();
    }

    /**
     * Getter для поля year.
     *
     * @return год издания книги
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Setter для поля year с проверкой данных.
     *
     * @param year год издания книги
     * @throws IllegalArgumentException если год не в диапазоне [1000, 2027]
     */
    public void setYear(Integer year) {
        // Проверка на null
        if (year == null) {
            throw new IllegalArgumentException("Год издания не может быть null");
        }

        // Проверка на диапазон
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new IllegalArgumentException(
                    "Год издания должен быть от " + MIN_YEAR + " до " + MAX_YEAR
            );
        }

        // Установка года
        this.year = year;
    }

    /**
     * Getter для поля isbn.
     *
     * @return ISBN книги
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Setter для поля isbn с проверкой данных.
     *
     * @param isbn ISBN книги
     * @throws IllegalArgumentException если ISBN не пустое, null
     */
    public void setIsbn(String isbn) {
        // Проверка на null
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN не может быть null");
        }

        // Проверка на пустое строку
        if (isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN не может быть пустым");
        }

        // Установка ISBN (очищенного от лишних пробелов)
        this.isbn = isbn.trim();
    }

    // ==================== ПЕРЕОПРЕДЕЛЕНИЕ МЕТОДОВ ====================

    /**
     * Переопределяет метод toString() для удобного вывода книги.
     *
     * ПРИНЦИП SOLID: Open/Closed Principle (OCP)
     * - Класс открыт для расширения (можно переопределить метод)
     * - Класс закрыт для изменения (не нужно менять внутренний код)
     *
     * @return строковое представление книги
     */
    @Override
    public String toString() {
        return String.format(
                "Book{id=%d, title='%s', author='%s', year=%d, isbn='%s'}",
                id != null ? id : "null",
                title,
                author,
                year,
                isbn
        );
    }

    /**
     * Переопределяет метод equals() для сравнения книг.
     *
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за сравнение объектов Book
     *
     * @param obj объект для сравнения
     * @return true если объекты равны, false иначе
     */
    @Override
    public boolean equals(Object obj) {
        // Проверка на ссылку на сам объект
        if (this == obj) {
            return true;
        }

        // Проверка на null
        if (obj == null) {
            return false;
        }

        // Проверка на тот же класс
        if (!(obj instanceof Book)) {
            return false;
        }

        // Преобразование к типу Book
        Book other = (Book) obj;

        // Сравнение по id (если id не null)
        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        // Если id null, сравнение по isbn (уникальный идентификатор книги)
        return isbn != null && isbn.equals(other.isbn);
    }

    /**
     * Переопределяет метод hashCode() для использования в хеш-таблицах.
     *
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за вычисление хеш-кода
     *
     * @return хеш-код объекта
     */
    @Override
    public int hashCode() {
        // Если id не null, используем его
        if (id != null) {
            return id.hashCode();
        }

        // Если id null, используем isbn
        return isbn != null ? isbn.hashCode() : 0;
    }
}