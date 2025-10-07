package com.yx.nas.entity.base;

import org.babyfish.jimmer.sql.Column;
import org.babyfish.jimmer.sql.MappedSuperclass;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

@MappedSuperclass
public interface BaseEntity {
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    LocalDateTime createTime();

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @Nullable
    LocalDateTime updateTime();

}
