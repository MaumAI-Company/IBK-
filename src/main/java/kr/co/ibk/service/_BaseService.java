package kr.co.ibk.service;

import kr.co.ibk.common.Base;
import kr.co.ibk.common.exceptions.AppCheckException;
import kr.co.ibk.common.exceptions.AppCheckNoRollbackException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;


// rollbackFor = AppCheckException.class 설정을 해도 RuntimeException 상속이 아니면 안됨.
//@Transactional(readOnly = true, rollbackFor = { RuntimeException.class, Error.class, AppCheckException.class } )
@Transactional(readOnly = true)
public abstract class _BaseService extends Base {

    /**
     * 반드시 체크를 해야하는 오류를 전달함.
     *
     * @param errorMessage
     * @throws AppCheckException
     */
    public void throwAppCheckException(String errorMessage) throws AppCheckException {
        throw new AppCheckException(errorMessage);
    }

    /**
     * 반드시 체크를 해야하는 오류, 롤백안됨.
     *
     * @param errorMessage
     * @throws AppCheckException
     */
    public void throwAppCheckNoRollbackException(String errorMessage) throws AppCheckNoRollbackException {
        throw new AppCheckNoRollbackException(errorMessage);
    }

    public HashMap<String, String> jsonToHashMap(String json) {
        // JSON 문자열을 HashMap으로 변환
        HashMap<String, String> map = new HashMap<>();

        //외부 대괄호 제거
        json = json.substring(1, json.length() - 1);

        String[] pairs = json.split("\\],\\["); // ],[ 기준으로 나누기
        for (String pair : pairs) {
            // 대괄호와 큰따옴표 제거
            pair = pair.replaceAll("[\\[\\]\"]", "");
            String[] entry = pair.split(",", 2); // 첫 쉼표를 기준으로 나눔
            if (entry.length == 2) {
                map.put(entry[0].trim(), entry[1].trim());
            }
        }
        return map;
    }
}
