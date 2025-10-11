package com.coffee.controller.user;

import com.coffee.context.BaseContext;
import com.coffee.dto.AddressDTO;
import com.coffee.result.Result;
import com.coffee.service.AddressService;
import com.coffee.vo.AddressVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("userAddressController")
@RequestMapping("/address")
@Api(tags = "用户地址相关接口")
@Slf4j
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/list")
    @ApiOperation("查询用户地址列表")
    public Result<List<AddressVO>> getList() {
        log.info("查询用户地址列表");
        Long userId = BaseContext.getCurrentId();
        List<AddressVO> list = addressService.getList(userId);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询地址详情")
    public Result<AddressVO> getById(@PathVariable Long id) {
        log.info("查询地址详情：{}", id);
        AddressVO addressVO = addressService.getById(id);
        return Result.success(addressVO);
    }

    @PostMapping
    @ApiOperation("新增地址")
    public Result<Void> add(@Valid @RequestBody AddressDTO addressDTO) {
        log.info("新增地址：{}", addressDTO);
        addressService.add(addressDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改地址")
    public Result<Void> update(@Valid @RequestBody AddressDTO addressDTO) {
        log.info("修改地址：{}", addressDTO);
        addressService.update(addressDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除地址")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("删除地址：{}", id);
        addressService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/default")
    @ApiOperation("设置默认地址")
    public Result<Void> setDefault(@PathVariable Long id) {
        log.info("设置默认地址：{}", id);
        Long userId = BaseContext.getCurrentId();
        addressService.setDefault(userId, id);
        return Result.success();
    }

    @GetMapping("/default")
    @ApiOperation("查询用户默认地址")
    public Result<AddressVO> getDefault() {
        log.info("查询用户默认地址");
        Long userId = BaseContext.getCurrentId();
        AddressVO addressVO = addressService.getDefault(userId);
        return Result.success(addressVO);
    }
}
