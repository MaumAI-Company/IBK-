package kr.co.ibk.common.aop;

import com.google.common.base.Joiner;
import kr.co.ibk.common.Base;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * AOP 메소드 실행시 로그를 남김.
 * @author user
 * @see https://jeong-pro.tistory.com/171
 */
//@Aspect
@Component
public class LogAspect extends Base {
  

	/**
	 * controller 이하 실행시 로그를 남김.
	 * 원래계획은 Controller 모두 로그를 남기려고 하였으나 오류발생되어 온라인디렉토리만 시범적으로 적용.
	 * kr.co.ibk.web.controller 를 추가하면 오류가 발생되어 건별로 일단추가. 2019.12.17
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* kr.co.ibk.web.controller..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		return logPrint_2(joinPoint);
    }

	

	private Integer level = 0;
	/**
	 * 실제로그를 출력함. 오류발생시 level이 정상적으로 줄어들지 않는 문제가 있는듯 하여 일단중지.
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	/*
	private Object logPrint_ORG(ProceedingJoinPoint joinPoint) throws Throwable {
		String typeName = joinPoint.getSignature().getDeclaringTypeName();
        try {
        	level++;
            if (log.isInfoEnabled() ) {
                log.info("{}==>> {}.{}() with argument[s] = {}", level,  typeName,
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
            }
            Object result = joinPoint.proceed(); 
            if (log.isInfoEnabled() ) {
                log.info("{}<<== {}.{}() with result = {}", level, typeName,
                    joinPoint.getSignature().getName(), result);
            }
            level--;
            return result;
        } catch (IllegalArgumentException e) {
            log.error("*** Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
	}
	*/
	
	
	/**
	 * 로그를 출력함. 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	private Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {

		String classFullName = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName(); 
		
		String classAndmethodName = classFullName + "." + methodName + "()"; 
		
    	level++;
    	log.info("{}==>> {} with argument[s] = {}", level,  classAndmethodName, Arrays.toString(joinPoint.getArgs()));
    	
        Object result = null; 
        try {
        	result = joinPoint.proceed();
        } catch (Exception e) {
            log.warn("{}==** {} with Exception={}", level, classAndmethodName, e.getMessage()) ;
            throw e;
        } finally {
	        log.info("{}<<== {} with result = {}", level, classAndmethodName, result);
	        level--;

	        //아래로직으로 에러가 전달되지 않는듯....
	        if ( result instanceof Exception ) {
	        	log.warn("==with error:{}", ((Exception) result).getMessage());
	        }
        }
        return result;
        
	}


	/**
	 * 로그를 출력함.
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	private Object logPrint_2(ProceedingJoinPoint joinPoint) throws Throwable {
		String params = getRequestParams(); // request 값 가져오기
		String classFullName = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		String classAndMethodName = classFullName + "." + methodName + "()";

		long startAt = System.currentTimeMillis();

		Object result = null;
		try {
			result = joinPoint.proceed(); // 4
		} catch (Throwable e) {
			log.warn("{}==** {} with Exception={}", level, classAndMethodName, e.getMessage()) ;
		} finally {
			long endAt = System.currentTimeMillis();

			if(classFullName.equals("kr.co.ibk.web.controller.rest.system.MenuResource") ||
					classFullName.equals("kr.co.ibk.web.controller.rest.common.CommonFuncResource")) {
				log.info("-----------> RESPONSE : {}({}) = {} ({}ms)", classFullName, methodName, "", endAt - startAt);
			}else{
				log.info("-----------> RESPONSE : {}({}) = {} ({}ms)", classFullName, methodName, result, endAt - startAt);
			}
		}

		return result;

	}

	private String paramMapToString(Map<String, String[]> paramMap) {
		return paramMap.entrySet().stream()
				.filter(entry -> entry.getValue() != null)
				.map(entry ->
						String.format("%s -> (%s)", entry.getKey(), Joiner.on(",").join(entry.getValue()))
				)
				.collect(Collectors.joining(", "));
	}

	// Get request values
	private String getRequestParams() {

		String params = "없음";

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes(); // 3

		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();

			Map<String, String[]> paramMap = request.getParameterMap();
			if (!paramMap.isEmpty()) {
				params = " [" + paramMapToString(paramMap) + "]";
			}
		}

		return params;

	}
	
	
	/*
	 * java.lang.NullPointerException: null	
	 * at kr.co.ibk.web.controller.MainController.topBannerList(MainController.java:166) ~[classes/:na]
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        log.info("start - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
        Object result = pjp.proceed();
        log.info("finished - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
        return result;
    }
	*/
	
	
	
	/*
	public Object loggingController(ProceedingJoinPoint joinPoint) throws Throwable {

		String signatureString = joinPoint.getSignature().toString();
		
		level++;
		String levelPad = level.toString(); 
		
		log.debug(levelPad + "==>> " + signatureString );
		long start = System.currentTimeMillis();
		Object result = null ;
		try {
			result = joinPoint.proceed();
			return result;
		} finally {
			long finish = System.currentTimeMillis();
			long tottime = (finish - start); 
			
			if( tottime > 1000 * 60 ) {
				log.warn(levelPad + "<<== " + signatureString + " time:" + (finish - start)/(1000*60) + "min.");
			} else if( (finish - start) > 1000 ) {
				log.debug(levelPad + "<<== " + signatureString + " time:" + (finish - start)/1000 + "sec.");
			} else {
				log.debug(levelPad + "<<== " + signatureString + " time:" + (finish - start) + "ms.");
			}
			level--;

			if ( result instanceof DataAccessException) {
				log.warn("**finally SQL EXCEPTION : " + result.toString()); 
			}
		}
	}
	*/

	/*
	public Object loggingOrg(ProceedingJoinPoint joinPoint) throws Throwable {
		Object retValue = null;
		log.info("***********로그시작");
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			retValue = joinPoint.proceed(); // invoke()같은것

		} catch (Throwable e) {

		} finally {
			stopWatch.stop();
			log.info("**********기록종료");
			log.info(joinPoint.getSignature().getName() + "메서드 실행시간"
					+ stopWatch.getTotalTimeMillis());
		}
		return retValue;
	}
	*/
	

}
