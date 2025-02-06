package kr.co.ibk_monitoring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class IbkMailSender {

    @Value("${Globals.mail.host}")
    private String HOST;
    @Value("${Globals.mail.port}")
    private String PORT;
    @Value("${Globals.mail.hostname}")
    private String HOSTNAME;

    /**
     * @Method Name : sendMail
     * @Method 설명 : smtp 방식 send mail (gwmail주소 형식 : <사번XXXXX@gwmail.ibk.co.kr>)
     *         메일서버에 등록된 서버에서만 발송 할 수 있음
     * @작성일 : 2012. 11. 7.
     * @param fromId   : 발신자 사번
     * @param toId     : 수신자 사번
     * @param fromName : 발신자명
     * @param toName   : 수신자명
     * @param subject  : 제목
     * @param body     : 메일 내용
     */

    public Boolean sendMail(String fromId, String toId, String fromName, String toName, String subject, String body) throws Exception {

        int tmpId = 0;

        if (Pattern.matches("^[0-9]*$", fromId) == true && !"".equals(fromId)) {
            tmpId 		= Integer.parseInt(fromId);
            fromId 		= Integer.toString(tmpId);
        }

        System.out.println("fromId#="+fromId);
        System.out.println("toId="+toId);

        if (Pattern.matches("^[0-9]*$", toId) == true && !"".equals(toId)) {
            tmpId 		= Integer.parseInt(toId);
            toId 		= Integer.toString(tmpId);

            if (tmpId >= 50000) {
                System.out.println("@@50000 이상 사번은 메일을 발송 하지 않습니다.@@");
                return true;
            }
        }

        String from 			= "<" + fromId + "@" + HOSTNAME + ">";
        String toAddr 			= "<" + toId + "@" + HOSTNAME + ">";

        String aliasFromName 	= fromName + "/" + fromId + "/IBK";
        String aliasToName 		= toName + "/" + toId + "/IBK";

        String aliasFrom 		= aliasFromName + from;
        String aliasToAddr 		= aliasToName + toAddr;

        Socket smtpSocket 		= null;
        DataOutputStream os 	= null;
        DataInputStream is 		= null;

        Date dDate 				= new Date();
        DateFormat dFormat 		= DateFormat.getDateInstance(DateFormat.FULL, Locale.KOREAN);
        Boolean retVal = false;

        try {
            // Open port to server
            smtpSocket 			= new Socket(HOST, Integer.parseInt(PORT));
            os 					= new DataOutputStream(smtpSocket.getOutputStream());
            is 					= new DataInputStream(smtpSocket.getInputStream());

            if (smtpSocket != null && os != null && is != null) {
                try {
                    os.writeBytes("HELO\r\n");
                    // You will add the email address that the server
                    // you are using know you as.
                    os.writeBytes("MAIL From: " + from + "\r\n");

                    // Who the email is going to.
                    os.writeBytes("RCPT To: " + toAddr + "\r\n");
                    // IF you want to send a CC then you will have to add this
                    // os.writeBytes("RCPT Cc: <theCC@anycompany.com>\r\n");

                    // Now we are ready to add the message and the
                    // header of the email to be sent out.
                    os.writeBytes("DATA\r\n");

                    os.writeBytes("X-Mailer: Via Java\r\n");
                    os.writeBytes("DATE: " + dFormat.format(dDate) + "\r\n");
                    os.writeBytes("From: " + (new String(aliasFrom.getBytes("KSC5601"), "8859_1")) + "\r\n");
                    os.writeBytes("To: " + (new String(aliasToAddr.getBytes("KSC5601"), "8859_1")) + "\r\n");

                    // Again if you want to send a CC then add this.
                    // os.writeBytes("Cc: CCDUDE <CCPerson@theircompany.com>\r\n");

                    // Here you can now add a BCC to the message as well
                    // os.writeBytes("RCPT Bcc: BCCDude<BCC@invisiblecompany.com>\r\n");
//					print("@@제목:@@"+subject);
//					print("@@내용:@@"+body);

                    os.writeBytes("Subject: " + (new String(subject.getBytes("KSC5601"), "8859_1")) + "\r\n");
                    os.writeBytes((new String(body.getBytes("KSC5601"), "8859_1")) + "\r\n");

                    // Quit
                    os.writeBytes("\r\n.\r\n");
                    os.writeBytes("QUIT\r\n");

                    // Now send the email off and check the server reply.
                    // Was an OK is reached you are complete.
                    String responseline;
                    while ((responseline = is.readLine()) != null) {
//					BufferedReader br = new BufferedReader(new InputStreamReader(is));
//					while ((responseline = br.readLine()) != null) {
                        // print(responseline);
                        if (responseline.indexOf("Ok") != -1) {
                            retVal = false;
                            break;
                        }
                    }
                    retVal = true;

                    System.out.println("@@메일 발송@@");
                } catch (Exception e) {
                    System.out.println("Cannot send email as an error occurred.");
                    throw e;
                }
            }
        } catch (Exception e) {
            System.out.println("Host " + HOST + "unknown");
            throw e;
        } finally {
            if (os != null) try {os.close();} catch(Exception e){};
            if (is != null) try {is.close();} catch(Exception e){};
            if (smtpSocket != null) try {smtpSocket.close();} catch(Exception e){};

        }
        return retVal;
    }
}
