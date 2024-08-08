package kr.co.ibk.common.utils.Proj4j;

import lombok.Getter;

/**
 * @author : chiheon
 * @version : 1.0.0
 * @since  : 2022-04-11
 *
 * @apiNote  :  CoordinateSystemType for CRSFactory createFromParameters()
 * @see org.osgeo.proj4j.CRSFactory
 * ==================================================
 * DATE            AUTHOR          NOTE
 * --------------------------------------------------
 * 2022-04-11      chiheon         좌표계 변환 타입 생성
 */
public enum CoordinateSystemType {
    // Google Mercator: 구글지도/빙지도/야후지도/OSM 등 에서 사용중인 좌표계
    EPSG_3857("EPSG:3857", "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs"),

    // Bessel 1841 경위도: 한국과 일본에 잘 맞는 지역타원체를 사용한 좌표계
    EPSG_4004("EPSG:4004", "+proj=longlat +ellps=bessel +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43"),

    // GRS80 경위도: WGS84와 거의 유사
    EPSG_4019("EPSG:4019", "+proj=longlat +ellps=GRS80 +no_defs"),

    // WGS84(GPS가 사용하는 좌표계)
    EPSG_4326("EPSG:4326", "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs"),

    // UTM-K (Bessel): 새주소지도
    EPSG_5178("EPSG:5178", "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43"),

    // UTM-K (GRS80): 도로명주소
    EPSG_5179("EPSG:5179", "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs"),

    // 중부원점(GRS80) [200,000 500,000], 카카오, 공공데이터포탈
    ESPG_5181("ESPG:5181", "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs"),

    // 서부원점(GRS80)-falseY:60000
    EPSG_5185("EPSG:5185", "+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs"),

    // 중부원점(GRS80)-falseY:60000
    EPSG_5186("EPSG:5186", "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs"),

    // 동부원점(GRS80)-falseY:60000
    EPSG_5187("EPSG:5187", "+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs"),

    // 동해(울릉)원점(GRS80)-falseY:60000
    EPSG_5188("EPSG:5188", "+proj=tmerc +lat_0=38 +lon_0=131 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs;");

    @Getter
    private final String name;

    @Getter
    private final String paramStr;

    CoordinateSystemType(String name, String paramStr) {
        this.name = name;
        this.paramStr = paramStr;
    }
}
