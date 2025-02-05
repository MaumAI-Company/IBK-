package kr.co.ibk.service;

import kr.co.ibk.domain.web.StatisticInfo;
import kr.co.ibk.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticService extends _BaseService {

    private final StatisticRepository statisticRepository;

    public List<StatisticInfo> getStatisticInput() {
        List<StatisticInfo> statisticInput = statisticRepository.getStatisticInput();

        if (!ObjectUtils.isEmpty(statisticInput)) {
            for (StatisticInfo info : statisticInput) {
                info.setPer(info.getOutCnt() / info.getTotal() * 100);
            }
        }

        return statisticInput;
    }

    public List<StatisticInfo> getStatisticOutput() {
        List<StatisticInfo> statisticOutput = statisticRepository.getStatisticOutput();
        if (!ObjectUtils.isEmpty(statisticOutput)) {
            for (StatisticInfo info : statisticOutput) {
                info.setHitCnt1Per((info.getHitCnt2() / info.getTotal() * 100));
                info.setHitCnt2Per((info.getHitCnt2() / info.getTotal() * 100));
                info.setHitCnt3Per((info.getHitCnt3() / info.getTotal() * 100));
            }
        }
        return statisticOutput;
    }
}
