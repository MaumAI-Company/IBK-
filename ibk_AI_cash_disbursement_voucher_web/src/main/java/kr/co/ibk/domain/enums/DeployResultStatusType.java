package kr.co.ibk.domain.enums;

public enum DeployResultStatusType {
    SUCCESS("성공"),
    FAIL("실패");

    final private String name;

    public String getName() {
        return name;
    }

    private DeployResultStatusType(String name) {
        this.name = name;
    }
}
