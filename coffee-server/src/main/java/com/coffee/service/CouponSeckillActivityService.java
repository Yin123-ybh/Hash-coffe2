// 文件路径：coffee-server/src/main/java/com/coffee/service/CouponSeckillActivityService.java
package com.coffee.service;

import com.coffee.dto.CouponSeckillActivityDTO;
import com.coffee.dto.CouponSeckillPageQueryDTO;
import com.coffee.dto.CouponSeckillParticipateDTO;
import com.coffee.result.PageResult;
import com.coffee.vo.CouponSeckillActivityVO;
import com.coffee.vo.CouponSeckillParticipantVO;
import com.coffee.vo.CouponSeckillStatsVO;
import java.util.List;

public interface CouponSeckillActivityService {

    /**
     * 创建优惠券秒杀活动
     */
    void createActivity(CouponSeckillActivityDTO activityDTO);

    /**
     * 获取进行中的活动列表
     */
    List<CouponSeckillActivityVO> getActiveActivities();

    /**
     * 根据ID获取活动详情
     */
    CouponSeckillActivityVO getActivityById(Long id);

    /**
     * 参与优惠券秒杀
     */
    void participateSeckill(Long userId, CouponSeckillParticipateDTO participateDTO);

    /**
     * 更新活动状态
     */
    void updateActivityStatus(Long id, Integer status);

    /**
     * 分页查询活动列表
     */
    PageResult<CouponSeckillActivityVO> getActivitiesPage(CouponSeckillPageQueryDTO pageQueryDTO);

    /**
     * 更新活动
     */
    void updateActivity(CouponSeckillActivityDTO activityDTO);

    /**
     * 删除活动
     */
    void deleteActivity(Long id);

    /**
     * 获取活动统计信息
     */
    CouponSeckillStatsVO getActivityStats(Long id);

    /**
     * 获取活动参与记录
     */
    PageResult<CouponSeckillParticipantVO> getParticipants(Long activityId, Integer page, Integer pageSize);

    void warmUpCouponSeckillStock(Long id);
}