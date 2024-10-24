package kr.co.ibk.domain.enums;

public enum OutputColumnType {
    AFST_BZN("가맹점사업자등록번호", "afstBzn"),
    AMSL_AFST_NO("매출가맹점번호", "amslAfstNo"),
    AFST_TPBCD("가맹점업종코드", "afstTpbcd");

    final private String name;
    final private String camelColumn;

    public String getName() {
        return name;
    }

    public String getCamelColumn() {
        return camelColumn;
    }

    private OutputColumnType(String name, String camelColumn) {
        this.name = name;
        this.camelColumn = camelColumn;
    }

}