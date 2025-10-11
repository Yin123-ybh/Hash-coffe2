package com.coffee.service.impl;

import com.coffee.context.BaseContext;
import com.coffee.dto.AddressDTO;
import com.coffee.entity.Address;
import com.coffee.mapper.AddressMapper;
import com.coffee.service.AddressService;
import com.coffee.vo.AddressVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<AddressVO> getList(Long userId) {
        log.info("查询用户地址列表，用户ID：{}", userId);
        return addressMapper.selectByUserId(userId);
    }

    @Override
    public AddressVO getById(Long id) {
        log.info("查询地址详情，地址ID：{}", id);
        return addressMapper.selectById(id);
    }

    @Override
    @Transactional
    public void add(AddressDTO addressDTO) {
        log.info("新增地址：{}", addressDTO);
        
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        
        // 设置用户ID
        address.setUserId(BaseContext.getCurrentId());
        
        // 设置创建时间和更新时间
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        
        // 如果是默认地址，先取消其他地址的默认状态
        if (addressDTO.getIsDefault() != null && addressDTO.getIsDefault() == 1) {
            addressMapper.cancelAllDefault(address.getUserId());
        }
        
        addressMapper.insert(address);
    }

    @Override
    @Transactional
    public void update(AddressDTO addressDTO) {
        log.info("修改地址：{}", addressDTO);
        
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        
        // 设置更新时间
        address.setUpdateTime(LocalDateTime.now());
        
        // 如果是默认地址，先取消其他地址的默认状态
        if (addressDTO.getIsDefault() != null && addressDTO.getIsDefault() == 1) {
            addressMapper.cancelAllDefault(BaseContext.getCurrentId());
        }
        
        addressMapper.update(address);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("删除地址，地址ID：{}", id);
        addressMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void setDefault(Long userId, Long id) {
        log.info("设置默认地址，用户ID：{}，地址ID：{}", userId, id);
        
        // 先取消所有地址的默认状态
        addressMapper.cancelAllDefault(userId);
        
        // 设置指定地址为默认
        addressMapper.setDefault(userId, id);
    }

    @Override
    public AddressVO getDefault(Long userId) {
        log.info("查询用户默认地址，用户ID：{}", userId);
        return addressMapper.selectDefaultByUserId(userId);
    }
}
