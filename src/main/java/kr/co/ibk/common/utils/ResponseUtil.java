package kr.co.ibk.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

public interface ResponseUtil {

    static <T> ResponseEntity<T> wrapOrNotFound(T response) {
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok().body(response);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
