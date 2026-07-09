package kr.co.ibk.domain.enums;

public enum UserRoleType {
    ADMIN("관리자"),
    NORMAL("일반사용자");

    final private String name;

    public String getName() {
        return name;
    }

    private UserRoleType(String name){
        this.name = name;
    }
}
