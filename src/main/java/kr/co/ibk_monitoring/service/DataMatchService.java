package kr.co.ibk_monitoring.service;

import kr.co.ibk_monitoring.domain.web.MemberInfo;
import kr.co.ibk_monitoring.repository.AdminMemberRepository;
import kr.co.ibk_monitoring.repository.DataMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataMatchService {

    private final IbkMailSender ibkMailSender;
    private final DataMatchRepository dataMatchRepository;
    private final AdminMemberRepository adminMemberRepository;

    private final String TITLE_MAIL = "IBK 예산관리시스템 자동지급결의 AI 시스템 장애감지 알림";
    private final String BODY_MAIL = "IBK 예산관리시스템 자동지급결의 AI 시스템 장애감지 알림<br/>" +
            "1. 발생시간 : ##DATETIME##<br/>" +
            "2. 등급 : CRITICAL<br/>" +
            "3. 호스트명 : pmvscbl3<br/>" +
            "4. 메시지그룹 : AI<br/>" +
            "5. 이벤트 내용 : ##TARGET## AI 배치 추론 장애 발생으로, AI 엔진 및 API 컨테이너를 확인해주세요.";

    private final String TITLE_MESSENGER = " [자동지급결의AI시스템] AI 배치 추론(##TARGET##) 과정에서 장애가 발생했습니다.";
    private final String BODY_MESSENGER = "##TARGET## AI 배치 추론이 정상적으로 작동하지 못했습니다.";

    @Value("${Globals.check.dataMatch}")
    private Boolean dataMatchCheck;

    @Value("${Globals.check.mail}")
    private Boolean mailCheck;

    @Value("${Globals.check.messenger}")
    private Boolean messengerCheck;

    @Value("${Globals.messenger.domain}")
    private String messengerDomain;
    @Value("${Globals.messenger.srvCode}")
    private String messengerSrvCode;

    /**
     * 인아웃풋 데이터 적합성 체크 > 불일치 시 메신저/메일 발송
     */
    public void dataMatchCheck() {
        if (dataMatchCheck) {
            boolean isCardMatch = dataMatchRepository.cardCheck();
            boolean isBillMatch = dataMatchRepository.billCheck();

            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss"));

            // 카드 체크 (BC카드)
            if (!isCardMatch) {
                String target = "BC카드";
                String titleMail = TITLE_MAIL.replace("##TARGET##", target);
                String bodyMail = BODY_MAIL
                        .replace("##DATETIME##", now)
                        .replace("##TARGET##", "BC카드");

                String titleMessenger = TITLE_MESSENGER.replace("##TARGET##", target);
                String bodyMessenger = BODY_MESSENGER.replace("##TARGET##", target);

                callAlarm(titleMail, bodyMail, titleMessenger, bodyMessenger);
            }

            // 세금계산서 체크
            if (!isBillMatch) {
                String target = "세금계산서";
                String titleMail = TITLE_MAIL.replace("##TARGET##", target);
                String bodyMail = BODY_MAIL
                        .replace("##DATETIME##", now)
                        .replace("##TARGET##", "세금계산서");

                String titleMessenger = TITLE_MESSENGER.replace("##TARGET##", target);
                String bodyMessenger = BODY_MESSENGER.replace("##TARGET##", target);

                callAlarm(titleMail, bodyMail, titleMessenger, bodyMessenger);
            }
        }
    }

    private void sendMessenger(MemberInfo sender, List<MemberInfo> receiverList, String title, String body) throws IOException {
        String baseUrl = messengerDomain + "/servlet/AnnounceService";

        String recipient = "";
        for (MemberInfo memberInfo : receiverList) {
            recipient += "," + memberInfo.getMemSno();
        }
        if (!"".equals(recipient)) {
            recipient = recipient.substring(1);
        }

        System.out.println("fromId#="+sender.getMemSno());
        System.out.println("toId="+recipient);

        Map<String, String> params = new HashMap<>();
        params.put("SRV_CODE", messengerSrvCode);
        params.put("RECIPIENT", recipient);
        params.put("SEND", sender.getMemSno());
        params.put("SENDER_ALIAS", sender.getMemName());
        params.put("TITLE", title);
        params.put("BODY", body);

        // make Post Request
        StringJoiner paramJoiner = new StringJoiner("&");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isEmpty()) {
                paramJoiner.add(key + "=" + value);
            }
        }

        String query = paramJoiner.toString();
        String fullUrl = baseUrl + (query.isEmpty() ? "" : "?" + query);
        System.out.println("fullUrl :" + fullUrl);

        // send Post Request
        URL url = new URL(baseUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(query.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = reader.readLine())!= null) {
            response.append(inputLine);
        }
        reader.close();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("POST 요청 성공!");
        } else {
            System.out.println("POST 요청 실패!");
        }
        System.out.println("응답값 : " + response.toString());
    }

    private void callAlarm(String titleMail, String bodyMail, String titleMessenger, String bodyMessenger) {
        if (mailCheck || messengerCheck) {
            MemberInfo sender = adminMemberRepository.getSender();
            List<MemberInfo> receiverList = adminMemberRepository.getReceiverList();

            if (mailCheck) {
                for (MemberInfo memberInfo : receiverList) {
                    try {
                        ibkMailSender.sendMail(sender.getMemSno(), memberInfo.getMemSno(), sender.getMemName(), memberInfo.getMemName(), titleMail, bodyMail);
                    } catch (Exception e) {
                        log.debug("mailCheck", e);
                    }
                }
            }
            if (messengerCheck) {
                try {
                    sendMessenger(sender, receiverList, titleMessenger, bodyMessenger);
                } catch (Exception e) {
                    log.debug("messengerCheck", e);
                }
            }
        }
    }
}
