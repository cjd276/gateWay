package com.cjd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;

/**
 * 1、初始化加载动态路由规则
 * 2、组装路由bean
 * 3、加载路由bean
 */
@Service
public class InitRouteService {

    @Autowired
    private DynamicRouteService dynamicRouteService;

    @PostConstruct
    void Init(){
        dynamicRouteService.addAll(getRouteDefinitions());
    }


    List<RouteDefinition>   getRouteDefinitions(){

        List<RouteDefinition>  routeDefinitions = new ArrayList<>();

        RouteDefinition definition = new RouteDefinition();
        URI uri = UriComponentsBuilder.fromUriString("lb://fdl").build().toUri();
        definition.setId("fldRoute");
        definition.setUri(uri);
        definition.setOrder(0);

        //PathRoutePredicateFactory
        PredicateDefinition pathPredicate = new PredicateDefinition();
        pathPredicate.setName("Path");
        pathPredicate.addArg("pattern", "/service/**");

        //HostRoutePredicateFactory
        PredicateDefinition hostPredicate = new PredicateDefinition();
        hostPredicate.setName("Host");
        hostPredicate.addArg("pattern", "127.0.0.1");
        //AfterRoutePredicateFactory
        PredicateDefinition afterPredicate = new PredicateDefinition();
        afterPredicate.setName("After");
        afterPredicate.addArg("datetime", "2017-01-20T17:42:47.789-07:00[America/Denver]");
        //BeforeRoutePredicateFactory
        PredicateDefinition beforePredicate = new PredicateDefinition();
        beforePredicate.setName("Before");
        beforePredicate.addArg("datetime", "2017-01-20T17:42:47.789-07:00[America/Denver]");
        //BetweenRoutePredicateFactory
        PredicateDefinition betweenPredicate = new PredicateDefinition();
        betweenPredicate.setName("Between");
        betweenPredicate.addArg("datetime1", "2017-01-20T17:42:47.789-07:00[America/Denver]");
        betweenPredicate.addArg("datetime2", "2017-01-20T17:42:47.789-07:00[America/Denver]");
        //CookieRoutePredicateFactory
        PredicateDefinition cookiePredicate = new PredicateDefinition();
        cookiePredicate.setName("Cookie");
        cookiePredicate.addArg("name", "chocolate");
        cookiePredicate.addArg("regexp", "ch.p");
        //HeaderRoutePredicateFactory
        PredicateDefinition headerPredicate = new PredicateDefinition();
        headerPredicate.setName("Header");
        headerPredicate.addArg("header", "X-Request-Id");
        headerPredicate.addArg("regexp", "\\d+");
        //MethodRoutePredicateFactory
        PredicateDefinition methodPredicate = new PredicateDefinition();
        methodPredicate.setName("Method");
        methodPredicate.addArg("method", "get");
        //QueryRoutePredicateFactory
        PredicateDefinition queryPredicate = new PredicateDefinition();
        queryPredicate.setName("Query");
        queryPredicate.addArg("param", "foo");
        queryPredicate.addArg("regexp", "bar");
        //RemoteAddrRoutePredicateFactory
        PredicateDefinition remoteAddrPredicate = new PredicateDefinition();
        queryPredicate.setName("RemoteAddr");
        queryPredicate.addArg("source", "192.168.1.1/24");

        //WeightRoutePredicateFactory
        PredicateDefinition weightPredicate = new PredicateDefinition();
        weightPredicate.setName("Weight");
        weightPredicate.addArg("group", "group1");
        weightPredicate.addArg("routeId", "2");


        definition.setPredicates(Arrays.asList(pathPredicate,hostPredicate));






        //定义Filter
        //StripPrefixGatewayFilterFactory
        FilterDefinition stripPrefixFilter = new FilterDefinition();
        stripPrefixFilter.setName("StripPrefix");
        stripPrefixFilter.addArg("parts","1");


        //HystrixGatewayFilterFactory
        FilterDefinition hystrixFilter = new FilterDefinition();
        hystrixFilter.setName("Hystrix");
        hystrixFilter.addArg("name","fallbackcmd");
        hystrixFilter.addArg("fallbackUri","forward:/fallback?a=128");

        //RequestRateLimiterGatewayFilterFactory

        FilterDefinition requestRateLimiterFilter = new FilterDefinition();
        requestRateLimiterFilter.setName("RequestRateLimiter");
        requestRateLimiterFilter.addArg("keyResolver","#{@ipAddressKeyResolver}");
        requestRateLimiterFilter.addArg("redis-rate-limiter.replenishRate","1");
        requestRateLimiterFilter.addArg("redis-rate-limiter.burstCapacity","1");

        //ModifyRequestParamsGatewayFilterFactory
        FilterDefinition modifyRequestParamsFilter = new FilterDefinition();
        modifyRequestParamsFilter.setName("ModifyRequestParams");









        definition.setFilters(Arrays.asList(stripPrefixFilter, hystrixFilter,modifyRequestParamsFilter,requestRateLimiterFilter));


        routeDefinitions.add(definition);


        return routeDefinitions;
    }








}
