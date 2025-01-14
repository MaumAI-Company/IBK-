package kr.co.ibk.service;

import kr.co.ibk.domain.web.BillInputInfo;
import kr.co.ibk.model.BillInputForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.BillInputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BillInputService extends _BaseService {

    private final BillInputRepository billInputRepository;

    public List<BillInputInfo> page(BillInputForm params) {
        int totalCount = billInputRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);
        params.setPaginationInfo(paginationInfo);

        List<BillInputInfo> list = new ArrayList<>();
        if (totalCount > 0) {
            if (ObjectUtils.isEmpty(params.getSorting())) {
                params.setSorting("desc");
            }
            params.setPagingAt("Y");
            list = list(params);

            list.forEach(item -> {
                String[] strArr;
                String str;
                if (!ObjectUtils.isEmpty(item.getBdgtItexFrcsPrbCon())) {
                    str = "";
                    strArr = item.getBdgtItexFrcsPrbCon().split("\\|");
                    for (int i = 0; i < strArr.length; i++) {
                        str += (i + 1) + "순위 " + strArr[i] + "\r\n";
                    }
                    item.setBdgtItexFrcsPrbCon(str);
                }
                if (!ObjectUtils.isEmpty(item.getBdgtPrfrRsnFrcsCon())) {
                    str = "";
                    strArr = item.getBdgtPrfrRsnFrcsCon().split("\\|");
                    for (int i = 0; i < strArr.length; i++) {
                        str += (i + 1) + "순위 " + strArr[i] + "\r\n";
                    }
                    item.setBdgtPrfrRsnFrcsCon(str);
                }
                if (!ObjectUtils.isEmpty(item.getBdgtBsnsFrcsPrbCon())) {
                    str = "";
                    strArr = item.getBdgtBsnsFrcsPrbCon().split("\\|");
                    for (int i = 0; i < strArr.length; i++) {
                        str += (i + 1) + "순위 " + strArr[i] + "\r\n";
                    }
                    item.setBdgtBsnsFrcsPrbCon(str);
                }
            });
        }

        return list;
    }

    public List<BillInputInfo> list(BillInputForm params) {
        if (!ObjectUtils.isEmpty(params.getPagingAt()) && "Y".equals(params.getPagingAt())) {
            return billInputRepository.getList(params);
        }
        return billInputRepository.getAllList(params);
    }

    public BillInputInfo detail(BillInputForm params) {
        return billInputRepository.getDetail(params);
    }
}
