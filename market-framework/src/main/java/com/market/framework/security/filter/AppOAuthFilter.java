//package com.market.framework.security.filter;
//
//import com.alibaba.fastjson2.JSONObject;
//import com.market.common.utils.ServletUtils;
//import com.market.common.utils.StringUtils;
//import com.market.common.utils.sign.Md5Utils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 拦截app发送的请求
// * @author ph
// * @version 1.0
// * @date 2023-05-18 14:47
// */
//@Component
//@Slf4j
//public class AppOAuthFilter extends OncePerRequestFilter {
//
//    private static final String URL_PREFIX = "/app";
//
//    private static final String KEY = "iml7fpyktj6tmtru2q4u6qzvyuezouqcaznyirq53f5s2rpf5dreg4n8zaedrdyz";
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String url = request.getRequestURL().toString();
//        //不含app的url直接放行
//        if(url.indexOf(URL_PREFIX) == -1){
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = request.getHeader("token");
//        if(StringUtils.isEmpty(token)){
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String body = ServletUtils.getRequestBody(request);
//        log.info("APP调用接口：url = " + url);
//        log.info("APP调用接口参数：body = " + body);
//        if(StringUtils.isNotEmpty(body)){
//            JSONObject jsonObject = JSONObject.parseObject(body);
//            String time = jsonObject.getString("time");
//            String phoneNumber = jsonObject.getString("phoneNumber");
//            String code = Md5Utils.hash(phoneNumber + KEY + time);
//            if(!StringUtils.equals(code, token)){
//
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//
//}
