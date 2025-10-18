package com.coffee.service.impl;

import com.coffee.constant.StatusConstant;
import com.coffee.dto.CouponSeckillActivityDTO;
import com.coffee.dto.CouponSeckillPageQueryDTO;
import com.coffee.dto.CouponSeckillParticipateDTO;
import com.coffee.entity.CouponSeckillActivity;
import com.coffee.entity.UserCoupon;
import com.coffee.exception.BaseException;
import com.coffee.mapper.CouponSeckillActivityMapper;
import com.coffee.mapper.UserCouponMapper;
import com.coffee.result.PageResult;
import com.coffee.service.CouponSeckillActivityService;
import com.coffee.utils.CouponSeckillRedisUtil;
import com.coffee.vo.CouponSeckillActivityVO;
import com.coffee.vo.CouponSeckillParticipantVO;
import com.coffee.vo.CouponSeckillStatsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CouponSeckillActivityServiceImpl implements CouponSeckillActivityService {
    @Autowired
    private CouponSeckillActivityMapper couponSeckillActivityMapper;

    @Autowired
    private CouponSeckillRedisUtil couponSeckillRedisUtil;
    
    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    @Transactional
    public void createActivity(CouponSeckillActivityDTO activityDTO) {
        log.info("创建优惠券秒杀活动: {}", activityDTO);
        //参数校验
        if (activityDTO.getStartTime().isAfter(activityDTO.getEndTime())) {
            throw new IllegalArgumentException("开始时间必须早于结束时间");
        }

        if (activityDTO.getSeckillStock() <= 0) {
            throw new IllegalArgumentException("秒杀库存必须大于0");
        }

        if (activityDTO.getPerUserLimit() <= 0) {
            throw new IllegalArgumentException("每个用户限购数量必须大于0");
        }
        //创建活动
        CouponSeckillActivity activity = CouponSeckillActivity.builder()
                .name(activityDTO.getName())
                .description(activityDTO.getDescription())
                .couponId(activityDTO.getCouponId())
                .seckillStock(activityDTO.getSeckillStock())
                .perUserLimit(activityDTO.getPerUserLimit())
                .startTime(activityDTO.getStartTime())
                .endTime(activityDTO.getEndTime())
                .status(StatusConstant.COUPON_SECKILL_STATUS_NOT_STARTED)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        couponSeckillActivityMapper.insert(activity);
        log.info("优惠券秒杀活动创建成功，活动ID：{}", activity.getId());
    }

    @Override
    public List<CouponSeckillActivityVO> getActiveActivities() {
        log.info("获取进行中的优惠券秒杀活动列表");
        LocalDateTime now = LocalDateTime.now();
        return couponSeckillActivityMapper.selectActiveActivities(now);
    }

    @Override
    public CouponSeckillActivityVO getActivityById(Long id) {
        log.info("获取优惠券秒杀活动详情，活动ID：{}", id);
        CouponSeckillActivityVO activity = couponSeckillActivityMapper.selectById(id);
        if (activity == null) {
            throw new BaseException("优惠券秒杀活动不存在");
        }
        return activity;
    }

    @Override
    @Transactional
    public void participateSeckill(Long userId, CouponSeckillParticipateDTO participateDTO) {
        log.info("用户参与优惠券秒杀，用户ID：{}，活动ID：{}，数量：{}",
                userId, participateDTO.getActivityId(), participateDTO.getQuantity());

        // 1. 检查活动是否存在
        CouponSeckillActivityVO activity = couponSeckillActivityMapper.selectById(participateDTO.getActivityId());
        if (activity == null) {
            throw new BaseException("优惠券秒杀活动不存在");
        }

        // 2. 检查活动状态和时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getStartTime()) || now.isAfter(activity.getEndTime())) {
            throw new BaseException("优惠券秒杀活动不在进行中");
        }
        // 3. 使用Redis Lua脚本执行优惠券秒杀
        String stockKey = "coupon_seckill:stock:" + participateDTO.getActivityId();
        String participantsKey = "coupon_seckill:participants:" + participateDTO.getActivityId();

        log.info("执行Lua脚本，stockKey: {}, participantsKey: {}, userId: {}, quantity: {}, perUserLimit: {}", 
                stockKey, participantsKey, userId, participateDTO.getQuantity(), activity.getPerUserLimit());

        List<Object> result = couponSeckillRedisUtil.executeCouponSeckillScript(
                stockKey,
                participantsKey,
                userId.toString(),
                participateDTO.getQuantity().toString(),
                activity.getPerUserLimit().toString()
        );

        log.info("Lua脚本返回结果: {}", result);

        // 4. 处理Lua脚本返回结果
        if (result == null || result.isEmpty()) {
            throw new BaseException("优惠券秒杀执行失败");
        }

        // Redis返回的是Long类型，需要转换
        Long successLong = (Long) result.get(0);
        Long errorCodeLong = (Long) result.get(1);
        
        int success = successLong.intValue();
        int errorCode = errorCodeLong.intValue();

        if (success != 1) {
            // 根据错误代码返回对应的错误消息
            String errorMessage;
            switch (errorCode) {
                case 1:
                    errorMessage = "用户已经参与过优惠券秒杀";
                    break;
                case 2:
                    errorMessage = "活动不存在";
                    break;
                case 3:
                    errorMessage = "秒杀库存不足";
                    break;
                case 4:
                    errorMessage = "超过限领数量";
                    break;
                default:
                    errorMessage = "优惠券秒杀失败";
            }
            throw new BaseException(errorMessage);
        }

        Long remainingStockLong = (Long) result.get(2);
        int remainingStock = remainingStockLong.intValue();
        
        // 5. 发放优惠券到用户
        try {
            // 发放指定数量的优惠券
            for (int i = 0; i < participateDTO.getQuantity(); i++) {
                UserCoupon userCoupon = UserCoupon.builder()
                        .userId(userId)
                        .couponId(activity.getCouponId())
                        .status(StatusConstant.USER_COUPON_STATUS_UNUSED)
                        .getTime(LocalDateTime.now())
                        .build();
                userCouponMapper.insert(userCoupon);
            }
            log.info("发放优惠券成功，用户ID：{}，优惠券ID：{}，数量：{}", 
                    userId, activity.getCouponId(), participateDTO.getQuantity());
        } catch (Exception e) {
            log.error("发放优惠券失败，用户ID：{}，优惠券ID：{}", userId, activity.getCouponId(), e);
            // 这里可以选择是否回滚Redis操作，暂时只记录日志
        }
        
        log.info("用户参与优惠券秒杀成功，用户ID：{}，活动ID：{}，剩余库存：{}",
                userId, participateDTO.getActivityId(), remainingStock);
    }

    /**
     * 预热优惠券秒杀库存到Redis
     */
    public void warmUpCouponSeckillStock(Long activityId) {
        CouponSeckillActivityVO activity = couponSeckillActivityMapper.selectById(activityId);
        if (activity != null) {
            couponSeckillRedisUtil.warmUpCouponSeckillStock(activityId, activity.getSeckillStock());
        }
    }
    @Override
    public void updateActivityStatus(Long id, Integer status) {
        log.info("更新优惠券秒杀活动状态，活动ID：{}，状态：{}", id, status);
        couponSeckillActivityMapper.updateStatus(id, status);
    }

    @Override
    public PageResult<CouponSeckillActivityVO> getActivitiesPage(CouponSeckillPageQueryDTO pageQueryDTO) {
        log.info("分页查询优惠券秒杀活动：{}", pageQueryDTO);
        
        // 查询总数
        Long total = couponSeckillActivityMapper.countActivities(pageQueryDTO);
        
        // 查询数据
        List<CouponSeckillActivityVO> records = couponSeckillActivityMapper.selectActivitiesPage(pageQueryDTO);
        
        return PageResult.of(total, records);
    }

    @Override
    @Transactional
    public void updateActivity(CouponSeckillActivityDTO activityDTO) {
        log.info("更新优惠券秒杀活动：{}", activityDTO);
        
        // 参数校验
        if (activityDTO.getId() == null) {
            throw new IllegalArgumentException("活动ID不能为空");
        }
        
        if (activityDTO.getStartTime().isAfter(activityDTO.getEndTime())) {
            throw new IllegalArgumentException("开始时间必须早于结束时间");
        }

        if (activityDTO.getSeckillStock() <= 0) {
            throw new IllegalArgumentException("秒杀库存必须大于0");
        }

        if (activityDTO.getPerUserLimit() <= 0) {
            throw new IllegalArgumentException("每个用户限购数量必须大于0");
        }
        
        // 检查活动是否存在
        CouponSeckillActivityVO existingActivity = couponSeckillActivityMapper.selectById(activityDTO.getId());
        if (existingActivity == null) {
            throw new BaseException("优惠券秒杀活动不存在");
        }
        
        // 构建更新对象
        CouponSeckillActivity activity = CouponSeckillActivity.builder()
                .id(activityDTO.getId())
                .name(activityDTO.getName())
                .description(activityDTO.getDescription())
                .couponId(activityDTO.getCouponId())
                .seckillStock(activityDTO.getSeckillStock())
                .perUserLimit(activityDTO.getPerUserLimit())
                .startTime(activityDTO.getStartTime())
                .endTime(activityDTO.getEndTime())
                .updateTime(LocalDateTime.now())
                .build();
        
        couponSeckillActivityMapper.updateActivity(activity);
        log.info("优惠券秒杀活动更新成功，活动ID：{}", activityDTO.getId());
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        log.info("删除优惠券秒杀活动，活动ID：{}", id);
        
        // 检查活动是否存在
        CouponSeckillActivityVO existingActivity = couponSeckillActivityMapper.selectById(id);
        if (existingActivity == null) {
            throw new BaseException("优惠券秒杀活动不存在");
        }
        
        // 检查是否有参与记录
        Long participantCount = couponSeckillActivityMapper.countParticipants(id);
        if (participantCount > 0) {
            throw new BaseException("该活动已有参与记录，无法删除");
        }
        
        couponSeckillActivityMapper.deleteActivity(id);
        log.info("优惠券秒杀活动删除成功，活动ID：{}", id);
    }

    @Override
    public CouponSeckillStatsVO getActivityStats(Long id) {
        log.info("获取活动统计信息，活动ID：{}", id);
        
        // 检查活动是否存在
        CouponSeckillActivityVO existingActivity = couponSeckillActivityMapper.selectById(id);
        if (existingActivity == null) {
            throw new BaseException("优惠券秒杀活动不存在");
        }
        
        CouponSeckillStatsVO stats = couponSeckillActivityMapper.selectActivityStats(id);
        if (stats == null) {
            // 如果没有参与记录，返回默认统计信息
            stats = CouponSeckillStatsVO.builder()
                    .activityId(id)
                    .activityName(existingActivity.getName())
                    .totalStock(existingActivity.getSeckillStock())
                    .remainingStock(existingActivity.getSeckillStock())
                    .participantCount(0)
                    .successCount(0)
                    .failCount(0)
                    .successRate("0%")
                    .build();
        }
        
        return stats;
    }

    @Override
    public PageResult<CouponSeckillParticipantVO> getParticipants(Long activityId, Integer page, Integer pageSize) {
        log.info("获取活动参与记录，活动ID：{}，页码：{}，页大小：{}", activityId, page, pageSize);
        
        // 检查活动是否存在
        CouponSeckillActivityVO existingActivity = couponSeckillActivityMapper.selectById(activityId);
        if (existingActivity == null) {
            throw new BaseException("优惠券秒杀活动不存在");
        }
        
        // 计算偏移量
        Integer offset = (page - 1) * pageSize;
        
        // 查询总数
        Long total = couponSeckillActivityMapper.countParticipants(activityId);
        
        // 查询数据
        List<CouponSeckillParticipantVO> records = couponSeckillActivityMapper.selectParticipants(activityId, offset, pageSize);
        
        return PageResult.of(total, records);
    }
}
