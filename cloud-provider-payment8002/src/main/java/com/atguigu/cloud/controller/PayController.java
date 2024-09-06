package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "支付微服务模块",description = "支付CRUD")
public class PayController {

    @Resource
    private PayService payService;

    @Operation(summary = "新增",description = "新增支付流水方法,json串做参数")
    @PostMapping(value = "/pay/add")
    public ResultData<String> addPay(@RequestBody Pay pay){
        System.out.println(pay.toString());
        int add = payService.add(pay);
        return ResultData.success("成功插入记录，返回值："+add);
    }

    @Operation(summary = "删除",description = "删除支付流水方法")
    @DeleteMapping(value = "/pay/del/{id}")
    public ResultData<Integer> detetePay(@PathVariable("id") Integer id){
        int delete = payService.delete(id);
        return ResultData.success(delete);
    }

    @Operation(summary = "修改",description = "修改支付流水方法")
    @PutMapping(value = "/pay/update")
    public ResultData<String> updatePay(@RequestBody PayDTO payDTO){
        Pay pay = new Pay();
        // 把 payDTO 的数据 赋值到 pay 上
        BeanUtils.copyProperties(payDTO,pay);
        int update = payService.update(pay);
        return ResultData.success("成功修改记录，返回值："+update);

    }

    @Operation(summary = "按照ID查流水",description = "查询支付流水方法")
    @GetMapping(value = "/pay/get/{id}")
    public ResultData<Pay> getPay(@PathVariable("id") Integer id){
        Pay byId = payService.getById(id);
        return ResultData.success(byId);
    }

    @Operation(summary = "查找全部流水",description = "查询全部支付流水方法")
    @GetMapping(value = "/pay/getAll")
    public List<Pay> getAll(){
        return payService.getAll();
    }


    // 测试获取consul配置信息
    @Value("${server.port}")
    private String port;

    @GetMapping(value = "/pay/get/info")
    public String getInfoByConsul(@Value("${atguigu.info}") String atguiguInfo){
        return "atguiguInfo:"+atguiguInfo+"\t"+port;
    }
}
