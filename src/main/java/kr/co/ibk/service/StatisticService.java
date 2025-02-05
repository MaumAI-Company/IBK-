package kr.co.ibk.service;

import kr.co.ibk.domain.web.StatisticInfo;
import kr.co.ibk.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticService extends _BaseService {

    private final StatisticRepository statisticRepository;

    public List<StatisticInfo> getStatisticInput() {
        return statisticRepository.getStatisticInput();
    }

    public List<StatisticInfo> getStatisticOutput() {
        return statisticRepository.getStatisticOutput();
    }
}
