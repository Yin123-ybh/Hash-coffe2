package com.coffee.service.impl;

import com.coffee.dto.ProductDTO;
import com.coffee.dto.ProductPageQueryDTO;
import com.coffee.entity.Product;
import com.coffee.mapper.ProductMapper;
import com.coffee.result.PageResult;
import com.coffee.service.ProductService;
import com.coffee.vo.ProductVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    /**
     * 分页查询商品
     */
    @Transactional
    @Override
    public PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO) {
        log.info("分页查询商品，参数：{}", productPageQueryDTO);
        //设置分页参数
        PageHelper.startPage(productPageQueryDTO.getPage(), productPageQueryDTO.getPageSize());
        //执行查询
        Page<Product> page =(Page<Product>) productMapper.pageQuery(productPageQueryDTO);
        //提取查询结果
        long total = page.getTotal();
        List<Product> records = page.getResult();
        // 转换为VO
        List<ProductVO> productVOList = new ArrayList<>();
        for (Product product : records) {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);

            // 设置状态文本
            productVO.setStatusText(product.getStatus() == 1 ? "上架" : "下架");

            productVOList.add(productVO);
        }

        return new PageResult(total, productVOList);
    }
    /**
     * 根据ID查询商品
     */
    @Transactional
    @Override
    public ProductVO getById(Long id) {
        log.info("根据ID查询商品，参数：{}", id);
        Product product = productMapper.getById(id);
        if(product==null){
            log.error("根据ID查询商品，商品不存在，参数：{}", id);
            throw new RuntimeException("商品不存在");
        }
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        productVO.setStatusText(product.getStatus() == 1 ? "上架" : "下架");
        return productVO;
    }
    /**
     * 新增商品
     */
    @Transactional
    @Override
    public ProductVO save(ProductDTO productDTO) {
        log.info("新增商品，参数：{}", productDTO);
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        //设置默认值
        product.setStatus(1);//默认上架
        product.setSales(0);//默认销量为0
        product.setCreateTime(LocalDateTime.now());//默认当前时间
        product.setUpdateTime(LocalDateTime.now());//默认当前时间

        //插入数据库
        productMapper.insert(product);
        //转换为vo返回
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        productVO.setStatusText("上架");
        return productVO;
    }
    /**
     * 更新商品
     */
    @Transactional
    @Override
    public ProductVO update(ProductDTO productDTO) {
        log.info("更新商品，参数：{}", productDTO);
        //检查商品是否存在
        Product existProduct = productMapper.getById(productDTO.getId());
        if(existProduct == null){
            throw new RuntimeException("商品不存在");
        }
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setUpdateTime(LocalDateTime.now());
        //更新数据库
        productMapper.update(product);
        //查询更新后的数据
        Product updatedProduct = productMapper.getById(product.getId());
        //转换为vo返回
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        productVO.setStatusText(updatedProduct.getStatus() == 1 ? "上架" : "下架");
        return productVO;
    }
    /**
     * 删除商品
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("删除商品，参数：{}", id);
        //检查商品是否存在
        Product product = productMapper.getById(id);
        if(product==null){
            log.error("删除商品，商品不存在，参数：{}", id);
            throw new RuntimeException("商品不存在");
        }
        //添加业务检查规则

        productMapper.deleteById(id);

    }
    /**
     * 批量删除商品
     */
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        log.info("批量删除商品，参数：{}", ids);
        //检查商品是否存在
        for (Long id : ids) {
            Product product = productMapper.getById(id);
            if(product==null){
                log.error("批量删除商品，商品不存在，参数：{}", id);
                throw new RuntimeException("商品不存在");
            }
        }
        //添加业务检查规则

        productMapper.deleteByIds(ids);

    }
    /**
     * 启用禁用商品
     */
    @Transactional
    @Override
    public void startOrStop(Integer status, Long id) {
        log.info("启用禁用商品，参数：{}，{}", status, id);
        //检查商品是否存在
        Product product = productMapper.getById(id);
        if(product==null){
            log.error("启用禁用商品，商品不存在，参数：{}，{}", status, id);
            throw new RuntimeException("商品不存在");
        }

        productMapper.updateStatus(status, id);

    }
    /**
     * 根据分类ID查询商品列表
     */
    @Transactional
    @Override
    public List<ProductVO> listByCategoryId(Long categoryId) {
        log.info("根据分类ID查询商品列表，参数：{}", categoryId);

        ProductPageQueryDTO queryDTO = new ProductPageQueryDTO();
        queryDTO.setCategoryId(categoryId);
        queryDTO.setStatus(1);//默认查询上架商品

        List<Product> products = productMapper.pageQuery(queryDTO);

        List<ProductVO> productVOList = new ArrayList<>();
        for (Product product : products) {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);
            productVO.setStatusText( "上架" );
            productVOList.add(productVO);
        }
        return productVOList;
    }
}
