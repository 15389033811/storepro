package storepro.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil  {//判断验证码是否相同
    public static boolean checkVerifyCode(HttpServletRequest request)
    {
        String verifyCodeExpect=( (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY)).toLowerCase();
        String verifyCodeActual=HttpServletRequestUtil.getString(request,"verifyCodeActual").toLowerCase();

        if (verifyCodeActual==null||!verifyCodeActual.equals(verifyCodeExpect))//当实际值为空或者实际值和期望值不同
            return  false;//返回错误
        return true;
    }



}
