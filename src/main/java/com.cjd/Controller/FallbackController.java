package com.cjd.Controller;

import com.google.gson.Gson;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    private Gson gson = new Gson();
//    @PostMapping("/fallback")
//    public Map<String,String> hystrixPost(HttpRequest request){
//        URI uri = request.getURI();
//
//        Map<String,String> map = new HashMap<>();
//        map.put("hystrix","使用端路由器");
//        return map;
//    }
    @RequestMapping("/fallback")
    public Map<String,String> hystrixGet(/*@RequestParam(value = "request") String request,@RequestBody Map data*/){
        //URI uri = request.getURI();

        Map<String,String> map = new HashMap<>();
        map.put("hystrix","使用端路由器");
//        map.put("request get",request);
//        map.put("request post",gson.toJson(data));
        return map;
    }

}
