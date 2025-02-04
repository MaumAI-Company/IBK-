package kr.co.ibk.service;

import kr.co.ibk.common.utils.NullHelper;
import kr.co.ibk.domain.web.BillInputInfo;
import kr.co.ibk.model.BillInputForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.BillInputRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public void reportBillExcelDown(HttpServletResponse response, List<BillInputInfo> excelList) throws UnsupportedEncodingException {
        final String fileName = "세금계산서 지급결의 내역_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";

        final String[] colNames1 = {
                "No"
                , "대상"
                , "세금계산서구분코드"
                , "공급자사업자번호"
                , "공급자상호명"
                , "공급자업태명"
                , "공급자종목명"
                , "발행금액"
                , "세금계산서품목명"
                , "예산관리비목관리번호"
                , "예산집행사유코드"
                , "사업세부사업"
                , "결과등록년월일"
        };

        final int[] colWidths = {4000, 8000, 6000, 8000, 8000, 6000, 4000, 6000, 8000, 8000, 8000, 8000, 8000};

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = null;
        XSSFCell cell = null;
        XSSFRow row = null;

        int rowCnt = 0;

        sheet = workbook.createSheet("세금계산서 지급결의 내역");

        // 제목 스타일
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);

        // 텍스트 형식 스타일 (가운데 정렬)
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        // 텍스트 형식 스타일 (왼쪽 정렬)
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.LEFT);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);

        // 숫자 형식 스타일
        CellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle3.setBorderBottom(BorderStyle.THIN);
        cellStyle3.setBorderLeft(BorderStyle.THIN);
        cellStyle3.setBorderRight(BorderStyle.THIN);
        cellStyle3.setBorderTop(BorderStyle.THIN);
        cellStyle3.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

        row = sheet.createRow(rowCnt++);
        for (int i = 0; i < colNames1.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(colNames1[i]);
            sheet.setColumnWidth(i, colWidths[i]);
            cell.setCellStyle(headerCellStyle);
        }
        int cellCnt = 0;

        AtomicInteger index = new AtomicInteger();
        int totalRecords = excelList.size(); // No
        for (BillInputInfo item : excelList) {
            index.getAndIncrement();

            cellCnt = 0;
            row = sheet.createRow(rowCnt++);

            cell = row.createCell(cellCnt++); // No
            cell.setCellValue(totalRecords--);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 대상
            cell.setCellValue(NullHelper.isNull(item.getHdqrBobDcd()) ? "-" : ("1".equals(item.getHdqrBobDcd()) ? "본부" : "영업점"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 세금계산서구분코드
            cell.setCellValue(NullHelper.isNull(item.getTxblDcd()) ? "-" : item.getTxblDcd());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 공급자사업자번호
            cell.setCellValue(NullHelper.isNull(item.getSplrBsnnNo()) ? "-" : item.getSplrBsnnNo());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 공급자상호명
            cell.setCellValue(NullHelper.isNull(item.getSplrFrm()) ? "-" : item.getSplrFrm());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 공급자업태명
            cell.setCellValue(NullHelper.isNull(item.getSplrBzstNm()) ? "-" : item.getSplrBzstNm());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 공급자종목명
            cell.setCellValue(NullHelper.isNull(item.getSplrItmsNm()) ? "-" : item.getSplrItmsNm());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 발행금액
            cell.setCellValue(String.valueOf(NullHelper.isNull(item.getIssAmt()) ? "-" : item.getIssAmt()));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 세금계산서품목명
            cell.setCellValue(NullHelper.isNull(item.getTxblLsarNm()) ? "-" : item.getTxblLsarNm());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 예산관리비목관리번호
            cell.setCellValue(NullHelper.isNull(item.getBdgtItexFrcsPrbCon()) ? "-" : item.getBdgtItexFrcsPrbCon());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 사업세부사업
            cell.setCellValue(NullHelper.isNull(item.getBdgtPrfrRsnFrcsCon()) ? "-" : item.getBdgtPrfrRsnFrcsCon());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 예산집행사유코드
            cell.setCellValue(NullHelper.isNull(item.getBdgtBsnsFrcsPrbCon()) ? "-" : item.getBdgtPrfrRsnFrcsCon());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(cellCnt++); // 결과등록년월일
            cell.setCellValue(NullHelper.isNull(item.getRsreYmd()) ? "-" : item.getRsreYmd());
            cell.setCellStyle(cellStyle);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("KSC5601"), "8859_1"));

        try {
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
