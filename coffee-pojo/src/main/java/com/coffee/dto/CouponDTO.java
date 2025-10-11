package com.coffee.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券数据传输对象
 * 用于接收前端传来的优惠券数据
 */
@Data
public class CouponDTO {

    /**
     * 优惠券ID（编辑时需要）
     */
    private Long id;

    /**
     * 优惠券名称
     * @NotNull: 不能为空
     * @NotBlank: 不能为空字符串
     * @Size: 长度限制
     */
    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 100, message = "优惠券名称长度不能超过100个字符")
    private String name;

    /**
     * 优惠券类型
     * 1-满减券 2-折扣券
     */
    @NotNull(message = "优惠券类型不能为空")
    @Min(value = 1, message = "优惠券类型必须为1或2")
    @Max(value = 2, message = "优惠券类型必须为1或2")
    private Integer type;

    /**
     * 优惠金额（满减券使用）
     * @DecimalMin: 最小值限制
     */
    @DecimalMin(value = "0.01", message = "优惠金额必须大于0")
    private BigDecimal discountAmount;

    /**
     * 折扣率（折扣券使用）
     * 0.01-1.00，如0.8表示8折
     */
    @DecimalMin(value = "0.01", message = "折扣率必须大于0")
    @DecimalMax(value = "1.00", message = "折扣率不能大于1")
    private BigDecimal discountRate;

    /**
     * 最低消费金额
     */
    @NotNull(message = "最低消费金额不能为空")
    @DecimalMin(value = "0.01", message = "最低消费金额必须大于0")
    private BigDecimal minAmount;

    /**
     * 总数量
     */
    @NotNull(message = "总数量不能为空")
    @Min(value = 1, message = "总数量必须大于0")
    private Integer totalCount;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    /**
     * 状态
     * 0-禁用 1-启用
     */
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态必须为0或1")
    @Max(value = 1, message = "状态必须为0或1")
    private Integer status;
}