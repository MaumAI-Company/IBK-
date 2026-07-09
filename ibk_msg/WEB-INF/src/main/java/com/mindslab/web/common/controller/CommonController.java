package com.mindslab.web.common.controller;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.LocaleResolver;

import com.mindslab.web.common.service.CommonService;
import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.common.vo.ResponseVO;
import com.mindslab.web.properties.MidsLabCustomProperties;
import com.mindslab.web.properties.MindsLabProperties;
import com.mindslab.web.properties.SystemProperties;
import com.mindslab.web.vo.MemberVO;

public class CommonController {
    @Autowired
	@Qualifier("messageSource")
    protected MessageSource messageSource;

    @Autowired
    protected SystemProperties systemProperties;

    @Autowired
    protected LocaleResolver localeResolver;

    @Autowired
    protected MindsLabProperties mindsLabProperties;

    @Autowired
    protected MidsLabCustomProperties midsLabCustomProperties;
    
    @Autowired
    protected CommonService commonService;
    
    // 생성자
	public static String RSA_WEB_KEY ="";
	public static String RSA_INSTANCE ="";
	public static PrivateKey RSA_PRIVATE_KEY = null;
	
	/**생성자 만들어주는 모습 **/	
	public CommonController() {
		 RSA_WEB_KEY = "_RSA_WEB_Key_ibk"; // 개인키 session key 
		 RSA_INSTANCE = "RSA"; // rsa transformation :: RSA 고정
	}

    public boolean isLocal() {
        return systemProperties.isLocal();
    }

    public boolean isDev() {
        return systemProperties.isDev();
    }
    public boolean isReal() {
        return systemProperties.isReal();
    }
    
    public JSONArray subMenu(MemberVO sessionMember){
    	HashMap<String, Object> subMenuMap = new HashMap<String, Object>();
    	CustomMap param = new CustomMap();
    	
    	List<CustomMap> menuTree = new ArrayList();
    	
    	param.put("seq", sessionMember.getMemSeq());
    	param.put("memId", sessionMember.getMemId());
    	param.put("roleId", sessionMember.getRoleId());
    	
    	//메뉴트리목록을 가져온다.
    	menuTree = commonService.getMenuTree(param);
    	//메뉴트리목록 건수
    	
    	List<CustomMap> resultMenuTree = new ArrayList();
    	
    	resultMenuTree = menuTree;
    	

        JSONArray arr = new JSONArray(resultMenuTree);
        	
        return arr;
    }
    
	protected <T> ResponseVO<T> makeResponseData(HttpStatus status,
			T resultData){
		ResponseVO<T> response = new ResponseVO<T>();

		response.getHeader().setStatus(status.value());
		response.getBody().setDocCnt(1);
		response.getBody().setDoc(resultData);

		return response;
	}

	protected <T> ResponseVO<T> makeResponseData(HttpStatus status,
			List<T> resultDataList){
		ResponseVO<T> response = new ResponseVO<T>();

		response.getHeader().setStatus(status.value());
		response.getBody().setDocCnt(resultDataList.size());
		response.getBody().setDocs(resultDataList);

		return response;
	}
	
	// RSA 

    /**
     * 복호화
     * 
     * @param privateKey
     * @param securedValue
     * @return
     * @throws Exception
     */
    public static String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance(CommonController.RSA_INSTANCE);
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }
 
    /**
     * 16진 문자열을 byte 배열로 변환한다.
     * 
     * @param hex
     * @return
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) { return new byte[] {}; }
 
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
	
	
    /**
     * rsa 공개키, 개인키 생성
     * 
     * @param request
     */
    public void initRsa(HttpServletRequest request) {
        HttpSession session = request.getSession();
 
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(CommonController.RSA_INSTANCE);
            generator.initialize(1024);
 
            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance(CommonController.RSA_INSTANCE);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
 
            session.setAttribute(CommonController.RSA_WEB_KEY, privateKey); // session에 RSA 개인키를 세션에 저장
            RSA_PRIVATE_KEY = privateKey;
 
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
 
            request.setAttribute("RSAModulus", publicKeyModulus); // rsa modulus 를 request 에 추가
            request.setAttribute("RSAExponent", publicKeyExponent); // rsa exponent 를 request 에 추가
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
