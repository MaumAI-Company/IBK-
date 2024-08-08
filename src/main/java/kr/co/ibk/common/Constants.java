package kr.co.ibk.common;

public class Constants {

    public static final String SYSTEM = "system";

    /*페이징*/
    public static final int DEFAULT_PAGESIZE = 10;
    public static final int[] PAGE_SIZES = {10, 20, 30, 50, 100, 0};    // 0 : 전체

    /*첨부파일*/
    public static final Long imageMaxFileSize = 1024 * 500L;       //이미지 업로드 최대 사이즈 (Byte)
    public static final Long bigImageMaxFileSize = 1024 * 1024 * 10L;    //큰 이미지 업로드 최대 사이즈 (Byte)
    public static final Long docMaxFileSize = 1024 * 1024 * 10L;    //문서 업로드 최대 사이즈 (Byte)
    public static final String allowExtFileImg = "jpg,jpeg,gif,png,bmp";
    public static final String allowExtFileDoc = "doc,xls,docx,xlsx,ppt,pptx,hwp,pdf";
    public static final String allowExtFileBusi = "jpg,jpeg,pdf";

    // 파일 경로
    public static final String FOLDERNAME_GOODS = "goods";
    public static final String FOLDERNAME_ORDER = "order";
    public static final String FOLDERNAME_ORDER_PRODUCTION = "/production";
    public static final String FOLDERNAME_ORDER_DELIVERY = "/delivery";

    /**
     * AES 암복호화 key
     */
    public static final String AES_ENCRYPT_KEY_SVMS_CPS = "svms15pi7eor9cps";










}
