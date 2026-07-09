/*
package kr.co.ibk_monitoring.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;

import javax.inject.Inject;

import org.anyframe.nexa.query.service.impl.XPDao;
import org.anyframe.nexa.query.service.impl.XPServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ibk.bbs.bbs.biz.cm.co.svc.CMCO003Svc;
import com.ibk.bbs.bbs.comm.security.vo.MemberVo;
import com.ibk.bbs.bbs.comm.util.CommonUtils;
import com.initech.util.URLDecoder;
import com.nexacro17.xapi.data.DataSet;
import com.nexacro17.xapi.data.DataSetList;
import com.nexacro17.xapi.data.VariableList;
public class IbkMessageSender extends XPServiceImpl implements CMCO003Svc {
    private static final Logger LOGGER = LoggerFactory.getLogger(IbkMessageSender.class);
    private static final String NAMASPACE = "CM.CO.CMCO003.";

    private static final int DefaultTimeOut = 1000;		// IBK톡 알람 기본 타임아웃 설정
    private static int ReadTimeOut = 1000;				// IBK톡 알람 기본 타임아웃 설정 (공통코드 관리값)

    @Inject
    public IbkMessageSender(XPDao xpDao) {
        super.xpDao = xpDao;
    }

    */
/**
     *
     * 설  명 : 로그인 사용자 내역 조회
     * This Area Change Content Write
     * @param
     * @return
     * @throws Exception
     *//*

    public void selectLoginUserList(VariableList inVarList, DataSetList inDsList, VariableList outVarList, DataSetList outDsList) throws Exception {
        try
        {
            MemberVo memberVo = CommonUtils.getLoginInfo();

            VariableList paramList = new VariableList();

            paramList.clear();
            paramList.add("empNo", memberVo.getEmn());

            DataSet outDs = xpDao.getList(NAMASPACE + "selectLoginUserList", paramList);

            outDs.setName("dsLoginUserList");
            outDsList.add(outDs);

        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    */
/**
     *
     * 설  명 : 전송대상 제한 건수 조회
     * This Area Change Content Write
     * @param
     * @return
     * @throws Exception
     *//*

    public void selectCodeCntList(VariableList inVarList, DataSetList inDsList, VariableList outVarList, DataSetList outDsList) throws Exception {
        try
        {
            VariableList paramList = new VariableList();

            paramList.clear();
            paramList.add("cdId", "CO100001");	// 코드도메인

            DataSet outDs = xpDao.getList(NAMASPACE + "selectCodeCntList", paramList);

            outDs.setName("dsCodeCntList");
            outDsList.add(outDs);

        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    */
/**
     *
     * 설  명 : 직원내역조회
     * This Area Change Content Write
     * @param
     * @return
     * @throws Exception
     *//*

    public void selectEmpInfoList(VariableList inVarList, DataSetList inDsList, VariableList outVarList, DataSetList outDsList) throws Exception {
        try
        {
            */
/*****************************************************************************************
             * dsSearch 항목
             * ***************************************************************************************
             * brcd 		(부서코드)
             * emm  		(사번 또는 사원명)
             * ducd20 	(직책코드 - 부장) AS-IS 시스템 RSPT코드 동적쿼리 금지로 인한 대체 필드(NULL만 아니면 조건문탐)
             * ducd30 	(직책코드 - 팀장) AS-IS 시스템 RSPT코드 동적쿼리 금지로 인한 대체 필드(NULL만 아니면 조건문탐)
             * ducd40 	(직책코드 - 팀원) AS-IS 시스템 RSPT코드 동적쿼리 금지로 인한 대체 필드(NULL만 아니면 조건문탐)
             * ducd90 	(직책코드 - 기타) AS-IS 시스템 RSPT코드 동적쿼리 금지로 인한 대체 필드(NULL만 아니면 조건문탐)
             * brncDcd 	(본부/영업점 구분 - 1:본부, 2:영업점, 9:특점부점)
             * gnaf 		(업무 서무여부 체크용)
             ****************************************************************************************//*

            DataSet inDs = inDsList.get("dsSearch");
            DataSet outDs = xpDao.getList(NAMASPACE + "selectEmpInfoList", inDs);
            outDs.setName("dsUserList");
            outDsList.add(outDs);

        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    */
