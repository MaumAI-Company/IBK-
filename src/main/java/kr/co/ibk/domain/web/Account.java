package kr.co.ibk.domain.web;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {

    static final long serialVersionUID = 1L;
    private Long mngrPid;
    private String mngrNm;
    private String loginId;
    private String pwd;
    private String regPsId;
    private LocalDateTime regDtm;
    private String updPsId;
    private LocalDateTime updDtm;
    private String delAt;
    private String useAt;


    @Builder
    public Account(Long mngrPid, String mngrNm, String loginId, String pwd, String delAt, String useAt) {
        this.mngrPid = mngrPid;
        this.mngrNm = mngrNm;
        this.loginId = loginId;
        this.pwd = pwd;
        this.delAt = delAt;
        this.useAt = useAt;
    }

    public void encodingPwd(PasswordEncoder pwdEncoder) {
        this.pwd = pwdEncoder.encode(this.pwd);
    }
}
