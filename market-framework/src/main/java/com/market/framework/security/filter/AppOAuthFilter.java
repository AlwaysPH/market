package com.market.framework.security.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.market.common.constant.HttpStatus;
import com.market.common.core.domain.AjaxResult;
import com.market.common.utils.ServletUtils;
import com.market.common.utils.StringUtils;
import com.market.common.utils.sign.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * 拦截app发送的请求
 * @author ph
 * @version 1.0
 * @date 2023-05-18 14:47
 */
@Component
@Slf4j
public class AppOAuthFilter extends OncePerRequestFilter {

    private static final List<String> URL_PREFIX = Arrays.asList("/app/activity/receiveCoupon", "/app/coupon/getUserCouponList",
                                                    "/app/coupon/useCoupon", "/app/coupon/getPayCouponList");

    private static final String KEY = "iml7fpyktj6tmtru2q4u6qzvyuezouqcaznyirq53f5s2rpf5dreg4n8zaedrdyz";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        //不含app的url直接放行
        Boolean flag = false;
        for(String i : URL_PREFIX){
            if(StringUtils.equals(url, i)){
                flag = true;
                break;
            }
        }
        if(!flag){
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("token");
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        if(StringUtils.isEmpty(token)){
            ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
            return;
        }
        String body = ServletUtils.getRequestBody(request);
        log.info("APP调用接口：url = " + url);
        log.info("APP调用接口参数：body = " + body);
        if(StringUtils.isNotEmpty(body)){
            JSONObject jsonObject = JSONObject.parseObject(body);
            String time = jsonObject.getString("time");
            String phoneNumber = jsonObject.getString("phoneNumber");
            String authCode = Md5Utils.hash(phoneNumber + KEY + time);
            if(!StringUtils.equals(authCode, token)){
                ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
