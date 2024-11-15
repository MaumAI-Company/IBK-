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
            params.setPagingAt("Y");
            list = list(params);

            list.forEach(item -> {
                String[] strArr;
                String str;
                if (!ObjectUtils.isEmpty(item.getBdgtItexFrcsCon())) {
                    str = "";
                    strArr = item.getBdgtItexFrcsCon().split("\\|");
                    for (int i = 0; i < strArr.length; i++) {
                        str += (i + 1) + "순위 " + strArr[i] + "\n";
                    }
                    item.setBdgtItexFrcsCon(str);
                }
                if (!ObjectUtils.isEmpty(item.getBdgtBsnsFrcsCon())) {
                    str = "";
                    strArr = item.getBdgtBsnsFrcsCon().split("\\|");
                    for (int i = 0; i < strArr.length; i++) {
                        str += (i + 1) + "순위 " + strArr[i] + "\n";
                    }
                    item.setBdgtBsnsFrcsCon(str);
                }
                if (!ObjectUtils.isEmpty(item.getBdgtPrfrRsnFrcsCon())) {
                    str = "";
                    strArr = item.getBdgtPrfrRsnFrcsCon().split("\\|");
                    for (int i = 0; i < strArr.length; i++) {
                        str += (i + 1) + "순위 " + strArr[i] + "\n";
                    }
                    item.setBdgtPrfrRsnFrcsCon(str);
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
