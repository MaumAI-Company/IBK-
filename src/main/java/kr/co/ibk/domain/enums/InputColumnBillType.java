package kr.co.ibk.domain.enums;

public enum InputColumnBillType {
    BRCD("부점코드", "brcd", true),
    TXBL_DCD("세금계산서구분코드", "txblDcd", true),
    SPRL_BSNN_NO("공급자사업자번호", "sprlBsnnNo", true),
    SPLR_FRM("공급자상호명", "splrFrm", true),
    SPLR_BZST_NM("공급자업태명", "splrBzstNm", true),
    SPLR_ITMS_NM("공급자종목명", "splrItmsNm", true),
    ISS_AMT("발행금액", "issAmt", true),
    TXBL_LSAR_NM("세금계산서품목명", "txblLsarNm", true);

    final private String name;
    final private String camelColumn;
    final private boolean isChecked;

    public String getName() {
        return name;
    }

    public String getCamelColumn() {
        return camelColumn;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    private InputColumnBillType(String name, String camelColumn, boolean isChecked) {
        this.name = name;
        this.camelColumn = camelColumn;
        this.isChecked = isChecked;
    }

}