package ru.fcpsr.informationtable.utils;

public class NamingUtil {
    private static NamingUtil instance = null;

    private final String refreshName;
    private final String accessName;

    protected NamingUtil(){
        refreshName = "information-table-refresh-token";
        accessName = "information-table-access-token";
    }

    public static NamingUtil getInstance(){
        if(instance == null){
            instance = new NamingUtil();
        }
        return instance;
    }

    public String getRefreshName() {
        return refreshName;
    }

    public String getAccessName() {
        return accessName;
    }
}
