package com.cjd.loadbalancer;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClient( configuration = { FdlRibbonClientConfiguration.class },name = "fdl")
public class FdlRibbonClient {
}
