package com.cjd.loadbalancer;


import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClients( defaultConfiguration = { GlobalRibbonClientConfiguration.class } )
public class GlobalRibbonClient {
}
