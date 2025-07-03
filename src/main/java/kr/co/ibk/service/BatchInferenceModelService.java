package kr.co.ibk.service;

import kr.co.ibk.common.exceptions.CustomApiException;
import kr.co.ibk.domain.enums.BatchTargetType;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.BatchInferenceModelForm;
import kr.co.ibk.repository.BatchInferenceModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BatchInferenceModelService extends _BaseService {
    private final BatchInferenceModelRepository batchInferenceModelRepository;

    @Value("${Globals.fileStorePath}")
    private String filepath;

    @Value("${Globals.batchInfer.fileName}")
    private String batchInferFileName;

    @Value("${Globals.batchInfer.ext}")
    private String batchInferFileExt;

    @Value("${Globals.batchInfer.backupFileStorePath}")
    private String backupFileStorePath;

    /**
     * 배치 추론 모델 정보 저장
     * - 기존 target에 해당하는 데이터를 삭제
     * - 새로 지정된 모델 ID를 insert
     * - BC카드 본부, 영업점에 대해 스크립트를 생성
     */
    @Transactional
    public HashMap<String, Object> save(BatchInferenceModelForm.RegisterForm form, MemberInfo memberInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "FAIL");

        Long saveCnt = 0L;
        String modId = memberInfo.getMemId();

        Integer cardHqModelId = null;
        Integer cardBrModelId = null;
        Integer billModelId = null;

        for (BatchInferenceModelForm.BatchTargetItem item : form.getTargets()) {
            BatchTargetType target = item.getTarget();
            Integer modelId = item.getModelId();

            // 기존 target 데이터 삭제
            batchInferenceModelRepository.deleteByTarget(target);

            // insert
            if (modelId != null) {
                saveCnt += batchInferenceModelRepository.insert(target.name(), modelId, modId);

                // 스크립트 생성을 위한 본부/영업점/세금계산서 모델 ID 저장
                if (target == BatchTargetType.CARD_HQ) {
                    cardHqModelId = modelId;
                } else if (target == BatchTargetType.CARD_BR) {
                    cardBrModelId = modelId;
                } else if (target == BatchTargetType.BILL_INTEGRATED) {
                    billModelId = modelId;
                }
            }
        }

        if (cardHqModelId == null || cardBrModelId == null || billModelId == null) {
            throw new CustomApiException("배치 추론 모델을 지정해주세요.");
        }

        // BC CARD 스크립트 생성
        createBatchScript(cardHqModelId, cardBrModelId);
        // TODO: 세금계산서 스크립트 작업

        if (saveCnt > 0) {
            map.put("status", "SUCCESS");
        }
        return map;
    }

    /**
     * 배치 스크립트 생성
     * - 기존 파일은 백업
     * - 새로 생성 후 POSIX 권한(755) 부여 (POSIX 지원 환경만)
     */
    public void createBatchScript(Integer hqModelId, Integer brModelId) {

        try {
            String batchInferTemplate = "#!/bin/bash\n" +
                    "\n" +
                    "# API URL\n" +
                    "API_URL=\"http://127.0.0.1:9001/mcc-classify-batch/\"\n" +
                    "\n" +
                    "# 본부 요청\n" +
                    "headquarters_payload='{\n" +
                    "  \"model_id\":\"##hqModelId##\",\n" +
                    "  \"hdqr_bob_dcd\": \"1\"\n" +
                    "}'\n" +
                    "\n" +
                    "# 영업점 요청\n" +
                    "branch_payload='{\n" +
                    "  \"model_id\":\"##brModelId##\",\n" +
                    "  \"hdqr_bob_dcd\": \"2\"\n" +
                    "}'\n" +
                    "\n" +
                    "send_request() {\n" +
                    "        local payload=\"$1\"\n" +
                    "        local target=\"$2\"\n" +
                    "\n" +
                    "        echo \"target check $target\"\n" +
                    "        echo \"payload check $payload\"\n" +
                    "        echo \"Sending request for $target...\"\n" +
                    "\n" +
                    "        response=$(curl -s -o response.json -w \"%{http_code}\" -X POST $API_URL \\\n" +
                    "                   -H \"Content-Type: application/json\" \\\n" +
                    "                   -d \"$payload\")\n" +
                    "\n" +
                    "        if [[ \"$response\" -eq 200 ]]; then\n" +
                    "                echo \"$target request succeeded.\"\n" +
                    "        elif [[ \"$response\" -eq 500 ]]; then\n" +
                    "                echo \"[ERROR] $target request failed with HTTP 500 Internal Server Error;\" >&2\n" +
                    "                cat response.json\n" +
                    "                exit 1\n" +
                    "        else\n" +
                    "                echo \"[WARNING] $target request returned HTTP status code $response\"\n" +
                    "                exit 1\n" +
                    "        fi\n" +
                    "}\n" +
                    "\n" +
                    "# Headquarter 요청\n" +
                    "send_request \"$headquarters_payload\" \"Headquarters\"\n" +
                    "\n" +
                    "# Branch 요청\n" +
                    "send_request \"$branch_payload\" \"Branch\"\n" +
                    "\n" +
                    "echo \"Batch requests completed.\"\n";

            // 값 치환
            String resultScript = batchInferTemplate
                    .replace("##hqModelId##", !ObjectUtils.isEmpty(hqModelId) ? hqModelId.toString() : "")
                    .replace("##brModelId##", !ObjectUtils.isEmpty(brModelId) ? brModelId.toString() : "");

            // 파일 경로
            Path targetPath = Paths.get(filepath, batchInferFileName + batchInferFileExt);

            // 기존 파일 백업 (batch_infer_backup_20250627000000000.sh)
            if (Files.exists(targetPath)) {
                String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
                        .format(LocalDateTime.now());

                String backupBatchInferFileName = batchInferFileName + "_backup_" + timestamp + batchInferFileExt;
                Path backupPath = Paths.get(backupFileStorePath, backupBatchInferFileName);

                // 백업 폴더 없으면 생성
                if (!Files.exists(Paths.get(backupFileStorePath))) {
                    Files.createDirectories(Paths.get(backupFileStorePath));
                }

                Files.copy(targetPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 파일 저장
            Files.writeString(targetPath, resultScript, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            // 권한 설정 (755)
            if (Files.getFileStore(targetPath).supportsFileAttributeView("posix")) {
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
                Files.setPosixFilePermissions(targetPath, perms);
            }

        } catch (IOException e) {
            throw new CustomApiException("스크립트 생성에 실패하였습니다.");
        }
    }

    /**
     * 배치 추론 모델 ID 조회
     * - 각 BatchTargetType 별 지정된 모델 ID 반환
     */
    public HashMap<String, Object> getBatchInference() {
        HashMap<String, Object> result = new HashMap<>();

        batchInferenceModelRepository.findByTarget(BatchTargetType.CARD_HQ)
                .ifPresent(item -> result.put("cardHqModelId", item.getModelId()));

        batchInferenceModelRepository.findByTarget(BatchTargetType.CARD_BR)
                .ifPresent(item -> result.put("cardBrModelId", item.getModelId()));

        batchInferenceModelRepository.findByTarget(BatchTargetType.BILL_INTEGRATED)
                .ifPresent(item -> result.put("billModelId", item.getModelId()));

        return result;
    }

}
