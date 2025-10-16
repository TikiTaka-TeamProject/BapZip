package com.sparta.bapzip.shop.domain.entity;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.exception.ShopAlreadyDeletedException;
import com.sparta.bapzip.shop.application.dto.request.ShopCreationRequest;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

/**
 * Shop 엔티티
 * <p>
 * 가게 정보를 저장하는 엔티티로, 이름, 주소, 위치, 상태, 카테고리, 서비스 지역, 소유자 정보를 포함.
 * Soft delete와 상태 변경 등 도메인 로직을 포함하고 있음.
 */
@Entity
@Table(name = "p_shops")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopEntity extends BaseEntity {

    /** 가게 UUID (Primary Key) */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** 가게 이름 */
    @Column(nullable = false)
    private String name;

    /** 가게 주소 */
    @Column(nullable = false)
    private String address;

    /** 가게 상태 (PENDING, APPROVED 등) */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ShopStatusEnum status = ShopStatusEnum.PENDING;

    /** 가게 위치 좌표(Point) */
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    /** 가게 소유자 */
    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private UserEntity owner;

    /** 가게 카테고리 */
    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    // ===========================
    // Update Methods (Domain Logic)
    // ===========================

    /**
     * 가게 이름 수정
     *
     * @param name 새로운 가게 이름
     */
    public void updateName(String name) {
        this.name = name;
    }

    /**
     * 가게 주소 수정
     *
     * @param address 새로운 가게 주소
     */
    public void updateAddress(String address) {
        this.address = address;
    }

    /**
     * 가게 위치(Point) 수정
     *
     * @param location 새로운 위치(Point)
     */
    public void updateLocation(Point location) {
        this.location = location;
    }

    /**
     * 가게 카테고리 수정
     *
     * @param category 새로운 카테고리 엔티티
     */
    public void updateCategory(CategoryEntity category) {
        this.category = category;
    }

    /**
     * 가게 상태 업데이트
     *
     * @param status 새로운 상태
     */
    public void updateStatus(ShopStatusEnum status) {
        if (status != null) {
            this.status = status;
        }
    }

    /**
     * 가게 삭제 처리 (Soft Delete)
     *
     * @param userId 삭제 요청자 ID
     * @throws ShopAlreadyDeletedException 이미 삭제된 경우
     */
    public void softDelete(Long userId) {
        if (this.getIsDeleted()) {
            throw new ShopAlreadyDeletedException(ErrorCode.SHOP_ALREADY_DELETED);
        }
        markDeleted(userId);
    }

    // ===========================
    // Factory Method
    // ===========================


    /**
     *
     * 새로운 가게 생성
     *
     * @param name          가게 이름
     * @param address       가게 주소
     * @param owner         소유자 UserEntity
     * @param category      카테고리 CategoryEntity
     * @param location      위치 좌표(Point)
     * @return ShopEntity
     */
    public static ShopEntity create(
            String name,
            String address,
            UserEntity owner,
            CategoryEntity category,
            Point location
    ) {
        return ShopEntity.builder()
                .name(name)
                .address(address)
                .owner(owner)
                .category(category)
                .location(location)
                .status(ShopStatusEnum.PENDING)
                .build();
    }
}
