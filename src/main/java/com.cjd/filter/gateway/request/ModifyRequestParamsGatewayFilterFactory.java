package com.cjd.filter.gateway.request;


import com.cjd.filter.ModifyRequestParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class ModifyRequestParamsGatewayFilterFactory extends AbstractGatewayFilterFactory {

    private Logger logger = LoggerFactory.getLogger("ModifyRequestParamsGatewayFilterFactory");

    @Autowired
    ModifyRequestParams modifyRequestParams;


     @Override
     public GatewayFilter apply(Object config) {
         return (exchange,chain)->{
             logger.info("request in GatewayFilter of ModifyRequestParamsGatewayFilterFactory");
             ServerHttpRequest serverHttpRequest = exchange.getRequest();
             HttpMethod method = serverHttpRequest.getMethod();
             String contentType = serverHttpRequest.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
             //post请求时，如果是文件上传之类的请求，不修改请求消息体
             if (method == HttpMethod.POST && (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(contentType)
                     || MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType))) {
                 return modifyRequestParams.reBuildPostRequest(exchange,chain);
             }else if (method == HttpMethod.GET) {
                 return modifyRequestParams.reBuildGetParams(exchange,chain);
             }
             return chain.filter(exchange);


         };
     }




}
