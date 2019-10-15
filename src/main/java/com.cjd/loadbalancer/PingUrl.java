package com.cjd.loadbalancer;

import com.cjd.filter.global.jwtAuth.AuthFilter;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PingUrl implements IPing {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingUrl.class);
    public boolean isAlive(Server server) {

        String urlStr   = "http://" + server.getId();


        boolean isAlive = false;


        HttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest getRequest = new HttpGet(urlStr);
        String content=null;
        try {
            HttpResponse response = httpClient.execute(getRequest);
            //content = EntityUtils.toString(response.getEntity());
            //isAlive = (response.getStatusLine().getStatusCode() == 200); // 根据状态码和返回的内容来判定服务实例是否有效
            //System.out.println(response.getStatusLine().getStatusCode()+"------------------");
            isAlive = true;
        } catch (IOException e) {
            LOGGER.info("服务:{}不可用",server.getId());
        }finally{
            // Release the connection.
            getRequest.abort();
        }
        server.setAlive(isAlive);
        return isAlive;
    }

}