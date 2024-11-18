package kr.co.ibk.domain.enums;

public enum TermType {
    HALF("반기"),
    QUARTER("분기"),
    MONTHLY("매월"),
    WEEKLY("매주");

    final private String name;

    public String getName() {
        return name;
    }

    private TermType(String name) {
        this.name = name;
    }

}