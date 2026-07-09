package kr.co.ibk.domain.enums;

public enum ResultCodeType {
    SUCCESS("성공", "00"),
    FAIL("실패", "01");

    final private String name;
    final private String code;

    public String getName() {
        return name;
    }

    public String code() {
        return code;
    }

    ResultCodeType(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
