package com.coffee.mapper;

import com.coffee.entity.Address;
import com.coffee.vo.AddressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {

    /**
     * 根据用户ID查询地址列表
     * @param userId 用户ID
     * @return 地址列表
     */
    List<AddressVO> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据ID查询地址详情
     * @param id 地址ID
     * @return 地址详情
     */
    AddressVO selectById(@Param("id") Long id);

    /**
     * 插入地址
     * @param address 地址信息
     */
    void insert(Address address);

    /**
     * 更新地址
     * @param address 地址信息
     */
    void update(Address address);

    /**
     * 根据ID删除地址
     * @param id 地址ID
     */
    void deleteById(@Param("id") Long id);

    /**
     * 设置默认地址
     * @param userId 用户ID
     * @param id 地址ID
     */
    void setDefault(@Param("userId") Long userId, @Param("id") Long id);

    /**
     * 取消用户所有地址的默认状态
     * @param userId 用户ID
     */
    void cancelAllDefault(@Param("userId") Long userId);

    /**
     * 查询用户默认地址
     * @param userId 用户ID
     * @return 默认地址
     */
    AddressVO selectDefaultByUserId(@Param("userId") Long userId);
}
