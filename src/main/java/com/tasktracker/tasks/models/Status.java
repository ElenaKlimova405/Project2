package com.tasktracker.tasks.models;

public enum Status {
    DISTRIBUTED("Раздается"),
    TAKEN("Взята"),
    COMPLETED("Выполнена");

    private String enumName;

    Status(String enumName) {
        this.enumName = enumName;
    }

    public String getEnumName() {
        return enumName;
    }
}