/**
     *
     * 설  명 : IBK알람 전송
     * This Area Change Content Write
     * @param
     * @return
     * @throws Exception
     *//*

    public void doIBKAlamCall(VariableList inVarList, DataSetList inDsList, VariableList outVarList, DataSetList outDsList) throws Exception {

        String userNm = "";
        String sendMsg = "";
        String url = CommonUtils.SERVER_URL + "nx/index.html";
        String userIp = "";
        MemberVo memberVo = CommonUtils.getLoginInfo();

        try
        {
            String menuId 		= inDsList.get("dsSendInfo").getString(0, "menuId");	// 링크보낼 화면ID
            sendMsg 		= inDsList.get("dsSendInfo").getString(0, "sendMsg");	// 전송메시지 내용(필수)
            DataSet mainList 	= inDsList.get("dsUserList");							// 전송대상 리스트

            if(menuId != null && !"".equals(menuId))
            {
                url += "?menuId=" + menuId;
            }

            if(mainList != null)
            {
                StringBuffer sbMessage = new StringBuffer();

                for(int i = 0; i < mainList.getRowCount(); i++)
                {
                    userIp = mainList.getString(i, "userIp");

                    if(userIp != null && !"".equals(userIp))
                    {
                        userNm = mainList.getString(i, "userNm");

                        sbMessage.setLength(0);
                        sbMessage.append(userNm);	// 수신자명
                        sbMessage.append("|" + sendMsg);					// 메시지내용
                        sbMessage.append("|" + memberVo.getEmm());			// 발송자명
                        sbMessage.append("|예산관리시스템");					// 시스템명칭
                        sbMessage.append("|" + url);						// 시스템 링크
                        sendSoket(userIp, URLEncoder.encode(sbMessage.toString(), "UTF-8"));
                    }
                }
            }

        } catch(Exception e) {
            String errMsg = "=====IBK알람전송오류발생=====";
            errMsg += "\n=====파라미터=====";
            errMsg += "\n=====userNm(수신자명): "+userNm;
            errMsg += "\n=====sendMsg(메시지내용)"+sendMsg;
            errMsg += "\n=====sendEmm(발송자명): "+memberVo.getEmm();
            errMsg += "\n=====url(시스템링크): "+url;
            errMsg += "\n=====userIp(수신Ip): "+userIp;
            errMsg += "\n=====오류내용=====\n";
            errMsg += e.getMessage();
            LOGGER.error(errMsg);
        }
        return;
    }

    */
/**
     *
     * 설  명 : 알람전송
     * This Area Change Content Write
     * @param
     * @return
     * @throws Exception
     *//*

    public synchronized void sendSoket(String sendIp, String sendMsg) throws Exception {

        DatagramSocket sock 		= null;
        InetAddress serverInet 		= null;
        DatagramPacket out 			= null;
        DatagramPacket input 		= null;

        int port = 3601;
        String word = null;

        // 일정기간 동안 UC알람이 전송되지 않도록 처리
        if(Arrays.asList("LOCAL", "DEV").contains(CommonUtils.SERVER_TYPE)) {
            return;
        }

        // 받는이 성명 | 문서 제목 | 문서 발송자 | 시스템명칭 | 해당 시스템의 링크
        byte[] send_buf = null;

        try
        {
            String hostName = InetAddress.getLocalHost().getHostName();

            sock 			= new DatagramSocket();
            serverInet 		= InetAddress.getByName(sendIp);
            send_buf 		= sendMsg.getBytes();
            input 			= new DatagramPacket(new byte[1024], 1024);

            // DNS 스푸핑 대응
            if("LOCAL".equals(CommonUtils.SERVER_TYPE)) {
                if(!hostName.startsWith("PW")) {
                    throw new Exception("잘못된 접근입니다.");
                }
            }
            else if("DEV".equals(CommonUtils.SERVER_TYPE)) {
                if(!"dbbsapl1".equals(hostName)) {
                    throw new Exception("잘못된 접근입니다.");
                }
            }
            else if("PROD".equals(CommonUtils.SERVER_TYPE)) {
                if(!"pbbsapl1".equals(hostName)) {
                    throw new Exception("잘못된 접근입니다.");
                }
            }

            out 			= new DatagramPacket(send_buf, send_buf.length, serverInet, port);
            sock.send(out);

//		    while(true)
//		    {
//		    	sock.receive(input);
//		    	word = new String(input.getData(), 0, input.getLength());
//		    	if(word == null) break;
//		    }
        } catch(Exception e) {
            String errMsg = "=====IBK알람전송오류발생(sendSoket)=====";
            errMsg += "\n=====파라미터=====";
            errMsg += "\n=====sendIp(수신Ip): "+sendIp;
            errMsg += "\n=====sendMsg(메시지내용)"+sendMsg;
            errMsg += "\n=====serverInet(serverInet): "+serverInet;
            errMsg += "\n=====port(port): "+port;
            errMsg += "\n=====오류내용=====\n";
            errMsg += e.getMessage();
            LOGGER.error(errMsg);
            throw e;
        } finally {
            if(sock != null) sock.close();
            LOGGER.info("closed Socket Client");
        }
    }

    */
