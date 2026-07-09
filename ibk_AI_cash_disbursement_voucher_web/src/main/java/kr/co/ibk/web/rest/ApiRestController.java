package kr.co.ibk.web.rest;

import kr.co.ibk.common.ResponseDto;
import kr.co.ibk.common.utils.StringHelper;
import kr.co.ibk.domain.enums.ResultCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/o")
@RequiredArgsConstructor
public class ApiRestController {

    @PostMapping("/check-engine")
    public ResponseEntity<?> checkEngine() {

        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", null), HttpStatus.OK);
    }

    @GetMapping("/check-ip")
    public ResponseEntity<?> checkIP(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        String X_Forwarded_For = StringHelper.getHeaderIp(request, "X-Forwarded-For");
        String Proxy_Client_IP = StringHelper.getHeaderIp(request, "Proxy-Client-IP");
        String WL_Proxy_Client_IP = StringHelper.getHeaderIp(request, "WL-Proxy-Client-IP");
        String HTTP_CLIENT_IP = StringHelper.getHeaderIp(request, "HTTP_CLIENT_IP");
        String HTTP_X_FORWARDED_FOR = StringHelper.getHeaderIp(request, "HTTP_X_FORWARDED_FOR");
        String ip = request.getRemoteAddr();

        map.put("X_Forwarded_For", X_Forwarded_For);
        map.put("Proxy_Client_IP", Proxy_Client_IP);
        map.put("WL_Proxy_Client_IP", WL_Proxy_Client_IP);
        map.put("HTTP_CLIENT_IP", HTTP_CLIENT_IP);
        map.put("HTTP_X_FORWARDED_FOR", HTTP_X_FORWARDED_FOR);
        map.put("ip", ip);

        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", map), HttpStatus.OK);
    }

}
