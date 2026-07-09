package kr.co.ibk.domain.enums;

public enum FileDvType {

    DELIVERY("배송"),
    PRODUCTION("제작"),
    ATTACHED("첨부파일");

    final private String name;

    public String getName() {
        return name;
    }

    private FileDvType(String name) {
        this.name = name;
    }
}
