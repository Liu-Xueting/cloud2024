package com.atguigu.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther zzyy
 * @create 2023-05-30 16:13
 */
@RestController
@Slf4j
public class RateLimitController
{
    @GetMapping("/rateLimit/byUrl")
    public String byUrl()
    {
        return "按rest地址限流测试OK";
    }

    // SentinelResource指定组件进行 流量控制 服务降级等功能  并自定义违反限流规则处理
    // blockHandler 方法参数返回值和原方法一致
    @GetMapping("/rateLimit/byResource")
    @SentinelResource(value = "byResourceSentinelResource",blockHandler = "handleException")
    public String byResource()
    {
        return "按资源名称SentinelResource限流测试OK";
    }
    public String handleException(BlockException exception)
    {
        return "服务不可用@SentinelResource启动"+"\t"+"o(╥﹏╥)o";
    }


    @GetMapping("/rateLimit/doAction/{p1}")
    @SentinelResource(value = "doActionSentinelResource",
            blockHandler = "doActionBlockHandler", fallback = "doActionFallback")
    public String doAction(@PathVariable("p1") Integer p1) {
        if (p1 == 0){
            throw new RuntimeException("p1等于零直接异常");
        }
        return "doAction";
    }
    // 违反 sentinel 限流规则  对sentinel配置后出现的违规情况进行处理
    public String doActionBlockHandler(@PathVariable("p1") Integer p1,BlockException e) {
        log.error("sentinel配置自定义限流了:{}", e);
        return "sentinel配置自定义限流了";
    }
    // 出现异常 兜底方法(自定义服务降级处理)   程序异常了JVM抛出的异常服务降级
    // ？？ 如果sentinel配置的熔断发生了 调用哪个
    public String doActionFallback(@PathVariable("p1") Integer p1,Throwable e){
        log.error("程序逻辑异常了:{}", e);
        return "程序逻辑异常了"+"\t"+e.getMessage();
    }

    /**
     * 热点参数限流
     * @param p1
     * @param p2
     * @return
     */
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "dealHandler_testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2){
        return "------testHotKey";
    }
    public String dealHandler_testHotKey(String p1,String p2,BlockException exception)
    {
        return "-----dealHandler_testHotKey";
    }


}

