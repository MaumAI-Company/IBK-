package kr.co.ibk.domain.web;

import kr.co.ibk.domain.enums.BatchTargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatchInferenceModelInfo {
    private BatchTargetType target;
    private Integer modelId;
    private String modId;
    private String modDt;
}