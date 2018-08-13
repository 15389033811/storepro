package storepro.util;

import javax.servlet.http.HttpServletRequest;

//将前端传来的String值解析为相应的值，因为前端无论什么类型都传String
public class HttpServletRequestUtil {//通过前端传来的key值进行转换,这个key值通过前端设定好，我们后台直接去取出

    public static int getInt(HttpServletRequest request, String key) {//操作前端传来的int值
        try {
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static Long getLong(HttpServletRequest request, String key) {//操作前端传来的Long值
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1L;
        }

    }

    public static Double getDouble(HttpServletRequest request, String key) {//操作前端传来的double值
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1d;
        }

    }

    public static Boolean getBoolean(HttpServletRequest request, String key) {//操作前端传来的boolean值
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false;
        }

    }

    public static String getString(HttpServletRequest request, String key) {//操作前端传来的Stirng值
        try {
            String result = request.getParameter(key);
            if (request != null) {
                result = result.trim();
            }
            if ("".equals(result)) {//如果字符串为空
                result = null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }

    }


}
