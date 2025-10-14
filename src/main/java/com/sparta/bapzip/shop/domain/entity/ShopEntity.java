package com.sparta.bapzip.shop.domain.entity;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.exception.ShopAlreadyDeletedException;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.UUID;


@Entity
@Table(name = "p_shops")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ShopStatusEnum status = ShopStatusEnum.PENDING;

//    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private UserEntity owner;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @JoinColumn(name = "service_area_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceAreaEntity serviceArea;

    public void updateName(String name) { this.name = name; }
    public void updateAddress(String address) { this.address = address; }
    public void updateLocation(Point location) { this.location = location; }
    public void updateCategory(CategoryEntity category) { this.category = category; }
    public void updateServiceArea(ServiceAreaEntity serviceArea) { this.serviceArea = serviceArea; }

    @Builder
    public ShopEntity(String name, String address, Point location,
                      UserEntity owner, CategoryEntity category, ServiceAreaEntity serviceArea) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.owner = owner;
        this.category = category;
        this.serviceArea = serviceArea;
    }

    /**
     * ShopEntity 생성 메서드
     *
     * @param name 가게 이름
     * @param address 가게 주소
     * @param location 가게 위치(Point)
     * @param owner 소유자(UserEntity)
     * @param category 카테고리(CategoryEntity)
     * @param serviceArea 서비스 지역(ServiceAreaEntity)
     * @return 생성된 ShopEntity 객체
     *
     * 생성 시 createdBy, updatedBy, createdAt, updatedAt를 자동으로 기록
     */
    public static ShopEntity create(String name, String address, Point location,
                                    UserEntity owner, CategoryEntity category, ServiceAreaEntity serviceArea) {
        ShopEntity shop = new ShopEntity(name, address, location, owner, category, serviceArea);
        shop.markCreated(owner.getId());
        shop.markUpdated(owner.getId());
        return shop;
    }

    /**
     * 가게 삭제 처리(Soft Delete)
     *
     * - 실제 DB에서 삭제하지 않고, isDeleted = true 처리
     * - 이미 삭제된 경우 GlobalException 발생
     * - 삭제 시 deletedBy, deletedAt를 기록
     *
     * @param userId 삭제를 요청한 사용자 ID (Owner)
     * @throws GlobalException 이미 삭제된 경우 SHOP_ALREADY_DELETED 예외 발생
     */
    public void softDelete(Long userId) {
        if (this.getIsDeleted()) {
            throw new ShopAlreadyDeletedException(ErrorCode.SHOP_ALREADY_DELETED);
        }
        markDeleted(userId);
    }
}
