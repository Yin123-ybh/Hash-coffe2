package com.coffee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 优惠券秒杀活动统计VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSeckillStatsVO implements Serializable {
    private Long activityId;            // 活动ID
    private String activityName;       // 活动名称
    private Integer totalStock;         // 总库存
    private Integer remainingStock;     // 剩余库存
    private Integer participantCount;   // 参与人数
    private Integer successCount;      // 成功领取数量
    private Integer failCount;         // 失败数量
    private String successRate;        // 成功率
}