/**
     *
     * 설  명 : UC알람 전송
     * This Area Change Content Write
     * @param
     * @return
     * @throws Exception
     *//*

    public void doUCCall(VariableList inVarList, DataSetList inDsList, VariableList outVarList, DataSetList outDsList) throws Exception {

        try {

            */
/******************************************************************************************
             * 알람 전송시 필요한 정보
             * Dataset
             * dsSendInfo(sendMsg : 전송할 내용, sendType : 전송구분, menuId : 오픈할 메뉴ID (없을시 공백) - 1Row
             * dsUserList(userId : 전송대상 ID) - nRow
             ******************************************************************************************//*

            MemberVo memberVo = CommonUtils.getLoginInfo();

            VariableList paramList = new VariableList();

            boolean sendToNewSvr = true;	// 신서버 전송여부 (기본값 신서버 전송)

            */
/*******************************************************************
             * 발신여부 확인
             ******************************************************************//*

            // 메시지 수신여부 조회
            DataSet outDs = xpDao.getList(NAMASPACE + "selectSendInfo", paramList);

            if (outDs == null || outDs.getRowCount() < 1) {
                LOGGER.error("PSJPSJ 전송정보조회불가!!!");
                return;
            }
            else {

                // 전송여부조회
                if ("N".equals(outDs.getString(0, "cdItncNm"))) {
                    LOGGER.error("PSJPSJ 전송제외상태!!!");
                    return;
                }

                // OLD / NEW 조회
                if ("OLD".equals(outDs.getString(1, "cdItncNm"))) {
                    sendToNewSvr = false;
                }

                // READ TIMEOUT 시간 설정
                try {

                    // 타임아웃 시간이 다르다면 다시 설정
                    if (outDs.getInt(2, "cdItncNm") != ReadTimeOut) {
                        ReadTimeOut = outDs.getInt(2, "cdItncNm");
                    }

                } catch (Throwable t) {

                }
            }

            */
/*******************************************************************
             * 발신여부 Y 진행
             ******************************************************************//*

            LOGGER.info("PSJPSJ 전송시작");

            String sendEmn 		= memberVo.getEmn();	// 직원번호
            String sendEmm 		= memberVo.getEmm();	// 직원명
            String sendAlias 	= "";
            String menuId 		= inDsList.get("dsSendInfo").getString(0, "menuId");					// 링크보낼 화면ID
            String sendMsg 		= inDsList.get("dsSendInfo").getString(0, "sendMsg");					// 전송메시지 내용(필수)
            String sendType 	= String.valueOf(inDsList.get("dsSendInfo").getObject(0, "sendType"));	// 전송구분(ALAM : UC알람 ELSE 쪽지) (필수)

            */
/*******************************************************************
             * 발신자 셋팅
             ******************************************************************//*

            if(sendEmn.substring(0,1).equals("A"))
            {
                sendEmn 	= "a" + sendEmn.substring(1);
                sendAlias 	= sendEmm;
            }
            else if(sendEmn.trim().length() == 6)
            {
                sendEmn 	= sendEmn.substring(1); 			// 5자리수 직원번호
                sendAlias 	= sendEmm;

                if("0".equals(sendEmn.substring(0,1)))
                { 	// 4자리수 직원번호
                    sendEmn 	= sendEmn.substring(1); 		// 한번더 맨앞에 '0'제거
                    sendAlias 	= sendEmm;
                }
            }

            LOGGER.debug("sendEmn : " 	+ sendEmn	);
            LOGGER.debug("sendAlias : " + sendAlias	);

            */
