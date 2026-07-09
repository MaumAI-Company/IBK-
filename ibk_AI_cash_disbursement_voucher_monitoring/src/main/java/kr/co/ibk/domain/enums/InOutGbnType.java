package kr.co.ibk.domain.enums;

public enum InOutGbnType {
    INPUT("인풋"),
    OUTPUT("아웃풋");

    final private String name;

    public String getName() {
        return name;
    }

    private InOutGbnType(String name) {
        this.name = name;
    }

}