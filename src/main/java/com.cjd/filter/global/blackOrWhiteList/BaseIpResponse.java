package com.cjd.filter.global.blackOrWhiteList;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class BaseIpResponse {



    Mono<Void> getResponse(ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();
        Map<String,Object> res = new HashMap<>();
        res.put("code",401);
        res.put("msg","禁止ip访问");
        DataBuffer buffer = response.bufferFactory().wrap(res.toString().getBytes());
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }





}
