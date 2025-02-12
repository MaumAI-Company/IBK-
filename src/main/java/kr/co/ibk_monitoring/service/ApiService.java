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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final IbkMailSender ibkMailSender;
    private final AdminMemberRepository adminMemberRepository;
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

    @Value("${Globals.messenger.domain}")
    private String messengerDomain;
    @Value("${Globals.messenger.srvCode}")
    private String messengerSrvCode;

    public void serverCheck() {

        MemberInfo sender = adminMemberRepository.getSender();
        List<MemberInfo> receiverList = adminMemberRepository.getReceiverList();

        String title = TITLE;
        try {
            if (mccCheck) {
                Integer resMCC = mccServerCheck();
                if (resMCC == null || resMCC != 200) {
                    log.debug("### MCC 서버 장애! : " + resMCC);
                    title = "MCC " + TITLE;
                }
            }
            if (webCheck) {
                Integer resWeb = webServerCheck();
                if (resWeb == null || resWeb != 200) {
                    log.debug("### WEB 서버 장애! : " + resWeb);
                    title = "WEB " + TITLE;
                    callAlarm(sender, receiverList, title, title);
                }
            }
        } catch (Exception e) {
            callAlarm(sender, receiverList, title, title);
        }
    }

    public Integer mccServerCheck() {
        try {
            return sendPost(mccDomain, "/check-engine/");
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

    private void sendMessenger(MemberInfo sender, List<MemberInfo> receiverList, String title, String body) throws IOException {
        String url = messengerDomain + "/servlet/AnnounceService";

        String recipient = "";
        for (MemberInfo memberInfo : receiverList) {
            recipient += "," + memberInfo.getMemSno();
        }
        if (!"".equals(recipient)) {
            recipient = recipient.substring(1);
        }

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        // 파라미터 만들기
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("SRV_CODE", messengerSrvCode));
        param.add(new BasicNameValuePair("RECIPIENT", recipient));
        param.add(new BasicNameValuePair("SEND", sender.getMemSno()));
        param.add(new BasicNameValuePair("SENDER_ALIAS", sender.getMemName()));
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

    private void callAlarm(MemberInfo sender, List<MemberInfo> receiverList, String title, String message) {
        if (mailCheck) {
//            System.out.println("메일 발송");
            for (MemberInfo memberInfo : receiverList) {
                try {
                    ibkMailSender.sendMail(sender.getMemSno(), memberInfo.getMemSno(), sender.getMemName(), memberInfo.getMemName(), title, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (messengerCheck) {
//            System.out.println("메신저 발송");
            try {
                sendMessenger(sender, receiverList, title, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
