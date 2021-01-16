package com.tasktracker.tasks.models;

public enum Statuses {
    DISTRIBUTED ("Раздается"),
    TAKEN ("Взята"),
    COMPLETED ("Выполнена");

    private String enumName;

    Statuses(String enumName) {
        this.enumName = enumName;
    }

    public String getEnumName() {
        return enumName;
    }
}
