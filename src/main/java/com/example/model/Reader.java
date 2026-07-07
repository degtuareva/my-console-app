package com.example.model;

import java.util.Objects;

/**
 * Класс Reader (Читатель) - представляет сущность читателя в библиотеке.
 *
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: описывать читателя
 * - Не отвечает за бизнес-логику, хранение или отображение
 *
 * @author Your Name
 * @version 1.0.0
 */
public class Reader {

    // ==================== КОНСТАНТЫ ====================

    /** Максимальная длина имени читателя */
    private static final int MAX_NAME_LENGTH = 100;

    /** Максимальная длина телефона */
    private static final int MAX_PHONE_LENGTH = 20;

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /** Уникальный идентификатор читателя (Primary Key) */
    private Long id;

    /** Имя читателя */
    private String name;

    /** Телефон читателя */
    private String phone;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор по умолчанию (пустой).
     */
    public Reader() {
    }

    /**
     * Конструктор с основными параметрами (name, phone).
     *
     * @param name имя читателя (не null, не пустое)
     * @param phone телефон читателя (не null, не пустое)
     */
    public Reader(String name, String phone) {
        setName(name);
        setPhone(phone);
    }

    /**
     * Конструктор с полным набором параметров (включая id).
     *
     * @param id уникальный идентификатор читателя
     * @param name имя читателя
     * @param phone телефон читателя
     */
    public Reader(Long id, String name, String phone) {
        this.id = id;
        setName(name);
        setPhone(phone);
    }

    // ==================== МЕТОДЫ GETTER И SETTER ====================

    /**
     * Getter для поля id.
     *
     * @return уникальный идентификатор читателя
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter для поля id.
     *
     * @param id уникальный идентификатор читателя
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter для поля name.
     *
     * @return имя читателя
     */
    public String getName() {
        return name;
    }

    /**
     * Setter для поля name с проверкой данных.
     *
     * @param name имя читателя
     * @throws IllegalArgumentException если имя не пустое, null или слишком длинное
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Имя читателя не может быть null");
        }

        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя читателя не может быть пустым");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "Имя читателя слишком длинное (максимум " + MAX_NAME_LENGTH + " символов)"
            );
        }

        this.name = name.trim();
    }

    /**
     * Getter для поля phone.
     *
     * @return телефон читателя
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter для поля phone с проверкой данных.
     *
     * @param phone телефон читателя
     * @throws IllegalArgumentException если телефон не пустое, null или слишком длинное
     */
    public void setPhone(String phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Телефон не может быть null");
        }

        if (phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Телефон не может быть пустым");
        }

        if (phone.length() > MAX_PHONE_LENGTH) {
            throw new IllegalArgumentException(
                    "Телефон слишком длинный (максимум " + MAX_PHONE_LENGTH + " символов)"
            );
        }

        this.phone = phone.trim();
    }

    // ==================== ПЕРЕОПРЕДЕЛЕНИЕ МЕТОДОВ ====================

    /**
     * Переопределяет метод toString() для удобного вывода читателя.
     *
     * @return строковое представление читателя
     */
    @Override
    public String toString() {
        return String.format(
                "Reader{id=%d, name='%s', phone='%s'}",
                id != null ? id : "null",
                name,
                phone
        );
    }

    /**
     * Переопределяет метод equals() для сравнения читателей.
     *
     * @param obj объект для сравнения
     * @return true если объекты равны, false иначе
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Reader)) {
            return false;
        }

        Reader other = (Reader) obj;

        // Сравнение по id
        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        // Если id null, сравнение по имени и телефону
        return Objects.equals(name, other.name) && Objects.equals(phone, other.phone);
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

        return Objects.hash(name, phone);
    }
}