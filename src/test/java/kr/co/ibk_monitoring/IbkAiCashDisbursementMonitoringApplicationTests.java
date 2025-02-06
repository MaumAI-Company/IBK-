package kr.co.ibk_monitoring;

import kr.co.ibk_monitoring.service.ApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IbkAiCashDisbursementMonitoringApplicationTests {

    @Autowired
    private ApiService apiService;

    @Test
    void contextLoads() {
    }

    @Test
    void serverCheck() {
        apiService.serverCheck();
    }

}
