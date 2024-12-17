package kr.co.ibk.model.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

@Getter
@Setter
public class Criteria {
    /**
     * 현재 페이지 번호
     */
    private int currentPageNo;

    /**
     * 페이지당 출력할 데이터 개수
     */
    private int recordsPerPage;

    /**
     * 화면 하단에 출력할 페이지 사이즈
     */
    private int pageSize;

    /**
     * 검색 유형
     */
    private String searchType;

    /**
     * 검색 키워드
     */
    private String searchKeyword;

    /**
     * 조회 기간
     */
    private String searchStartDate;
    private String searchEndDate;
    private String searchAllDateAt;

    /*조회 대상*/
    private String searchTarget;

    private String searchJson;
    private HashMap<String, String> searchJsonMap;
    private String searchTypeJson;

	/*public String getSearchStartDate() {
		if (this.searchStartDate == null || this.searchStartDate.length() < 1) {
			this.searchStartDate = initialDate("start");
		}
		return this.searchStartDate;
	}
	public String getSearchEndDate() {
		if (this.searchEndDate == null || this.searchEndDate.length() < 1) {
			this.searchEndDate = initialDate("end");
		}
		return this.searchEndDate;
	}*/

    public String initialDate(String type) {

        // 날짜 포멧 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        // 반환 날짜 타입
        String returnDate = "";

        // 날자 셋팅 시작
        Calendar cal = Calendar.getInstance(Locale.KOREA);

        if ("start".equals(type)) {
            // 현재날짜 - 2 달전 8~10월 3개월 데이터
            cal.setTime(date);
            cal.add(Calendar.MONTH, -2);
            returnDate = sdf.format(cal.getTime());
        } else if ("end".equals(type)) {
            // 현재날짜
            cal.setTime(date);
            returnDate = sdf.format(cal.getTime());
        }

        return returnDate;
    }

    public Criteria() {
        this(1, 20, 10, null, null);// 없으면 defalut 1번, 10개 씩 , 10페이지
    }

    public Criteria(int currentPageNo, int recordsPerPage, int pageSize, String searchStartDate, String searchEndDate) {
        this.currentPageNo = currentPageNo;
        this.recordsPerPage = recordsPerPage;
        this.pageSize = pageSize;
        this.searchStartDate = searchStartDate;
        this.searchEndDate = searchEndDate;

    }

    public int getStartPage() {
        return (currentPageNo - 1) * recordsPerPage;
    }

    public String makeQueryString(int pageNo) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .queryParam("currentPageNo", pageNo)
                .queryParam("recordsPerPage", recordsPerPage)
                .queryParam("pageSize", pageSize)
                .queryParam("searchType", searchType)
                .queryParam("searchKeyword", searchKeyword)
                .queryParam("searchStartDate", searchStartDate)
                .queryParam("searchEndDate", searchEndDate)
                .build()
                .encode();
        return uriComponents.toUriString();
    }
}
