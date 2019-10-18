package com.cjd.filter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

@Component
public class ModifyRequestParams {

    private Logger logger = LoggerFactory.getLogger("ModifyRequestParams");

    private Gson gson = new Gson();


    /**
     * 修改报文并重新构建请求状态及所需耗时
     * @param chain
     * @param exchange
     * @return
     */
    public Mono<Void> returnMono(GatewayFilterChain chain, ServerWebExchange exchange){
        return chain.filter(exchange).then(Mono.fromRunnable(()->{
            Long startTime = exchange.getAttribute("startTime");
            if (startTime != null){
                long executeTime = (System.currentTimeMillis() - startTime);
                logger.info("耗时：{}ms" , executeTime);
                logger.info("状态码：{}" , Objects.requireNonNull(exchange.getResponse().getStatusCode()).value());
            }
        }));
    }

    /**
     * 修改参数后需要重新设置http 请求头的报文长度
     * @param exchange
     * @param headers
     * @param outputMessage
     * @return
     */
    public ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, MyCachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0L) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set("Transfer-Encoding", "chunked");
                }
                return httpHeaders;
            }
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    /**
     * 修改get 方式请求下的参数，并重新构建请求
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> reBuildGetParams(ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        // 获取原参数
        URI uri = serverHttpRequest.getURI();
        StringBuilder query = new StringBuilder();
        String originalQuery = uri.getRawQuery();
        if (org.springframework.util.StringUtils.hasText(originalQuery)) {
            query.append(originalQuery);
            if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                query.append('&');
            }
        }
        // 添加查询参数
        query.append("addGetParams=addGetValue");

        // 替换查询参数
        URI newUri = UriComponentsBuilder.fromUri(uri)
                .replaceQuery(query.toString())
                .build(true)
                .toUri();

        ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri).build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    /**
     * 重新组装post方式请求报文的body
     * @param exchange
     * @return
     */
    private Mono<String> reBuildPostBody(ServerWebExchange exchange){
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        //重点
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
            //因为约定了终端传参的格式，所以只考虑json的情况，如果是表单传参，请自行发挥
            if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType) || MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(mediaType)) {
                String newBody;
                JsonParser parse = new JsonParser();
                JsonObject json = parse.parse(body).getAsJsonObject();//创建jsonObject对象
                json.addProperty("addParamsGlobal","addGlobalValue");
                // 转换回字符串
                newBody = gson.toJson(json);

                return Mono.just(newBody);
            }
            return Mono.empty();
        });
        return modifiedBody;
    }

    /**
     * 重新构建post 方式的参数并重新构建post请求
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> reBuildPostRequest(ServerWebExchange exchange, GatewayFilterChain chain){
        Mono<String> modifiedBody = reBuildPostBody(exchange);
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        //猜测这个就是之前报400错误的元凶，之前修改了body但是没有重新写content length
        headers.remove("Content-Length");
        //MyCachedBodyOutputMessage 这个类完全就是CachedBodyOutputMessage，只不过CachedBodyOutputMessage不是公共的
        MyCachedBodyOutputMessage outputMessage = new MyCachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
            return returnMono(chain, exchange.mutate().request(decorator).build());
        }));
    }
}