/*******************************************************************
             * 수신자 셋팅
             ******************************************************************//*

            DataSet mainList = inDsList.get("dsUserList");		// 전송대상 리스트

            if(mainList != null)
            {
                for(int i = 0; i < mainList.getRowCount(); i++)
                {
                    String sendYn = "Y";
                    String userId = mainList.getString(i, "userId");

                    LOGGER.debug("userId : " + userId);

                    paramList.clear();
                    paramList.add("cdId", 	"BM100141"	);	// 코드도메인
                    paramList.add("cdItncVl", 	userId		);	// 알람 수신거부 직원번호

                    // 메시지 수신여부 조회
                    if(xpDao.getList(NAMASPACE + "selectChkMsgRcv", paramList).getRowCount() > 0)
                    {
                        sendYn = "N";
                    }

                    if(userId.substring(0,1).equals("A"))
                    {
                        userId = "a" + userId.substring(1);
                    }
                    else if(userId.trim().length() == 6)
                    {
                        userId = userId.substring(1); 			// 5자리수 직원번호

                        if(userId.substring(0,1).equals("0"))
                        { 	// 4자리수 직원번호
                            userId = userId.substring(1); 		// 한번더 맨앞에 '0'제거
                        }
                    }
                    if("ALAM".equals(sendType)) userId = "E:" + userId;

                    */
/*******************************************************************
                     * 전송 기본사항 셋팅
                     ******************************************************************//*

                    String LINK_URL = CommonUtils.SERVER_URL + "nx/index.html";

                    paramList.clear();
                    paramList.add("SRV_CODE"		, "BBSBBS"	);	// 발송서버 코드 (시스템코드임)
                    paramList.add("RECIPIENT"		, userId		);	// 수신자
                    paramList.add("SEND"			, sendEmn		);	// 발신자 사번
                    paramList.add("SENDER_ALIAS"	, sendAlias		); 	// 발신자명
                    paramList.add("TITLE"			, "[예산관리시스템]"); 	// 제목(사이즈제한 32Byte 이내)
                    paramList.add("BODY"			, sendMsg		); 	// 본문(사이즈제한 100Byte 이내)
                    paramList.add("LINK_TXT"		, "예산관리시스템"	);
                    paramList.add("SEND_TYPE"		, sendType		);	// 전송구분(ALAM : UC알람 ELSE 쪽지) (필수)

                    if(menuId != null && !"".equals(menuId))
                    {
                        LINK_URL += "?menuId=" + menuId;
                    }
                    paramList.add("LINK_URL", LINK_URL);

                    // 운영서버만 알람전송
                    if("PROD".equals(CommonUtils.SERVER_TYPE))
                    {
                        // 알람을 동의한 사원일경우 또는 전송타입이 쪽지일 경우
                        if("Y".equals(sendYn) || !"ALAM".equals(sendType))
                        {
                            // 신서버
                            if (sendToNewSvr) {
                                send(paramList);
                            }
                            // 구서버
                            else {
                                LOGGER.info("PSJPSJ 구서버 전송!!!");
                                paramList.add("SRV_CODE", "B01BBSC001"	);
                                send_Old(paramList);
                            }
                        }
                        else
                        {
                            LOGGER.info("알람 수신을 원하지 않는 직원입니다. : " 	+ userId);
                        }
                    }
                    else
                    {

                        // 테스트 할때 아래 주석 푸세요
                        if("Y".equals(sendYn) || !"ALAM".equals(sendType))
                        {
                            LOGGER.info("[개발]알람전송 테스트중 ");
                            // 신서버
                            if (sendToNewSvr) {
                                send(paramList);
                            }
                            // 구서버
                            else {
                                LOGGER.info("PSJPSJ 구서버 전송!!!");
                                paramList.add("SRV_CODE", "B01BBSC001"	);
                                send_Old(paramList);
                            }
                        }
                        else
                        {
                            LOGGER.info("[개발]알람 수신을 원하지 않는 직원입니다. : " 	+ userId);
                        }

                    }
                }
            }
        }
        catch(Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    */
