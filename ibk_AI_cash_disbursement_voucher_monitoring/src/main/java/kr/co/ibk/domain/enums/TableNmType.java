package kr.co.ibk.domain.enums;

public enum TableNmType {
    TBL_GOODS("상품","tbl_goods"),
    TBL_ORDER("주문","tbl_order")
    ;

    final private String name;
    private final String value;

    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }

    private TableNmType(String name, String value){
        this.name = name;
        this.value = value;
    }

}