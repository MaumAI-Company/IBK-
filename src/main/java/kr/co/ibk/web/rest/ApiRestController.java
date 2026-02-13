package kr.co.ibk.web.rest;

import kr.co.ibk.common.ResponseDto;
import kr.co.ibk.common.utils.StringHelper;
import kr.co.ibk.domain.enums.ResultCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/o")
@RequiredArgsConstructor
public class ApiRestController {

    @PostMapping("/check-engine")
    public ResponseEntity<?> checkEngine() {

        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", null), HttpStatus.OK);
    }

    @PostMapping("/check-ip")
    public ResponseEntity<?> checkIP(HttpServletRequest request) {
        String clientIp = StringHelper.getClientIP(request);
        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", clientIp), HttpStatus.OK);
    }

}
