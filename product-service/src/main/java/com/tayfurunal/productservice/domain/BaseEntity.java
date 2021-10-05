package com.tayfurunal.productservice.domain;

import com.tayfurunal.productservice.util.MdcUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@MappedSuperclass
@SuperBuilder
@ToString
abstract class BaseEntity {

    @Column(name = "created_user_id", nullable = false)
    private Integer createdUserId;

    @Column(name = "updated_user_id")
    private Integer updatedUserId;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void prePersist() {
        this.createdUserId = MdcUtils.getAgentUserId();
        this.updatedUserId = MdcUtils.getAgentUserId();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedUserId = MdcUtils.getAgentUserId();
    }
}