/**
     *
     * 설  명 : UC 알람전송
     * This Area Change Content Write
     * @param
     * @return
     * @throws Exception
     *//*

    public void send(VariableList sendList) throws Exception {
        URL url;
        BufferedReader br 	= null;
        boolean retVal 		= false;
        String sendMsg 		= "";
        int timeOut		= ReadTimeOut;

        // 일정기간 동안 UC알람이 전송되지 않도록 처리
        // 로컬 개발은 return처리됨
        // if(Arrays.asList("LOCAL", "DEV").contains(CommonUtils.SERVER_TYPE)) {
        //
        //	LOGGER.info("DEV :" + CommonUtils.SERVER_TYPE);
        //	return;
        // }

        String SEND_TYPE = sendList.getString("SEND_TYPE");

        try
        {
            String SRV_CODE 	= URLEncoder.encode(sendList.getString("SRV_CODE")		, "UTF-8");		// 발송서버 코드 (시스템코드임)
            String RECIPIENT 	= URLEncoder.encode(sendList.getString("RECIPIENT")		, "UTF-8");		// 수신자
            String SEND 		= URLEncoder.encode(sendList.getString("SEND")			, "UTF-8");		// 발신자 사번
            String SENDER_ALIAS = URLEncoder.encode(sendList.getString("SENDER_ALIAS")	, "UTF-8");		// 발신자명
            String TITLE 		= URLEncoder.encode(sendList.getString("TITLE")			, "UTF-8");		// 제목(사이즈제한 32Byte 이내)
            String BODY 		= URLEncoder.encode(sendList.getString("BODY")			, "UTF-8");		// 본문(사이즈제한 100Byte 이내)
            String LINK_TXT 	= URLEncoder.encode(sendList.getString("LINK_TXT")		, "UTF-8");
            String LINK_URL 	= URLEncoder.encode(sendList.getString("LINK_URL")		, "UTF-8");

            // 전송구분(ALAM : UC알람 ELSE 쪽지) (필수)
            if("ALAM".equals(SEND_TYPE))
            {
                sendMsg  = "SRV_CODE=" + SRV_CODE;
                sendMsg += "&RECIPIENT=" + RECIPIENT;
                sendMsg += "&SEND=" + SEND;
                sendMsg += "&SENDER_ALIAS=" + SENDER_ALIAS;
                sendMsg += "&TITLE=" + TITLE;
                sendMsg += "&BODY=" + BODY;
                sendMsg += "&LINKTXT=" + LINK_TXT;
                sendMsg += "&LINKURL=" + LINK_URL;

                //url = new URL(CommonUtils.MESSENGER_URL + "servlet/AnnounceService");
                //개발 및 로컬 테스트 할때 아래 주석 푸세요(운영주소임)
                //url = new URL("http://172.18.49.7/servlet/AnnounceService");

                //[개선]UC 메신저 시스템 API 대응개발 (예산관리시스템)[M20240716-A40]_20240812
                //운영_172.18.76.21:8010
                //개발_172.18.123.43:8010

                // 운영서버 일때
                if("PROD".equals(CommonUtils.SERVER_TYPE))
                {
                    url = new URL("http://172.18.76.21:8010/servlet/AnnounceService");
                    LOGGER.info("[운영]strUrl확인: " 	+ url);

                }else {   // 운영서버가 아닐때
                    url = new URL("http://172.18.123.43:8010/servlet/AnnounceService");
                    LOGGER.info("[개발]strUrl확인: " 	+ url);
                }

            }
            // 쪽지
            else
            {
                //String strUrl = CommonUtils.MESSENGER_URL + "msg/sendMessage.do?";
                // 개발 및 로컬 테스트 할때 아래 주석 푸세요(운영주소임)
                // 개행처리
                BODY = URLEncoder.encode(sendList.getString("BODY").replace(StringUtils.CR, "").replace(StringUtils.LF, StringUtils.CR+StringUtils.LF), "UTF-8");

                //String strUrl = "http://172.18.49.7/msg/sendMessage.do?";
                //strUrl += "userId=" + SEND;									// 발신자
                //strUrl += "&rcvId=" + RECIPIENT;							// 수신자
                //strUrl += "&msg=" 	+ BODY;									// 메시지내용
                //strUrl += "&msgType=1&Chnl_type_clcd=UCS"; 					// 운영

                //[개선]UC 메신저 시스템 API 대응개발 (예산관리시스템)[M20240716-A40]_20240812
                //운영_172.18.76.21:8010
                //개발_172.18.123.43:8010

                //strUrl 초기화
                String strUrl = "";

                // 운영서버 일때
                if("PROD".equals(CommonUtils.SERVER_TYPE))
                {
                    strUrl = "http://172.18.76.21:8010/msg/sendMessage.do?";
                    LOGGER.info("[운영]strUrl확인: " 	+ strUrl);

                }else {   // 운영서버가 아닐때
                    strUrl = "http://172.18.123.43:8010/msg/sendMessage.do?";
                    LOGGER.info("[개발]strUrl확인: " 	+ strUrl);

                }

                strUrl += "USER_ID=" + SEND;									// 발신자
                strUrl += "&RCV_ID=" + RECIPIENT;							// 수신자
                strUrl += "&MSG=" 	+ BODY;									// 메시지내용
                strUrl += "&MSG_TYPE=1&SRV_CODE=BBSBBS"; 					// 운영
                url = new URL(strUrl);
            }
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            // ConnectTimeout 로직추가
            timeOut = ReadTimeOut > DefaultTimeOut ? DefaultTimeOut : ReadTimeOut;

            LOGGER.debug("PSJPSJ Connect TIMEOUT : " + String.valueOf(DefaultTimeOut));
            LOGGER.debug("PSJPSJ Read TIMEOUT : " + String.valueOf(timeOut));

            conn.setConnectTimeout(DefaultTimeOut);
            conn.setReadTimeout(timeOut);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            try
            {
                dos.write(sendMsg.getBytes());
            }
            catch(IOException e) {
                String errMsg = "=====UC알람전송오류발생(write)=====";
                errMsg += "\n=====파라미터=====";
                errMsg += "\n=====SEND_TYPE(전송유형): "+SEND_TYPE;
                errMsg += "\n=====SRV_CODE(발송서버코드): "+sendList.getString("SRV_CODE");
                errMsg += "\n=====RECIPIENT(수신자): "+sendList.getString("RECIPIENT");
                errMsg += "\n=====SEND(발신자 사번): "+sendList.getString("SEND");
                errMsg += "\n=====SENDER_ALIAS(발신자명): "+sendList.getString("SENDER_ALIAS");
                errMsg += "\n=====TITLE(제목): "+sendList.getString("TITLE");
                errMsg += "\n=====BODY(본문): "+sendList.getString("BODY");
                errMsg += "\n=====LINK_TXT(링크문자): "+sendList.getString("LINK_TXT");
                errMsg += "\n=====LINK_URL(링크url): "+sendList.getString("LINK_URL");
                errMsg += "\n=====오류내용=====\n";
                errMsg += e.getMessage();
                LOGGER.error(errMsg);
            }
            catch(Exception e) {
                String errMsg = "=====UC알람전송오류발생(write)=====";
                errMsg += "\n=====파라미터=====";
                errMsg += "\n=====SEND_TYPE(전송유형): "+SEND_TYPE;
                errMsg += "\n=====SRV_CODE(발송서버코드): "+sendList.getString("SRV_CODE");
                errMsg += "\n=====RECIPIENT(수신자): "+sendList.getString("RECIPIENT");
                errMsg += "\n=====SEND(발신자 사번): "+sendList.getString("SEND");
                errMsg += "\n=====SENDER_ALIAS(발신자명): "+sendList.getString("SENDER_ALIAS");
                errMsg += "\n=====TITLE(제목): "+sendList.getString("TITLE");
                errMsg += "\n=====BODY(본문): "+sendList.getString("BODY");
                errMsg += "\n=====LINK_TXT(링크문자): "+sendList.getString("LINK_TXT");
                errMsg += "\n=====LINK_URL(링크url): "+sendList.getString("LINK_URL");
                errMsg += "\n=====오류내용=====\n";
                errMsg += e.getMessage();
                LOGGER.error(errMsg);
            }
            finally
            {
                if (dos != null) dos.close();
            }

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()), 1024);
            String recvStr = br.readLine();

            LOGGER.debug(URLDecoder.decode(recvStr, "UTF-8"));
            LOGGER.debug(URLDecoder.decode(recvStr, "EUC-KR"));

            if (recvStr.trim().contains("true") || "0".equals(recvStr.trim()))
            {
                retVal = true;
                LOGGER.debug("UC SUCCESS : [" + recvStr.trim() + "]");
            }
            else
            {
                LOGGER.debug("UC FAIL : [" + recvStr.trim() + "]");
            }
            br.close();

        }
        catch(Exception e)
        {
            String errMsg = "=====UC(send) ERROR=====";
            errMsg += "\n==========";
            errMsg += "\n=====SEND_TYPE(): "+SEND_TYPE;
            errMsg += "\n=====SRV_CODE(): "+sendList.getString("SRV_CODE");
            errMsg += "\n=====RECIPIENT(): "+sendList.getString("RECIPIENT");
            errMsg += "\n=====SEND(): "+sendList.getString("SEND");
            errMsg += "\n=====SENDER_ALIAS(): "+sendList.getString("SENDER_ALIAS");
            errMsg += "\n=====TITLE(): "+sendList.getString("TITLE");
            errMsg += "\n=====BODY(): "+sendList.getString("BODY");
            errMsg += "\n=====LINK_TXT(): "+sendList.getString("LINK_TXT");
            errMsg += "\n=====LINK_URL(): "+sendList.getString("LINK_URL");
            errMsg += "\n=====ERROR =====\n";
            errMsg += "ERROR INFO : " + e.getMessage();
            LOGGER.error(errMsg);
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                } catch (IOException e) {}
            }
        }
        return;
    }

    */
