package com.sparta.bapzip.shop.domain.entity;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.exception.ShopAlreadyDeletedException;
import com.sparta.bapzip.shop.presentation.dto.request.CreateShopRequest;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.Map;
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

//    @Builder
//    public ShopEntity(String name, String address, Point location,
//                      UserEntity owner, CategoryEntity category, ServiceAreaEntity serviceArea, ShopStatusEnum status) {
//        this.name = name;
//        this.address = address;
//        this.location = location;
//        this.owner = owner;
//        this.category = category;
//        this.serviceArea = serviceArea;
//        this.status = status != null ? status : ShopStatusEnum.PENDING;
//    }

    public static ShopEntity create(CreateShopRequest request, UserEntity owner,
                                    CategoryEntity category, ServiceAreaEntity serviceArea, Point location) {
        return  ShopEntity.builder()
                .name(request.getName())
                .address(request.getAddress())
                .location(location)
                .owner(owner)
                .category(category)
                .serviceArea(serviceArea)
                .status(ShopStatusEnum.PENDING)
                .build();
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

    public void updateStatus(ShopStatusEnum status) {
        if (status != null) {
            this.status = status;
        }
    }

}
