package com.example.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс Booking (Бронирование) - представляет сущность бронирования книги читателем.
 * <p>
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: описывать бронирование
 * - Связывает сущности Book и Reader
 * <p>
 * ПРИНЦИП SOLID: Dependency Inversion Principle (DIP)
 * - Класс зависит от абстракций (Book, Reader), а не от конкретных реализаций
 *
 * @author Your Name
 * @version 1.0.0
 */
public class Booking {

    // ==================== КОНСТАНТЫ ====================

    /**
     * Максимальный срок бронирования в днях (30 дней)
     */
    private static final int MAX_BOOKING_DAYS = 30;

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /**
     * Уникальный идентификатор бронирования (Primary Key)
     */
    private Long id;

    /**
     * Книга, которую бронировали
     */
    private Book book;

    /**
     * Читатель, который бронировал книгу
     */
    private Reader reader;

    /**
     * Дата бронирования
     */
    private LocalDate bookingDate;

    /**
     * Дата возврата книги (может быть null, если книга еще не возвращена)
     */
    private LocalDate returnDate;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор по умолчанию (пустой).
     */
    public Booking() {
        // Дата бронирования устанавливается по умолчанию как текущая
        this.bookingDate = LocalDate.now();
    }

    /**
     * Конструктор с основными параметрами (book, reader).
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Конструктор отвечает только за создание объекта бронирования
     *
     * @param book   книга для бронирования (не null)
     * @param reader читатель, который бронировал (не null)
     */
    public Booking(Book book, Reader reader) {
        setBook(book);
        setReader(reader);
        // Дата бронирования устанавливается по умолчанию как текущая
        this.bookingDate = LocalDate.now();
    }

    /**
     * Конструктор с полным набором параметров.
     *
     * @param id          уникальный идентификатор бронирования
     * @param book        книга для бронирования
     * @param reader      читатель, который бронировал
     * @param bookingDate дата бронирования
     * @param returnDate  дата возврата книги (может быть null)
     */
    public Booking(Long id, Book book, Reader reader, LocalDate bookingDate, LocalDate returnDate) {
        this.id = id;
        setBook(book);
        setReader(reader);
        setBookingDate(bookingDate);
        setReturnDate(returnDate);
    }

    // ==================== МЕТОДЫ GETTER И SETTER ====================

    /**
     * Getter для поля id.
     *
     * @return уникальный идентификатор бронирования
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter для поля id.
     *
     * @param id уникальный идентификатор бронирования
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter для поля book.
     *
     * @return книга бронирования
     */
    public Book getBook() {
        return book;
    }

    /**
     * Setter для поля book с проверкой данных.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за установку книги с валидацией
     *
     * @param book книга для бронирования
     * @throws IllegalArgumentException если книга null
     */
    public void setBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Книга не может быть null");
        }
        this.book = book;
    }

    /**
     * Getter для поля reader.
     *
     * @return читатель бронирования
     */
    public Reader getReader() {
        return reader;
    }

    /**
     * Setter для поля reader с проверкой данных.
     *
     * @param reader читатель, который бронировал
     * @throws IllegalArgumentException если читатель null
     */
    public void setReader(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Читатель не может быть null");
        }
        this.reader = reader;
    }

    /**
     * Getter для поля bookingDate.
     *
     * @return дата бронирования
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    /**
     * Setter для поля bookingDate с проверкой данных.
     *
     * @param bookingDate дата бронирования
     * @throws IllegalArgumentException если дата null или будущая
     */
    public void setBookingDate(LocalDate bookingDate) {
        if (bookingDate == null) {
            throw new IllegalArgumentException("Дата бронирования не может быть null");
        }

        if (bookingDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата бронирования не может быть в будущем");
        }

        this.bookingDate = bookingDate;
    }

    /**
     * Getter для поля returnDate.
     *
     * @return дата возврата книги (может быть null)
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Setter для поля returnDate с проверкой данных.
     *
     * @param returnDate дата возврата книги (может быть null)
     * @throws IllegalArgumentException если дата до даты бронирования
     */
    public void setReturnDate(LocalDate returnDate) {
        // null допускается (книга еще не возвращена)
        if (returnDate == null) {
            this.returnDate = null;
            return;
        }

        // Проверка: дата возврата не может быть раньше даты бронирования
        if (returnDate.isBefore(bookingDate)) {
            throw new IllegalArgumentException(
                    "Дата возврата не может быть раньше даты бронирования"
            );
        }

        this.returnDate = returnDate;
    }

    // ==================== БИЗНЕС-МЕТОДЫ ====================

    /**
     * Метод для проверки, бронирование еще активно (книга не возвращена).
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за проверку статуса бронирования
     *
     * @return true если книга еще не возвращена, false если возвращена
     */
    public boolean isActive() {
        return returnDate == null;
    }

    /**
     * Метод для возврата книги (установка даты возврата).
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за возврат книги
     * <p>
     * ПРИНЦИП SOLID: Open/Closed Principle
     * - Метод открыт для расширения (можно добавить логику штрафов)
     *
     * @return true если книга успешно возвращена, false если уже возвращена
     */
    public boolean returnBook() {
        // Если книга уже возвращена, ничего не делаем
        if (!isActive()) {
            return false;
        }

        // Установка даты возврата как текущая дата
        this.returnDate = LocalDate.now();
        return true;
    }

    /**
     * Метод для получения числа дней бронирования.
     *
     * @return число дней от бронирования до возврата (или до текущей даты)
     */
    public int getBookingDays() {
        LocalDate endDate = isActive() ? LocalDate.now() : returnDate;
        return (int) (endDate.toEpochDay() - bookingDate.toEpochDay());
    }

    // ==================== ПЕРЕОПРЕДЕЛЕНИЕ МЕТОДОВ ====================

    /**
     * Переопределяет метод toString() для удобного вывода бронирования.
     *
     * @return строковое представление бронирования
     */
    @Override
    public String toString() {
        return String.format(
                "Booking{id=%d, book='%s', reader='%s', bookingDate=%s, returnDate=%s, active=%s}",
                id != null ? id : "null",
                book != null ? book.getTitle() : "null",
                reader != null ? reader.getName() : "null",
                bookingDate,
                returnDate,
                isActive()
        );
    }

    /**
     * Переопределяет метод equals() для сравнения бронирований.
     *
     * @param obj объект для сравнения
     * @return true если объекты равны, false иначе
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Booking)) {
            return false;
        }

        Booking other = (Booking) obj;

        // Сравнение по id
        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        // Если id null, сравнение по book, reader и bookingDate
        return Objects.equals(book, other.book) &&
                Objects.equals(reader, other.reader) &&
                Objects.equals(bookingDate, other.bookingDate);
    }

    /**
     * Переопределяет метод hashCode() для использования в хеш-таблицах.
     *
     * @return хеш-код объекта
     */
    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }

        return Objects.hash(book, reader, bookingDate);
    }
}