package kr.co.ibk.service;

import kr.co.ibk.domain.web.CardInputInfo;
import kr.co.ibk.model.CardInputForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.CardInputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardInputService extends _BaseService {

    private final CardInputRepository cardInputRepository;

    public List<CardInputInfo> page(CardInputForm params) {
        int totalCount = cardInputRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);
        params.setPaginationInfo(paginationInfo);

        List<CardInputInfo> list = new ArrayList<>();
        if (totalCount > 0) {
            if (ObjectUtils.isEmpty(params.getSorting())) {
                params.setSorting("desc");
            }
            list = list(params);
            params.setPagingAt("Y");

            list.forEach(item -> {
                if (!ObjectUtils.isEmpty(item.getBdgtItexFrcsCon())) {
                    item.setBdgtItexFrcsCon(item.getBdgtItexFrcsCon().split("\\|")[0]);
                }
                if (!ObjectUtils.isEmpty(item.getBdgtBsnsFrcsCon())) {
                    item.setBdgtBsnsFrcsCon(item.getBdgtBsnsFrcsCon().split("\\|")[0]);
                }
                if (!ObjectUtils.isEmpty(item.getBdgtPrfrRsnFrcsCon())) {
                    item.setBdgtPrfrRsnFrcsCon(item.getBdgtPrfrRsnFrcsCon().split("\\|")[0]);
                }
            });
        }

        return list;
    }

    public List<CardInputInfo> list(CardInputForm params) {
        if (!ObjectUtils.isEmpty(params.getPagingAt()) && "Y".equals(params.getPagingAt())) {
            return cardInputRepository.getList(params);
        }
        return cardInputRepository.getAllList(params);
    }

    public CardInputInfo detail(CardInputForm params) {
        return cardInputRepository.getDetail(params);
    }

}
