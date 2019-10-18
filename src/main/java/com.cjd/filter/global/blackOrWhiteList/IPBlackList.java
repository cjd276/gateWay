package com.cjd.filter.global.blackOrWhiteList;

import com.cjd.dao.IPMapper;
import com.cjd.filter.IPListUtil;
import com.cjd.filter.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Set;

@Configuration
public class IPBlackList extends BaseIpResponse implements GlobalFilter, Ordered {
    private Logger logger = LoggerFactory.getLogger("IPBlackList");

    @Autowired
    private IPMapper ipMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("request in GlobalFilter of IPBlackList----order:"+getOrder());
        ServerHttpRequest request = exchange.getRequest();
        String ip = IPUtil.getIpAddress(request);
        Set<String> blacks = ipMapper.getIpBlackList();
        boolean isAllowed = IPListUtil.checkLoginIP(ip,blacks);

        if (isAllowed){
            return getResponse(exchange);
        }

        return chain.filter(exchange);
    }




    @Override
    public int getOrder() {
        return 3;
    }
}
