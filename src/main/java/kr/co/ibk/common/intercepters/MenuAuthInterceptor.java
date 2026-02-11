package kr.co.ibk.common.intercepters;

import kr.co.ibk.common.annotation.MenuAuthBase;
import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.common.utils.StringHelper;
import kr.co.ibk.config.security.direct.UserDetails;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.service.CommonService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class MenuAuthInterceptor implements HandlerInterceptor {

    private final CommonService commonService;

    public MenuAuthInterceptor(CommonService commonService) {
        this.commonService = commonService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 허용 IP 검증
        if (!isAllowedIp(request)) {
            denyRequest(request, response, HttpServletResponse.SC_FORBIDDEN, "/nonAuth");
            return false;
        }

        // 요청(핸들러)에 선언된 권한 기준 경로(@MenuAuthBase) 목록 조회
        String[] authPaths = getAuthPaths(handler);

        // 권한 기준 경로가 없으면(애노테이션 미선언) 권한 체크를 하지 않고 통과
        if (authPaths.length == 0) {
            return true;
        }

        // 로그인 사용자 정보 조회
        MemberInfo memberInfo = getMemberInfo();
        // 비로그인 상태면 401 처리
        if (memberInfo == null) {
            denyRequest(request, response, HttpServletResponse.SC_UNAUTHORIZED, "/login");
            return false;
        }

        // 메뉴 권한 조회용 파라미터
        CustomMap param = new CustomMap();
        param.orginPut("seq", memberInfo.getMemSeq());
        param.orginPut("memId", memberInfo.getMemId());
        param.orginPut("roleId", memberInfo.getRoleId());
        // 사용자가 접근 가능한 메뉴 목록 조회
        List<CustomMap> menuTree = commonService.getMenuTree(param);
        // 메뉴 권한 자체가 없으면 403 처리
        if (CollectionUtils.isEmpty(menuTree)) {
            denyRequest(request, response, HttpServletResponse.SC_FORBIDDEN, "/nonAuth");
            return false;
        }
        // @MenuAuthBase에 지정된 경로들 중 하나라도 사용자 메뉴 URL과 일치하면 접근 허용
        for (String authPath : authPaths) {
            if (authPath.isEmpty()) {
                continue;
            }

            for (CustomMap menu : menuTree) {
                String menuUrl = menu.getString("menuUrl");
                if (menuUrl.isEmpty()) {
                    continue;
                }
                if (authPath.equals(menuUrl.trim())) {
                    return true;
                }
            }
        }

        // 매칭 실패 시 권한 없음(403)
        denyRequest(request, response, HttpServletResponse.SC_FORBIDDEN, "/nonAuth");
        return false;
    }

    private boolean isAllowedIp(HttpServletRequest request) {
        MemberInfo memberInfo = getMemberInfo();
        if (memberInfo == null) {
            return false;
        }

        String ipCheckYn = memberInfo.getMemColumn2(); // IP 검증 사용여부
        String allowedIps = memberInfo.getMemColumn1(); // 허용 IP

        if (!"Y".equalsIgnoreCase(ipCheckYn)) {
            return true;
        }

        // TODO :: 정책 결정 후 수정 필요
         if (allowedIps == null || allowedIps.trim().isEmpty()) {
            return true;
        }

        String clientIp = StringHelper.getClientIP(request);
        if (clientIp != null && clientIp.contains(",")) {
            clientIp = clientIp.split(",")[0].trim();
        }
        if (clientIp == null || clientIp.isEmpty()) {
            return false;
        }

        for (String allowedIp : allowedIps.split(",")) {
            if (clientIp.equals(allowedIp.trim())) {
                return true;
            }
        }
        return false;
    }

    private void denyRequest(HttpServletRequest request,
                             HttpServletResponse response,
                             int status,
                             String redirectPath) throws Exception {
        String redirectUrl = request.getContextPath() + redirectPath;
        if (isAjaxRequest(request)) {
            response.setStatus(status);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            return;
        }
        response.sendRedirect(redirectUrl);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        // 표준 AJAX 헤더 체크하여 비동기호출 여부 확인
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith);
    }

    private MemberInfo getMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getMemberInfo();
        }
        return null;
    }

    private static String[] getAuthPaths(Object handler) {

        // 컨트롤러 메서드 요청이 아니면 권한 경로 없음
        if (!(handler instanceof HandlerMethod)) {
            return new String[0];
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 메소드 레벨 @MenuAuthBase 우선
        MenuAuthBase methodAuth = handlerMethod.getMethodAnnotation(MenuAuthBase.class);
        if (methodAuth != null) {
            return Arrays.stream(methodAuth.value())
                    .filter(path -> path != null && !path.trim().isEmpty())
                    .toArray(String[]::new);
        }

        // 없으면 클래스 레벨 @MenuAuthBase 사용
        MenuAuthBase classAuth = handlerMethod.getBeanType().getAnnotation(MenuAuthBase.class);
        if (classAuth == null) {
            return new String[0];
        }
        return Arrays.stream(classAuth.value())
                .filter(path -> path != null && !path.trim().isEmpty())
                .toArray(String[]::new);
    }
}
