package com.cjd.filter.global.request;

import com.cjd.filter.ModifyRequestParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class ModifyRequestParamsGlobalFilterFactory implements GlobalFilter, Ordered {

    private Logger logger = LoggerFactory.getLogger("ModifyRequestParamsGlobalFilterFactory");

    @Autowired
    ModifyRequestParams modifyRequestParams;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        logger.info("request in GlobalFilter of ModifyRequestParamsGlobalFilterFactory---order:" + getOrder());
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

    }




    @Override
    public int getOrder() {
        return 8;
    }
}
