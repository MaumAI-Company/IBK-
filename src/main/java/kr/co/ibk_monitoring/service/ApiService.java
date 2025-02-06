package kr.co.ibk_monitoring.service;

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
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final IbkMailSender ibkMailSender;
    private final String TITLE = "서버에서 장애가 발생했습니다.";

    @Value("${Globals.domain.mcc}")
    private String mccDomain;

    @Value("${Globals.domain.web}")
    private String webDomain;

    @Value("${Globals.check.mcc}")
    private Boolean mccCheck;

    @Value("${Globals.check.web}")
    private Boolean webCheck;

    @Value("${Globals.check.mail}")
    private Boolean mailCheck;

    @Value("${Globals.check.messenger}")
    private Boolean messengerCheck;

    @Value("${Globals.mail.senderId}")
    private String mailSenderId;
    @Value("${Globals.mail.receiverId}")
    private String mailReceiverId;

    @Value("${Globals.messenger.domain}")
    private String messengerDomain;
    @Value("${Globals.messenger.srvCode}")
    private String messengerSrvCode;
    @Value("${Globals.messenger.senderId}")
    private String messengerSenderId;
    @Value("${Globals.messenger.receiverId}")
    private String messengerReceiverId;

    private Map<String, String> userMap = new HashMap<>();

    public void serverCheck() {
//        A99442-박성진, A88545-임유경, A00661-이동은, 025629-심중재, 043885-강승모
        userMap.put("A99442", "박성진");
        userMap.put("A88545", "임유경");
        userMap.put("A00661", "이동은");
        userMap.put("025629", "심중재");
        userMap.put("043885", "강승모");
        userMap.put("042353", "강다영");

        String title = TITLE;
        try {
            if (mccCheck) {
                Integer resMCC = sendPost(mccDomain, "/check-engine/");
                if (resMCC == null || resMCC != 200) {
                    log.debug("### MCC 서버 장애! : " + resMCC);
                    title = "MCC " + TITLE;
                    callAlarm(title, title);
                }
            }
            if (webCheck) {
                Integer resWeb = sendPost(webDomain, "/api/v1/o/check-engine");
                if (resWeb == null || resWeb != 200) {
                    log.debug("### WEB 서버 장애! : "+resWeb);
                    title = "WEB " + TITLE;
                    callAlarm(title, title);
                }
            }
        } catch (Exception e) {
            callAlarm(title, title);
        }
    }

    private Integer sendPost(String domain, String path) {
//        BufferedReader in = null;
        try {
            URL url = new URL(domain + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            //            System.out.println("Response Code : " + responseCode);
            /*in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }*/
            return connection.getResponseCode();
        } catch (IOException e) {
            return null;
        }/* finally {
            try {
                if (in != null) in.close();
            } catch (IOException e2) {
                // do nothing
            }
        }*/
    }

    private void sendMessenger(String title, String body) throws IOException {
        String url = messengerDomain + "/servlet/AnnounceService";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        // 파라미터 만들기
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("SRV_CODE", messengerSrvCode));
        param.add(new BasicNameValuePair("RECIPIENT", messengerReceiverId));
        param.add(new BasicNameValuePair("SEND", messengerSenderId));
        param.add(new BasicNameValuePair("SENDER_ALIAS", userMap.get(messengerSenderId)));
        param.add(new BasicNameValuePair("TITLE", title));
        param.add(new BasicNameValuePair("BODY", body));
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
        if (mailCheck) {
//            System.out.println("메일 발송");
            if (!ObjectUtils.isEmpty(mailReceiverId) && !ObjectUtils.isEmpty(userMap.get(mailReceiverId))) {
                String[] idArr = mailReceiverId.split(",");
                for (int i = 0; i < idArr.length; i++) {
                    try {
                        ibkMailSender.sendMail(mailSenderId, idArr[i], userMap.get(mailSenderId), userMap.get(idArr[i]), title, message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (messengerCheck) {
//            System.out.println("메신저 발송");
            try {
                sendMessenger(title, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
