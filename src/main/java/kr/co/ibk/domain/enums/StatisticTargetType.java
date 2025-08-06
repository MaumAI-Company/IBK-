package kr.co.ibk.domain.enums;

public enum StatisticTargetType {
    CARD("카드"),
    BILL("세금계산서");

    final private String name;

    public String getName() {
        return name;
    }

    private StatisticTargetType(String name) {
        this.name = name;
    }
}
