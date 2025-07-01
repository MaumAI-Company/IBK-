package kr.co.ibk.model;

import kr.co.ibk.domain.enums.BatchTargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BatchInferenceModelForm {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RegisterForm {
        private List<BatchTargetItem> targets;
        private String modId;
        private LocalDateTime modDt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BatchTargetItem {
        private BatchTargetType target;
        private Integer modelId;
    }

}
