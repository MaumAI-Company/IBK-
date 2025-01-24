package kr.co.ibk.domain.enums;

public enum InputColumnCardType {
    BRCD("부점코드", "brcd", true, 1),
    CDN("카드번호", "cdn", true, 2),
    BDGT_TSTM_USE_HMS("승인시간", "bdgtTstmUseHms", true, 3),
    AMSL_AMT("매출금액", "amslAmt", true, 4),
    AFST_NM("가맹점명", "afstNm", true, 5),
    TPBS_NM("업종명", "tpbsNm", true, 6),
    BZDY_YN("영업일여부", "bzdyYn", true, 7),
    AFST_DTL_ADR("가맹점상세주소", "afstDtlAdr", true, 8),
    BRNC_ADR("부점주소", "brncAdr", true, 9),
    AFST_BZN("가맹점사업자등록번호", "afstBzn", false, 10),
    AMSL_AFST_NO("매출가맹점번호", "amslAfstNo", false, 11),
    AFST_TPBCD("가맹점업종코드", "afstTpbcd", false, 12);

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

    InputColumnCardType(String name, String camelColumn, boolean isChecked, Integer sno) {
        this.name = name;
        this.camelColumn = camelColumn;
        this.isChecked = isChecked;
        this.sno = sno;
    }
}