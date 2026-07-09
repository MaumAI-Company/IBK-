package kr.co.ibk.domain.enums;

public enum TermType {
    WEEKLY("1주 (매주)", 1),
    MONTHLY("4주 (매월)", 4),
    QUARTER("12주 (분기)", 12),
    HALF("24주 (반기)", 24);

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