/**
     *
     * 설  명 : UC 알람전송
     * This Area Change Content Write
     * @param
     * @return
     * @throws Exception
     *//*

    public void send_Old(VariableList sendList) throws Exception {
        URL url;
        BufferedReader br 	= null;
        boolean retVal 		= false;
        String sendMsg 		= "";
        int timeOut		= ReadTimeOut;

        // 일정기간 동안 UC알람이 전송되지 않도록 처리

        if(Arrays.asList("LOCAL", "DEV").contains(CommonUtils.SERVER_TYPE)) {
            return;
        }

        String SEND_TYPE = sendList.getString("SEND_TYPE");

        try
        {
            String SRV_CODE 	= URLEncoder.encode(sendList.getString("SRV_CODE")		, "UTF-8");		// 발송서버 코드 (시스템코드임)
            String RECIPIENT 	= URLEncoder.encode(sendList.getString("RECIPIENT")		, "UTF-8");		// 수신자
            String SEND 		= URLEncoder.encode(sendList.getString("SEND")			, "UTF-8");		// 발신자 사번
            String SENDER_ALIAS = URLEncoder.encode(sendList.getString("SENDER_ALIAS")	, "UTF-8");		// 발신자명
            String TITLE 		= URLEncoder.encode(sendList.getString("TITLE")			, "UTF-8");		// 제목(사이즈제한 32Byte 이내)
            String BODY 		= URLEncoder.encode(sendList.getString("BODY")			, "UTF-8");		// 본문(사이즈제한 100Byte 이내)
            String LINK_TXT 	= URLEncoder.encode(sendList.getString("LINK_TXT")		, "UTF-8");
            String LINK_URL 	= URLEncoder.encode(sendList.getString("LINK_URL")		, "UTF-8");

            // 전송구분(ALAM : UC알람 ELSE 쪽지) (필수)
            if("ALAM".equals(SEND_TYPE))
            {
                sendMsg  = "SRV_CODE=" + SRV_CODE;
                sendMsg += "&RECIPIENT=" + RECIPIENT;
                sendMsg += "&SEND=" + SEND;
                sendMsg += "&SENDER_ALIAS=" + SENDER_ALIAS;
                sendMsg += "&TITLE=" + TITLE;
                sendMsg += "&BODY=" + BODY;
                sendMsg += "&LINKTXT=" + LINK_TXT;
                sendMsg += "&LINKURL=" + LINK_URL;

                //url = new URL(CommonUtils.MESSENGER_URL + "servlet/AnnounceService");
                // 개발 및 로컬 테스트 할때 아래 주석 푸세요(운영주소임)
                url = new URL("http://172.18.49.7/servlet/AnnounceService");
            }
            // 쪽지
            else
            {
                //String strUrl = CommonUtils.MESSENGER_URL + "msg/sendMessage.do?";
                // 개발 및 로컬 테스트 할때 아래 주석 푸세요(운영주소임)
                // 개행처리
                BODY = URLEncoder.encode(sendList.getString("BODY").replace(StringUtils.CR, "").replace(StringUtils.LF, StringUtils.CR+StringUtils.LF), "UTF-8");

                String strUrl = "http://172.18.49.7/msg/sendMessage.do?";
                strUrl += "userId=" + SEND;									// 발신자
                strUrl += "&rcvId=" + RECIPIENT;							// 수신자
                strUrl += "&msg=" 	+ BODY;									// 메시지내용
                strUrl += "&msgType=1&Chnl_type_clcd=UCS"; 					// 운영
                url = new URL(strUrl);
            }
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            // ConnectTimeout 로직추가
            timeOut = ReadTimeOut > DefaultTimeOut ? DefaultTimeOut : ReadTimeOut;
            LOGGER.debug("PSJPSJ Connect TIMEOUT : " + String.valueOf(DefaultTimeOut));
            LOGGER.debug("PSJPSJ Read TIMEOUT : " + String.valueOf(timeOut));

            conn.setConnectTimeout(DefaultTimeOut);
            conn.setReadTimeout(timeOut);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            try
            {
                dos.write(sendMsg.getBytes());
            }
            catch(IOException e) {
                String errMsg = "=====UC알람전송오류발생(write)=====";
                errMsg += "\n=====파라미터=====";
                errMsg += "\n=====SEND_TYPE(전송유형): "+SEND_TYPE;
                errMsg += "\n=====SRV_CODE(발송서버코드): "+sendList.getString("SRV_CODE");
                errMsg += "\n=====RECIPIENT(수신자): "+sendList.getString("RECIPIENT");
                errMsg += "\n=====SEND(발신자 사번): "+sendList.getString("SEND");
                errMsg += "\n=====SENDER_ALIAS(발신자명): "+sendList.getString("SENDER_ALIAS");
                errMsg += "\n=====TITLE(제목): "+sendList.getString("TITLE");
                errMsg += "\n=====BODY(본문): "+sendList.getString("BODY");
                errMsg += "\n=====LINK_TXT(링크문자): "+sendList.getString("LINK_TXT");
                errMsg += "\n=====LINK_URL(링크url): "+sendList.getString("LINK_URL");
                errMsg += "\n=====오류내용=====\n";
                errMsg += e.getMessage();
                LOGGER.error(errMsg);
            }
            catch(Exception e) {
                String errMsg = "=====UC알람전송오류발생(write)=====";
                errMsg += "\n=====파라미터=====";
                errMsg += "\n=====SEND_TYPE(전송유형): "+SEND_TYPE;
                errMsg += "\n=====SRV_CODE(발송서버코드): "+sendList.getString("SRV_CODE");
                errMsg += "\n=====RECIPIENT(수신자): "+sendList.getString("RECIPIENT");
                errMsg += "\n=====SEND(발신자 사번): "+sendList.getString("SEND");
                errMsg += "\n=====SENDER_ALIAS(발신자명): "+sendList.getString("SENDER_ALIAS");
                errMsg += "\n=====TITLE(제목): "+sendList.getString("TITLE");
                errMsg += "\n=====BODY(본문): "+sendList.getString("BODY");
                errMsg += "\n=====LINK_TXT(링크문자): "+sendList.getString("LINK_TXT");
                errMsg += "\n=====LINK_URL(링크url): "+sendList.getString("LINK_URL");
                errMsg += "\n=====오류내용=====\n";
                errMsg += e.getMessage();
                LOGGER.error(errMsg);
            }
            finally
            {
                if (dos != null) dos.close();
            }

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()), 1024);
            String recvStr = br.readLine();

            LOGGER.debug(URLDecoder.decode(recvStr, "UTF-8"));
            LOGGER.debug(URLDecoder.decode(recvStr, "EUC-KR"));

            if (recvStr.trim().contains("true") || "0".equals(recvStr.trim()))
            {
                retVal = true;
                LOGGER.debug("UC 전송 성공 : [" + recvStr.trim() + "]");
            }
            else
            {
                LOGGER.debug("UC 전송 실패 : [" + recvStr.trim() + "]");
            }
            br.close();

        }
        catch(Exception e)
        {
            String errMsg = "=====UC알람전송오류발생(send)=====";
            errMsg += "\n=====파라미터=====";
            errMsg += "\n=====SEND_TYPE(전송유형): "+SEND_TYPE;
            errMsg += "\n=====SRV_CODE(발송서버코드): "+sendList.getString("SRV_CODE");
            errMsg += "\n=====RECIPIENT(수신자): "+sendList.getString("RECIPIENT");
            errMsg += "\n=====SEND(발신자 사번): "+sendList.getString("SEND");
            errMsg += "\n=====SENDER_ALIAS(발신자명): "+sendList.getString("SENDER_ALIAS");
            errMsg += "\n=====TITLE(제목): "+sendList.getString("TITLE");
            errMsg += "\n=====BODY(본문): "+sendList.getString("BODY");
            errMsg += "\n=====LINK_TXT(링크문자): "+sendList.getString("LINK_TXT");
            errMsg += "\n=====LINK_URL(링크url): "+sendList.getString("LINK_URL");
            errMsg += "\n=====오류내용=====\n";
            errMsg += e.getMessage();
            LOGGER.error(errMsg);
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                } catch (IOException e) {}
            }
        }
        return;
    }
}
*/
