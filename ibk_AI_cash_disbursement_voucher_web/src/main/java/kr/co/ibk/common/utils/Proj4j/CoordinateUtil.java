package kr.co.ibk.common.utils.Proj4j;


import org.osgeo.proj4j.*;

/**
 * @author : chiheon
 * @version : 1.0.0
 * @since  : 2022-04-11
 *
 * @apiNote   : 좌표계 변환 Util Class
 * @link : https://www.osgeo.kr/17
 * @link : https://blog.tadadakcode.com/3
 * @link : https://yganalyst.github.io/spatial_analysis/spatial_analysis_3/
 * ==================================================
 * DATE            AUTHOR          NOTE
 * --------------------------------------------------
 * 2022-04-11      chiheon
 */
public class CoordinateUtil {

    /*public static void main(String[] args) {
        ProjCoordinate projCoordinate = new ProjCoordinate();
        projCoordinate.x = 964744.1470401846;
        projCoordinate.y = 1957576.0253509348;

        ProjCoordinate EPSG_4326 = transform(
                projCoordinate,
                CoordinateSystemType.EPSG_5179,
                CoordinateSystemType.EPSG_4326
        );
        System.out.format("도로명 -> EPSG_4326 %s, %s \n", EPSG_4326.x, EPSG_4326.y);
    }
*/
    public static ProjCoordinate transform(ProjCoordinate projCoordinate, CoordinateSystemType beforeTy, CoordinateSystemType afterTy) {
        CoordinateTransformFactory ctfactory = new CoordinateTransformFactory();
        final CRSFactory crsFactory = new CRSFactory();

        // UTM-K 좌표계, 네이버, 도로명주소
        CoordinateReferenceSystem before = crsFactory.createFromParameters(beforeTy.getName(), beforeTy.getParamStr());
        CoordinateReferenceSystem after = crsFactory.createFromParameters(afterTy.getName(), afterTy.getParamStr());

        final CoordinateTransform trans = ctfactory.createTransform(before, after);
        return trans.transform(projCoordinate, new ProjCoordinate());
    }
}
