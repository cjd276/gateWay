package com.cjd.loadbalancer;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalRibbonClientConfiguration {



    @Bean
    public IPing ribbonPing(){
        return new PingUrl();
    }



    @Bean
    public IRule ribbonRule(){
        return new AviliableResponseBalancer();
    }





}
