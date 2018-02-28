package xyz.launcel.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public interface AMapUtils {


    /**
     * @param lat 纬度
     * @param lng 经度
     * @return
     */
    static Point newPoint(double lat, double lng) {
        return new Point(lat, lng);
    }

    /**
     * 误差在0.05公里左右，因获取经纬度也存在误差
     *
     * @param l1
     * @param l2
     * @return
     */
    static double getDistance(Point l1, Point l2) {
        Logger log = LoggerFactory.getLogger(AMapUtils.class);
        double latDiff = l1.getRadLat() - l2.getRadLat();// 纬度的弧度差
        double lngDiff = l1.getRadLng() - l2.getRadLng();// 经度的弧度差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latDiff / 2), 2) + Math.cos(l1.getRadLat()) * Math.cos(l2.getRadLat()) * Math.pow(Math.sin(lngDiff / 2), 2)));
        s = s * 6378.137;
        log.info("Point({},{})与Point({},{})的距离={}", l1.getLng(), l1.getLat(), l2.getLng(), l2.getLat(), s);
        return s;
    }

    class Point {

        /**
         * 格式化
         */
        private static DecimalFormat decimalFormat = new DecimalFormat("0.0000000000", new DecimalFormatSymbols(Locale.US));

        private static double parse(double d) {
            return Double.parseDouble(decimalFormat.format(d));
        }

        /**
         * 维度
         */
        private double lat;
        /**
         * 经度
         */
        private double lng;
        /**
         * 维度弧度
         */
        private double radLat;
        /**
         * 经度弧度
         */
        private double radLng;

        private double getLat() {
            return lat;
        }

        private void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        private void setLng(double lng) {
            this.lng = lng;
        }

        public double getRadLat() {
            return radLat;
        }

        private void setRadLat(double radLat) {
            this.radLat = radLat;
        }

        public double getRadLng() {
            return radLng;
        }

        private void setRadLng(double radLng) {
            this.radLng = radLng;
        }

        Point(double lat, double lng) {
            this(lat, lng, true);
        }

        private Point(double lat, double lng, boolean check) {
            if (check) {
                if (lng >= -180d && lng <= 180d) {
                    this.lng = parse(lng);
                    radLng = getRadian(lng);
                } //else
//                    throw new XBusinessException("ACTIVITY_SCORE_0001");
                if (lat >= -90d && lat <= 90d) {
                    this.lat = lat;
                    radLat = getRadian(lat);
                } //else
//                    throw new XBusinessException("ACTIVITY_SCORE_0002");
            }
        }

        /**
         * 获得当前角度的弧度值
         *
         * @param degree
         * @return
         */
        private double getRadian(double degree) {
            return degree * Math.PI / 180.0;
        }
    }
}
