package kr.co.ibk.domain.enums;

public enum LearningType {
    CARD("BC카드"),
    BILL("세금계산서");

    final private String name;

    public String getName() {
        return name;
    }

    private LearningType(String name) {
        this.name = name;
    }

}