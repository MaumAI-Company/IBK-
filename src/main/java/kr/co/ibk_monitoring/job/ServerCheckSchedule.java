package kr.co.ibk_monitoring.job;

import kr.co.ibk_monitoring.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class ServerCheckSchedule {

    private static final Logger log = LoggerFactory.getLogger(ServerCheckSchedule.class);
    private final ApiService apiService;

    /* 속성
	*           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
	초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
	cron = "1 * * * * *"
	initialDelay 스케줄러에서 메소드가 등록되자마자 수행하는 것이 아닌 초기 지연시간을 설정
	fixedDelay (작업 수행 시간을 포함하여) 작업을 마친 후부터 주기 타이머가 돌아 메소드를 호출
	fixedRate 작업 수행시간과 상관없이 일정 주기마다 메소드를 호출
	@Scheduled(fixedRateString = "5", initialDelay = 3000)
	*/
    //	@Scheduled(fixedDelay = 1000 * 60 * 60 * 12, initialDelay = 0)

    /**
     * 3분 단위로 MCC서버, 웹서버 체크
     */
    @Scheduled(fixedDelay = 1000 * 60 * 3, initialDelay = 0)
    public void serverCheck() {
        log.debug("### MCC, Web 서버 모니터링 시작");
        apiService.serverCheck();
    }
}
