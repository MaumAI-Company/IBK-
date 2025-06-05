package kr.co.ibk.service;

import kr.co.ibk.domain.web.CardInputInfo;
import kr.co.ibk.model.CardInputForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.CardInputRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardInputService extends _BaseService {

    private final CardInputRepository cardInputRepository;

    @Value("${excel.data.partition.count}")
    private int partitionCount;

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
                        str += (i + 1) + "순위 " + strArr[i] + "\r\n";
                    }
                    item.setBdgtItexFrcsCon(str);
                }
                if (!ObjectUtils.isEmpty(item.getBdgtPrfrRsnFrcsCon())) {
                    str = "";
                    strArr = item.getBdgtPrfrRsnFrcsCon().split("\\|");
                    for (int i = 0; i < strArr.length; i++) {
                        str += (i + 1) + "순위 " + strArr[i] + "\r\n";
                    }
                    item.setBdgtPrfrRsnFrcsCon(str);
                }
                if (!ObjectUtils.isEmpty(item.getBdgtBsnsFrcsCon())) {
                    str = "";
                    strArr = item.getBdgtBsnsFrcsCon().split("\\|");
                    for (int i = 0; i < strArr.length; i++) {
                        str += (i + 1) + "순위 " + strArr[i] + "\r\n";
                    }
                    item.setBdgtBsnsFrcsCon(str);
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


    public void reportCardExcelDown(HttpServletResponse response, List<CardInputInfo> excelList) throws IOException {
        int batchSize = partitionCount;
        int totalRecords = excelList.size();
        int fileIndex = 1;
        int processedRecords = 0;

        String zipFileName = "BC카드_지급결의_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".zip";
        String tempDir = System.getProperty("java.io.tmpdir"); // OS별 임시 폴더 사용
        List<File> generatedFiles = new java.util.ArrayList<>();

        while (processedRecords < totalRecords) {
            String fileName = "BC카드_지급결의_" + fileIndex + "_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
            File excelFile = new File(tempDir, fileName);
            generatedFiles.add(excelFile);

            try (SXSSFWorkbook workbook = new SXSSFWorkbook(100);
                 FileOutputStream fileOutputStream = new FileOutputStream(excelFile)) {

                Sheet sheet = workbook.createSheet("BC카드 지급결의 내역");

                String[] colNames1 = {
                        "No", "대상", "부점코드", "카드번호", "승인시간", "영업일 여부", "매출금액",
                        "가맹점명", "업종명", "가맹점 사업자 등록번호", "매출 가맹점 번호", "가맹점 업종코드",
                        "가맹점 상세 주소", "부점주소", "예산관리비목관리번호", "예산집행사유코드", "사업세부사업", "결과등록년월일"
                };

                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
                headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerCellStyle.setBorderBottom(BorderStyle.THIN);
                headerCellStyle.setBorderLeft(BorderStyle.THIN);
                headerCellStyle.setBorderRight(BorderStyle.THIN);
                headerCellStyle.setBorderTop(BorderStyle.THIN);

                Row row = sheet.createRow(0);
                for (int i = 0; i < colNames1.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(colNames1[i]);
                    cell.setCellStyle(headerCellStyle);
                }

                int rowCnt = 1;
                int batchEnd = Math.min(processedRecords + batchSize, totalRecords);

                for (int i = processedRecords; i < batchEnd; i++) {
                    CardInputInfo item = excelList.get(i);
                    row = sheet.createRow(rowCnt++);
                    int cellCnt = 0;

                    row.createCell(cellCnt++).setCellValue(totalRecords - i); // No
                    row.createCell(cellCnt++).setCellValue(item.getHdqrBobDcd() == null ? "-" : ("1".equals(item.getHdqrBobDcd()) ? "본부" : "영업점"));
                    row.createCell(cellCnt++).setCellValue(item.getBrcd() == null ? "-" : item.getBrcd());
                    row.createCell(cellCnt++).setCellValue(item.getCdn() == null ? "-" : item.getCdn());
                    row.createCell(cellCnt++).setCellValue(item.getBdgtTstmUseHms() == null ? "-" : item.getBdgtTstmUseHms());
                    row.createCell(cellCnt++).setCellValue(item.getBzdyYn() == null ? "-" : item.getBzdyYn());
                    row.createCell(cellCnt++).setCellValue(item.getAmslAmt() == null ? "-" : item.getAmslAmt().toString());
                    row.createCell(cellCnt++).setCellValue(item.getAfstNm() == null ? "-" : item.getAfstNm());
                    row.createCell(cellCnt++).setCellValue(item.getTpbsNm() == null ? "-" : item.getTpbsNm());
                    row.createCell(cellCnt++).setCellValue(item.getAfstBzn() == null ? "-" : item.getAfstBzn());
                    row.createCell(cellCnt++).setCellValue(item.getAmslAfstNo() == null ? "-" : item.getAmslAfstNo());
                    row.createCell(cellCnt++).setCellValue(item.getAfstTpbcd() == null ? "-" : item.getAfstTpbcd());
                    row.createCell(cellCnt++).setCellValue(item.getAfstDtlAdr() == null ? "-" : item.getAfstDtlAdr());
                    row.createCell(cellCnt++).setCellValue(item.getBrncAdr() == null ? "-" : item.getBrncAdr());
                    row.createCell(cellCnt++).setCellValue(item.getBdgtItexFrcsCon() == null ? "-" : item.getBdgtItexFrcsCon());
                    row.createCell(cellCnt++).setCellValue(item.getBdgtBsnsFrcsCon() == null ? "-" : item.getBdgtBsnsFrcsCon());
                    row.createCell(cellCnt++).setCellValue(item.getBdgtPrfrRsnFrcsCon() == null ? "-" : item.getBdgtPrfrRsnFrcsCon());
                    row.createCell(cellCnt++).setCellValue(item.getRsreYmd() == null ? "-" : item.getRsreYmd());

                    if (rowCnt % 100 == 0) {
                        ((SXSSFSheet) sheet).flushRows(100);
                    }
                }

                workbook.write(fileOutputStream);
                workbook.dispose();
            }

            processedRecords += batchSize;
            fileIndex++;
        }

        File zipFile = new File(tempDir, zipFileName);

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            for (File file : generatedFiles) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOut.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }
            }
        }

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(zipFileName.getBytes("UTF-8"), "8859_1"));

        try (InputStream inputStream = Files.newInputStream(Paths.get(zipFile.getAbsolutePath()));
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }

        for (File file : generatedFiles) {
            file.delete();
        }
        zipFile.delete();
    }
}
