package kr.co.ibk.domain.enums;

public enum OutputColumnBillType {
    BDGT_ITEX_FRCS_CON("예산관리비목번호", "bdgtItexFrcsCon"),
    BDGT_PRFR_RSN_FRCS_CON("예산집행사유코드", "bdgtPrfrRsnFrcsCon"), //예산관리비목관리번호_예산집행사유코드
    BDGT_BSNS_FRCS_CON("사업세부사업", "bdgtBsnsFrcsCon");

    final private String name;
    final private String camelColumn;

    public String getName() {
        return name;
    }

    public String getCamelColumn() {
        return camelColumn;
    }

    private OutputColumnBillType(String name, String camelColumn) {
        this.name = name;
        this.camelColumn = camelColumn;
    }

}