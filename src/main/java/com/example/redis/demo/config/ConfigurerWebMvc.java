package com.example.redis.demo.config;

import com.alibaba.fastjson.JSON;
import com.example.redis.demo.errors.Result;
import com.example.redis.demo.errors.ResultCode;
import com.example.redis.demo.errors.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Spring MVC 配置
 */
@Configuration
@EnableWebMvc
public class ConfigurerWebMvc implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(ConfigurerWebMvc.class);


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    // 统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add((request, response, handler, e) -> {
            Result result = new Result();
            if (e instanceof ServiceException) {
                ServiceException serviceException = (ServiceException) e;
                result.setCode(serviceException.getCode()).setMessage(serviceException.getMessage());
                logger.error(serviceException.getMessage(), e);
            } else if (e instanceof HttpMessageNotReadableException) {
                //处理入参json格式不对导致的转换异常
                result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 参数格式错误");
            } else if (e instanceof NoHandlerFoundException) {
                result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
            } else if (e instanceof ServletException) {
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
            } else {
                result.setCode(ResultCode.INTERNAL_SERVER_ERROR)
                        .setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员");
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s", request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(), handlerMethod.getMethod().getName(),
                            e.getMessage());
                } else {
                    message = e.getMessage();
                }
                logger.error(message, e);
            }
            responseResult(response, result);
            return new ModelAndView();
        });
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

}