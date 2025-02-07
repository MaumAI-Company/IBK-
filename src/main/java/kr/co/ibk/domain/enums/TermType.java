package kr.co.ibk.domain.enums;

public enum TermType {
    HALF("반기", 24),
    QUARTER("분기", 12),
    MONTHLY("매월", 4),
    WEEKLY("매주", 1);

    final private String name;
    final private Integer week;

    public String getName() {
        return name;
    }

    public Integer getWeek() {
        return week;
    }

    private TermType(String name, Integer week) {
        this.name = name;
        this.week = week;
    }

}