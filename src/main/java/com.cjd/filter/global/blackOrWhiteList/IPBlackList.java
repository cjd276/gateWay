package com.cjd.filter.global.blackOrWhiteList;

import com.cjd.filter.IPWhiteListUtil;
import com.cjd.filter.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class IPBlackList implements GlobalFilter, Ordered {
    private Logger logger = LoggerFactory.getLogger("IPBlackList");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("request in GlobalFilter of IPBlackList----order:"+getOrder());
        ServerHttpRequest request = exchange.getRequest();
        String ip = IPUtil.getIpAddress(request);

        boolean isAllowed = IPWhiteListUtil.checkLoginIP(ip,"127.0.0.2");

        if (isAllowed){
            ServerHttpResponse response = exchange.getResponse();
            Map<String,Object> res = new HashMap<>();
            res.put("code",401);
            res.put("msg","禁止访问");

            DataBuffer buffer = response.bufferFactory().wrap(res.toString().getBytes());
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }

        return chain.filter(exchange);
    }




    @Override
    public int getOrder() {
        return 3;
    }
}
