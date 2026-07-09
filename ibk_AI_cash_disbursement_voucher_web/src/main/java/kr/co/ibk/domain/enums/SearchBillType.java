package kr.co.ibk.domain.enums;

public enum SearchBillType {
    BRCD("부점코드"),
    TXBL_DCD("세금계산서구분코드"),
    SPLR_BSNN_NO("공급자사업자번호"),
    SPLR_FRM("공급자상호명"),
    SPLR_BZST_NM("공급자업태명"),
    SPLR_ITMS_NM("공급자종목명"),
    ISS_AMT("발행금액"),
    TXBL_LSAR_NM("세금계산서품목명"),
    BDMN_ITEX_MNGM_NO("예산관리비목관리번호"),
    BDGT_BSNS_FRCS_CON("사업세부사업"),
    BDGT_PRFR_RSN_FRCS_CON("예산집행사유코드"),
    BDGT_EXNS_PAMT_MCD("예산경비지급방법코드"),
    ACIM_CON("계좌정보내용");

    final private String name;

    public String getName() {
        return name;
    }

    private SearchBillType(String name) {
        this.name = name;
    }

}