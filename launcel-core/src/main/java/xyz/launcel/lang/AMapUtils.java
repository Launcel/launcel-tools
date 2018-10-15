package xyz.launcel.lang;

import lombok.Getter;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.log.RootLogger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public interface AMapUtils
{


    /**
     * @param lat 纬度
     * @param lng 经度
     *
     * @return Point
     */
    static Point newPoint(double lat, double lng)
    {
        return new Point(lat, lng);
    }

    /**
     * 误差在0.05公里左右，因获取经纬度也存在误差
     *
     * @param l1 point 1
     * @param l2 point 2
     *
     * @return two point legth
     */
    static double getDistance(Point l1, Point l2)
    {
        // 纬度的弧度差
        var latDiff = l1.getRadLat() - l2.getRadLat();
        // 经度的弧度差
        var lngDiff = l1.getRadLng() - l2.getRadLng();
        var s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(latDiff / 2), 2) + Math.cos(l1.getRadLat()) * Math.cos(l2.getRadLat()) * Math.pow(Math.sin(lngDiff / 2),
                        2)));
        s = s * 6378.137;
        RootLogger.info("Point({},{})与Point({},{})的距离={}", l1.getLng(), l1.getLat(), l2.getLng(), l2.getLat(), s);
        return s;
    }

    @Getter
    class Point
    {

        /**
         * 格式化
         */
        private static DecimalFormat decimalFormat = new DecimalFormat("0.0000000000", new DecimalFormatSymbols(Locale.US));

        private static double parse(double d)
        {
            return Double.parseDouble(decimalFormat.format(d));
        }

        /**
         * 纬度
         */
        private double lat;
        /**
         * 经度
         */
        private double lng;
        /**
         * 纬度弧度
         */
        private double radLat;
        /**
         * 经度弧度
         */
        private double radLng;

        Point(double lat, double lng)
        {
            this(lat, lng, true);
        }

        private Point(double lat, double lng, boolean check)
        {
            if (check)
            {
                if (lng < -180d || lng > 180d)
                {
                    ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "经度值不在范围内");
                }

                if (lat < -90d || lat > 90d)
                {
                    ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "纬度值不在范围内");
                }

                this.lng = parse(lng);
                radLng = getRadian(lng);

                this.lat = lat;
                radLat = getRadian(lat);
            }
        }

        /**
         * 获得当前角度的弧度值
         *
         * @param degree 角度
         *
         * @return 弧度
         */
        private double getRadian(double degree) { return degree * Math.PI / 180.0; }
    }
}
