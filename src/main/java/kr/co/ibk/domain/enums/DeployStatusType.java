package kr.co.ibk.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum DeployStatusType {
    REGISTER_COMPLETE(0, "등록 완료"),
    LEARN_DATA_ERROR(1, "학습 데이터 생성 오류"),
    LEARN_ING(2, "학습 중"),
    LEARN_COMPLETE(3, "학습 완료"),
    LEARN_ERROR(4, "학습 오류"),
    DEPLOY_ING(5, "배포 중"),
//    DEPLOY_COMPLETE(6, "배포 완료"),
    DEPLOY_STOP(7, "배포 중지"),
    DEPLOY_FAIL(8, "배포 실패");

    private final Integer code;
    private final String statusNm;

    DeployStatusType(Integer code, String statusNm) {
        this.code = code;
        this.statusNm = statusNm;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatusNm() {
        return statusNm;
    }

    public static List<DeployStatusType> getDeployStatusList() {
        return Arrays.asList(LEARN_COMPLETE, DEPLOY_ING, DEPLOY_STOP, DEPLOY_FAIL);
    }
}
