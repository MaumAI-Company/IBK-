package kr.co.ibk_monitoring.service;

import kr.co.ibk_monitoring.domain.web.MemberInfo;
import kr.co.ibk_monitoring.repository.AdminMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final IbkMailSender ibkMailSender;
    private final AdminMemberRepository adminMemberRepository;
    private final String TITLE = "[자동지급결의AI시스템] ##SERVERNAME## 서버에서 장애가 발생했습니다.";
    private final String BODY = "[IBK 예산관리시스템 자동지급결의 AI 시스템 장애 감지 알림]<br/>" +
            "1. 발생시간 : ##DATETIME##<br/>" +
            "2. 등급 : ##GRADE##<br/>" +
            "3. 호스트명 : ##HOSTNAME##<br/>" +
            "4. 메시지그룹 : ##MESSAGEGROUP##<br/>" +
            "5. 이벤트 내용 : ##EVENT##";
    private final String BODY_MESSENGER = "장애가 발생했습니다.";

    @Value("${Globals.domain.mcc1}")
    private String mccDomain1;
    @Value("${Globals.domain.mcc2}")
    private String mccDomain2;

    @Value("${Globals.domain.web}")
    private String webDomain;

    @Value("${Globals.check.mcc1}")
    private Boolean mccCheck1;
    @Value("${Globals.check.mcc2}")
    private Boolean mccCheck2;

    @Value("${Globals.check.web}")
    private Boolean webCheck;

    @Value("${Globals.check.mail}")
    private Boolean mailCheck;

    @Value("${Globals.check.messenger}")
    private Boolean messengerCheck;

    @Value("${Globals.messenger.domain}")
    private String messengerDomain;
    @Value("${Globals.messenger.srvCode}")
    private String messengerSrvCode;

    public void serverCheck() {
        String title = TITLE;
        String body = BODY;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");

        if (mccCheck1) {
            title = title.replaceAll("##SERVERNAME##", "MCC(BC카드)");
            body = body.replaceAll("##GRADE##", "CRITICAL");
            body = body.replaceAll("##HOSTNAME##", "pmvscbl3");
            body = body.replaceAll("##MESSAGEGROUP##", "AI");
            try {
                Integer resMCC = mccServerCheck1();
                if (resMCC == null || resMCC != 200) {
                    log.debug("### MCC 서버 장애!(BC카드) : " + resMCC);
                    String now = LocalDateTime.now().format(formatter);
                    body = body.replaceAll("##DATETIME##", now);
                    body = body.replaceAll("##EVENT##", "학습 API나 AI 엔진에서 장애시 학습서버 컨테이너를 확인해주세요.");
                    callAlarm(title, body);
                }
            } catch (Exception e) {
                log.debug("mccCheck1", e);
                String now = LocalDateTime.now().format(formatter);
                body = body.replaceAll("##DATETIME##", now);
                body = body.replaceAll("##EVENT##", e.getMessage());
                callAlarm(title, body);
            }
        }
        if (mccCheck2) {
            try {
                title = title.replaceAll("##SERVERNAME##", "MCC(세금계산서)");
                body = body.replaceAll("##GRADE##", "CRITICAL");
                body = body.replaceAll("##HOSTNAME##", "pmvscbl3");
                body = body.replaceAll("##MESSAGEGROUP##", "AI");
                Integer resMCC = mccServerCheck2();
                if (resMCC == null || resMCC != 200) {
                    log.debug("### MCC 서버 장애!(세금계산서) : " + resMCC);
                    String now = LocalDateTime.now().format(formatter);
                    body = body.replaceAll("##DATETIME##", now);
                    body = body.replaceAll("##EVENT##", "학습 API나 AI 엔진에서 장애시 학습서버 컨테이너를 확인해주세요.");
                    callAlarm(title, body);
                }
            } catch (Exception e) {
                log.debug("mccCheck2", e);
                String now = LocalDateTime.now().format(formatter);
                body = body.replaceAll("##DATETIME##", now);
                body = body.replaceAll("##EVENT##", e.getMessage());
                callAlarm(title, body);
            }
        }
        if (webCheck) {
            try {
                title = title.replaceAll("##SERVERNAME##", "WEB");
                body = body.replaceAll("##GRADE##", "NORMAL");
                body = body.replaceAll("##HOSTNAME##", "pmvscbl2");
                body = body.replaceAll("##MESSAGEGROUP##", "WAS");
                Integer resWeb = webServerCheck();
                if (resWeb == null || resWeb != 200) {
                    log.debug("### WEB 서버 장애! : " + resWeb);
                    String now = LocalDateTime.now().format(formatter);
                    body = body.replaceAll("##DATETIME##", now);
                    body = body.replaceAll("##EVENT##", "web 서버 컨테이너를 확인해주세요.");
                    callAlarm(title, body);
                }
            } catch (Exception e) {
                log.debug("webCheck", e);
                String now = LocalDateTime.now().format(formatter);
                body = body.replaceAll("##DATETIME##", now);
                body = body.replaceAll("##EVENT##", e.getMessage());
                callAlarm(title, body);
            }
        }
    }

    public Integer mccServerCheck1() {
        try {
            return sendPost(mccDomain1, "/check-engine/");
        } catch (Exception e) {
            throw e;
        }
    }

    public Integer mccServerCheck2() {
        try {
            return sendPost(mccDomain2, "/check-engine/");
        } catch (Exception e) {
            throw e;
        }
    }

    public Integer webServerCheck() {
        try {
            return sendPost(webDomain, "/api/v1/o/check-engine");
        } catch (Exception e) {
            throw e;
        }
    }

    private Integer sendPost(String domain, String path) {
//        BufferedReader in = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(domain + path);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(10000);

            //            System.out.println("Response Code : " + responseCode);
            /*in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }*/
            return connection.getResponseCode();
        } catch (IOException e) {
            log.debug("sendPost", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            /*try {
                if (in != null) in.close();
            } catch (IOException e2) {
                // do nothing
            }*/
        }
    }

    private void sendMessenger(MemberInfo sender, List<MemberInfo> receiverList, String title, String body) throws IOException {
        String url = messengerDomain + "/servlet/AnnounceService";

        String recipient = "";
        for (MemberInfo memberInfo : receiverList) {
            recipient += "," + memberInfo.getMemSno();
        }
        if (!"".equals(recipient)) {
            recipient = recipient.substring(1);
        }

        System.out.println("fromId#="+sender.getMemSno());
        System.out.println("toId="+recipient);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        // 파라미터 만들기
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("SRV_CODE", messengerSrvCode));
        param.add(new BasicNameValuePair("RECIPIENT", recipient));
        param.add(new BasicNameValuePair("SEND", sender.getMemSno()));
        param.add(new BasicNameValuePair("SENDER_ALIAS", sender.getMemName()));
        param.add(new BasicNameValuePair("TITLE", URLEncoder.encode(title, StandardCharsets.UTF_8)));
        param.add(new BasicNameValuePair("BODY", URLEncoder.encode(body, StandardCharsets.UTF_8)));
        // 파라미터적용
        httpPost.setEntity(new UrlEncodedFormEntity(param));

        // 응답처리
        HttpResponse httpResponse = httpClient.execute(httpPost);
        // 스테이터스 가져오기
        System.out.println("status" + httpResponse.getStatusLine());
        // 헤더읽어보기
        Header[] hp = httpResponse.getAllHeaders();
        for (Header headers : hp) {
            System.out.println("header" + headers);
        }
        // 쿠키읽어보기
        /*List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("비어있음");
        } else {
            for (Cookie cookieItem : cookies) {
                System.out.println("쿠키이름\t : " + cookieItem.getName());
                System.out.println("쿠키값\t : " + cookieItem.getValue());
                System.out.println("쿠키도메인 : " + cookieItem.getDomain());
            }
        }*/
        // 결과가져오기
        String contents = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("------------");
        System.out.println("contents" + contents);
    }

    private void callAlarm(String title, String message) {
        if (mailCheck || messengerCheck) {
            MemberInfo sender = adminMemberRepository.getSender();
            List<MemberInfo> receiverList = adminMemberRepository.getReceiverList();

            if (mailCheck) {
//            System.out.println("메일 발송");
                for (MemberInfo memberInfo : receiverList) {
                    try {
                        ibkMailSender.sendMail(sender.getMemSno(), memberInfo.getMemSno(), sender.getMemName(), memberInfo.getMemName(), title, message);
                    } catch (Exception e) {
                        log.debug("mailCheck", e);
                    }
                }
            }
            if (messengerCheck) {
//            System.out.println("메신저 발송");
                try {
                    sendMessenger(sender, receiverList, title, BODY_MESSENGER);
                } catch (Exception e) {
                    log.debug("messengerCheck", e);
                }
            }
        }
    }
}
