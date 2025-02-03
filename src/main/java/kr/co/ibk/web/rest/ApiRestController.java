package kr.co.ibk.web.rest;

import kr.co.ibk.common.ResponseDto;
import kr.co.ibk.domain.enums.ResultCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/o")
@RequiredArgsConstructor
public class ApiRestController {

    @PostMapping("/check-engine")
    public ResponseEntity<?> saveToken() {

        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", null), HttpStatus.OK);
    }
}
