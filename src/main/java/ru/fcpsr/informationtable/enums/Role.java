package ru.fcpsr.informationtable.enums;

public enum Role {
    ADMIN("Администратор"),
    USER("Пользователь");

    private final String title;
    Role(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
