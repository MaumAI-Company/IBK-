package kr.co.ibk.domain.enums;

public enum InputColumnBillType {
    BRCD("부점코드", "brcd", true, 1),
    TXBL_DCD("세금계산서구분코드", "txblDcd", true, 2),
    SPRL_BSNN_NO("공급자사업자번호", "splrBsnnNo", true, 3),
    SPLR_FRM("공급자상호명", "splrFrm", true, 4),
    SPLR_BZST_NM("공급자업태명", "splrBzstNm", true, 5),
    SPLR_ITMS_NM("공급자종목명", "splrItmsNm", true, 6),
    ISS_AMT("발행금액", "issAmt", true, 7),
    TXBL_LSAR_NM("세금계산서품목명", "txblLsarNm", true, 8);

    final private String name;
    final private String camelColumn;
    final private boolean isChecked;
    final private Integer sno;

    public String getName() {
        return name;
    }

    public String getCamelColumn() {
        return camelColumn;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public Integer getSno() {
        return sno;
    }

    InputColumnBillType(String name, String camelColumn, boolean isChecked, Integer sno) {
        this.name = name;
        this.camelColumn = camelColumn;
        this.isChecked = isChecked;
        this.sno = sno;
    }
}