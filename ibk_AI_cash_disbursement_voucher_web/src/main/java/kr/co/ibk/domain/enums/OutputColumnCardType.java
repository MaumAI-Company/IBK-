package kr.co.ibk.domain.enums;

public enum OutputColumnCardType {
    BDMN_ITEX_MNGM_NO("예산관리비목관리번호", "bdmnItexMngmNo", 1),
    BDGT_PRFR_RSN_FRCS_CON("예산집행사유코드", "bdgtPrfrRsnFrcsCon", 2), //예산관리비목관리번호_예산집행사유코드
    BDGT_BSNS_FRCS_CON("사업세부사업", "bdgtBsnsFrcsCon", 3);
    final private String name;
    final private String camelColumn;
    final private Integer sno;


    public String getName() {
        return name;
    }

    public String getCamelColumn() {
        return camelColumn;
    }

    public Integer getSno() {
        return sno;
    }

    OutputColumnCardType(String name, String camelColumn, Integer sno) {
        this.name = name;
        this.camelColumn = camelColumn;
        this.sno = sno;
    }
}