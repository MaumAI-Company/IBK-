package com.mindslab.web.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class RoleVO implements Serializable{

    /* HC_ROLE */
    private String roleSeq; // '역할일련번호'
    private String roleId; // '역할식별자'
    private String roleType; // '역할유형'
    private String roleName; // '역할명'
    private String roleUseYn; // '사용: Y, 미사용: N'
    private String roleRegId; // '등록자'
    private Date roleRegDt; // '등록일시'
    private String roleModId; // '변경자'
    private Date roleModDt; // '변경일시'
    private String roleColumn1; // 커스텀컬럼
    private String roleColumn2; // 커스텀컬럼
    private String roleColumn3; // 커스텀컬럼
    private String roleColumn4; // 커스텀컬럼
    private String roleColumn5; // 커스텀컬럼

}
