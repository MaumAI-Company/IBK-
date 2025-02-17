package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.common.utils.RSAEncryptor;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.util.HashMap;


@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping("/soulGod/user/")
    public String index(Model model,
                        @CurrentUser MemberInfo memberInfo, HttpServletRequest req) {

        RSAEncryptor rsa = new RSAEncryptor();
        rsa.initRsa(req);

        model.addAttribute("mc", "user");
        model.addAttribute("pageTitle", "계정");
        model.addAttribute("memberInfo", memberInfo);
        return "/soulGod/user/index";

    }

    @PostMapping(value = {"/soulGod/user/changeUserPassword"})
    @ResponseBody
    public HashMap<String, Object> changeUserPassword(@RequestBody HashMap<String, Object> paramMap) {
        log.info("##### URI :: { changeUserPassword } #####");

        // RSA 복호화
        String userIdRSA = (String) paramMap.get("userId");
        String chkPwdRSA = (String) paramMap.get("chkPwd");
        String newPwdRSA = (String) paramMap.get("newPwd");

        // 데이터
        String userId = "";
        String chkPwd = "";
        String newPwd = "";

        PrivateKey privateKey = (PrivateKey) RSAEncryptor.RSA_PRIVATE_KEY;// 복호화

        try {
            userId = RSAEncryptor.decryptRsa(privateKey, userIdRSA);
            chkPwd = RSAEncryptor.decryptRsa(privateKey, chkPwdRSA);
            newPwd = RSAEncryptor.decryptRsa(privateKey, newPwdRSA);
        } catch (Exception e) {
        }
        // 비밀번호 변경로직 실행 후 결과반환
        HashMap<String, Object> map = userService.changeUserPassword(userId, chkPwd, newPwd);

        return map;
    }

    /**
     * 기존 비밀번호와 새 비밀번호 일치 여부 (true=일치)
     */
    @PostMapping(value = {"/soulGod/user/confirmNewPassword"})
    @ResponseBody
    public HashMap<String, Object> confirmNewPassword(@RequestBody HashMap<String, Object> paramMap) {

        // RSA 복호화
        String userIdRSA = (String) paramMap.get("userId");
        String chkPwdRSA = (String) paramMap.get("chkPwd");
        String newPwdRSA = (String) paramMap.get("newPwd");

        // 데이터
        String userId = "";
        String chkPwd = "";
        String newPwd = "";

        PrivateKey privateKey = (PrivateKey) RSAEncryptor.RSA_PRIVATE_KEY;// 복호화

        try {
            userId = RSAEncryptor.decryptRsa(privateKey, userIdRSA);
            chkPwd = RSAEncryptor.decryptRsa(privateKey, chkPwdRSA);
            newPwd = RSAEncryptor.decryptRsa(privateKey, newPwdRSA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userService.confirmNewPassword(userId, chkPwd, newPwd);
    }
}
