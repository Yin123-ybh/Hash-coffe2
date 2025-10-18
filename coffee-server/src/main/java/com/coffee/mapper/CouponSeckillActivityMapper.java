// 文件路径：coffee-server/src/main/java/com/coffee/mapper/CouponSeckillActivityMapper.java
package com.coffee.mapper;

import com.coffee.dto.CouponSeckillPageQueryDTO;
import com.coffee.entity.CouponSeckillActivity;
import com.coffee.vo.CouponSeckillActivityVO;
import com.coffee.vo.CouponSeckillParticipantVO;
import com.coffee.vo.CouponSeckillStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CouponSeckillActivityMapper {

    /**
     * 插入优惠券秒杀活动
     */
    void insert(CouponSeckillActivity activity);

    /**
     * 根据ID查询活动
     */
    CouponSeckillActivityVO selectById(@Param("id") Long id);

    /**
     * 查询进行中的活动列表
     */
    List<CouponSeckillActivityVO> selectActiveActivities(@Param("now") LocalDateTime now);

    /**
     * 更新活动状态
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 扣减秒杀库存
     */
    int decreaseSeckillStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 检查用户是否已参与
     */
    int checkUserParticipated(@Param("activityId") Long activityId, @Param("userId") Long userId);

    /**
     * 分页查询活动列表
     */
    List<CouponSeckillActivityVO> selectActivitiesPage(@Param("pageQuery") CouponSeckillPageQueryDTO pageQuery);

    /**
     * 统计活动总数
     */
    Long countActivities(@Param("pageQuery") CouponSeckillPageQueryDTO pageQuery);

    /**
     * 更新活动
     */
    void updateActivity(@Param("activity") CouponSeckillActivity activity);

    /**
     * 删除活动
     */
    void deleteActivity(@Param("id") Long id);

    /**
     * 获取活动统计信息
     */
    CouponSeckillStatsVO selectActivityStats(@Param("id") Long id);

    /**
     * 获取活动参与记录
     */
    List<CouponSeckillParticipantVO> selectParticipants(@Param("activityId") Long activityId, 
                                                         @Param("offset") Integer offset, 
                                                         @Param("pageSize") Integer pageSize);

    /**
     * 统计参与记录总数
     */
    Long countParticipants(@Param("activityId") Long activityId);
}