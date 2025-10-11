package com.coffee.service;

import com.coffee.dto.AddressDTO;
import com.coffee.vo.AddressVO;

import java.util.List;

public interface AddressService {

    /**
     * 查询用户地址列表
     * @param userId 用户ID
     * @return 地址列表
     */
    List<AddressVO> getList(Long userId);

    /**
     * 根据ID查询地址详情
     * @param id 地址ID
     * @return 地址详情
     */
    AddressVO getById(Long id);

    /**
     * 新增地址
     * @param addressDTO 地址信息
     */
    void add(AddressDTO addressDTO);

    /**
     * 修改地址
     * @param addressDTO 地址信息
     */
    void update(AddressDTO addressDTO);

    /**
     * 删除地址
     * @param id 地址ID
     */
    void delete(Long id);

    /**
     * 设置默认地址
     * @param userId 用户ID
     * @param id 地址ID
     */
    void setDefault(Long userId, Long id);

    /**
     * 查询用户默认地址
     * @param userId 用户ID
     * @return 默认地址
     */
    AddressVO getDefault(Long userId);
}
