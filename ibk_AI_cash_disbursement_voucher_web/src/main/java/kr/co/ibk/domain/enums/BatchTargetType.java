package kr.co.ibk.domain.enums;

public enum BatchTargetType {
    CARD_HQ("카드_본부"),
    CARD_BR("카드_영업점"),
    BILL_INTEGRATED("세금계산서_통합");

    final private String name;

    public String getName() {
        return name;
    }

    private BatchTargetType(String name) {
        this.name = name;
    }
}
