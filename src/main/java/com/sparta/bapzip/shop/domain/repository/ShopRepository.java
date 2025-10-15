package com.sparta.bapzip.shop.domain.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Shop 엔티티에 대한 Repository 인터페이스
 * <p>
 * 가게 조회, 저장, 상태별 검색 등 데이터 접근 메서드를 정의.
 */
public interface ShopRepository {

    /**
     * 가게 저장
     *
     * @param shop 저장할 ShopEntity
     * @return 저장된 ShopEntity
     */
    ShopEntity save(ShopEntity shop);

    /**
     * 특정 Owner가 이미 가게를 가지고 있는지 확인
     *
     * @param ownerId Owner ID
     * @return 존재 여부
     */
    boolean existsByOwnerId(Long ownerId);

    /**
     * UUID로 가게 조회
     *
     * @param shopId 조회할 가게 UUID
     * @return ShopEntity Optional
     */
    Optional<ShopEntity> findById(UUID shopId);

    /**
     * 상태별 가게 조회
     *
     * @param status   조회할 상태
     * @param pageable 페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findByStatus(ShopStatusEnum status, Pageable pageable);

    /**
     * 카테고리 ID 기준 삭제되지 않은 가게 조회
     *
     * @param categoryId 카테고리 UUID
     * @param pageable   페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId, Pageable pageable);

    /**
     * 모든 가게 조회 (페이지 처리)
     *
     * @param pageable 페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findAll(Pageable pageable);

    /**
     * 삭제되지 않은 가게 UUID 기준 조회
     *
     * @param shopId 가게 UUID
     * @return ShopEntity Optional
     */
    Optional<ShopEntity> findByIdAndIsDeletedFalse(UUID shopId);

    /**
     * 카테고리 ID 기준 가게 조회
     *
     * @param categoryId 카테고리 UUID
     * @param pageable   페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findByCategoryId(UUID categoryId, Pageable pageable);

    /**
     * 이름, 카테고리, 영역 Polygon 기준 필터링된 가게 조회
     *
     * @param name        가게 이름
     * @param categoryId  카테고리 UUID
     * @param areaPolygon 영역 Polygon
     * @param pageable    페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findShopsByFilters(String name, UUID categoryId, Polygon areaPolygon, Pageable pageable);

    /**
     * Polygon 없이 이름, 카테고리 기준 가게 조회
     *
     * @param name       가게 이름
     * @param categoryId 카테고리 UUID
     * @param pageable   페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findShopsWithoutPolygon(String name, UUID categoryId, Pageable pageable);

    /**
     * 평균 평점 포함 가게 조회
     *
     * @param shopId 가게 UUID
     * @return ShopEntity Optional
     */
    Optional<ShopEntity> findShopWithAvgScore(UUID shopId);

    /**
     * Polygon 포함 이름, 카테고리 기준 가게 조회
     *
     * @param name        가게 이름
     * @param categoryId  카테고리 UUID
     * @param areaPolygon 영역 Polygon
     * @param pageable    페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findShopsByPolygon(String name, UUID categoryId, Polygon areaPolygon, Pageable pageable);

    /**
     * 이름, 카테고리 기준 가게 조회
     *
     * @param name       가게 이름
     * @param categoryId 카테고리 UUID
     * @param pageable   페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findShops(String name, UUID categoryId, Pageable pageable);
}
