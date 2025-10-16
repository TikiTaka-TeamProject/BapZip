package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.shop.application.dto.ShopWithAvgScoreDto;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import lombok.NonNull;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Shop 엔티티 JPA Repository
 * <p>
 * 기본 CRUD, 상태 조회, 카테고리 조회, 위치 기반 검색, 평균 평점 조회 기능 제공
 */
public interface ShopJpaRepository extends JpaRepository<ShopEntity, UUID> {

    /**
     * 특정 Owner가 이미 가게를 보유하고 있는지 확인
     *
     * @param ownerId Owner ID
     * @return 존재 여부
     */
    boolean existsByOwnerId(Long ownerId);

    /**
     * UUID로 Shop 조회
     *
     * @param shopId Shop UUID
     * @return ShopEntity Optional
     */
    @NonNull
    Optional<ShopEntity> findById(@NonNull UUID shopId);

    /**
     * 상태별 가게 조회
     *
     * @param shopStatusEnum 상태
     * @param pageable       페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findByStatus(ShopStatusEnum shopStatusEnum, Pageable pageable);

    /**
     * 카테고리 ID 기준 삭제되지 않은 가게 조회
     *
     * @param categoryId 카테고리 UUID
     * @param pageable   페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    Page<ShopEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId, Pageable pageable);

    /**
     * 삭제되지 않은 가게 UUID 기준 조회
     *
     * @param shopId Shop UUID
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
     * @param name        가게 이름 (부분 일치)
     * @param categoryId  카테고리 UUID
     * @param areaPolygon 검색 영역
     * @param pageable    페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    @Query(value = """
            SELECT * 
            FROM p_shops s
            WHERE (:name IS NULL OR s.name ILIKE CONCAT('%', :name, '%'))
              AND (:categoryId IS NULL OR s.category_id = :categoryId)
              AND (:areaPolygon IS NULL OR ST_Contains(:areaPolygon, s.location))
              AND s.is_deleted = false
            """,
            countQuery = """
            SELECT COUNT(*) 
            FROM p_shops s
            WHERE (:name IS NULL OR s.name ILIKE CONCAT('%', :name, '%'))
              AND (:categoryId IS NULL OR s.category_id = :categoryId)
              AND (:areaPolygon IS NULL OR ST_Contains(:areaPolygon, s.location))
              AND s.is_deleted = false
            """,
            nativeQuery = true)
    Page<ShopEntity> findShopsByFilters(
            @Param("name") String name,
            @Param("categoryId") UUID categoryId,
            @Param("areaPolygon") Polygon areaPolygon,
            Pageable pageable
    );

    /**
     * Polygon 없이 이름, 카테고리 기준 가게 조회
     *
     * @param name       가게 이름 (부분 일치)
     * @param categoryId 카테고리 UUID
     * @param pageable   페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    @Query(value = """
        SELECT * 
        FROM p_shops s
        WHERE (:name IS NULL OR s.name ILIKE CONCAT('%', :name, '%'))
          AND (:categoryId IS NULL OR s.category_id = :categoryId)
          AND s.is_deleted = false
        """,
            countQuery = """
            SELECT COUNT(*)
            FROM p_shops s
            WHERE (:name IS NULL OR s.name ILIKE CONCAT('%', :name, '%'))
              AND (:categoryId IS NULL OR s.category_id = :categoryId)
              AND s.is_deleted = false
        """,
            nativeQuery = true)
    Page<ShopEntity> findShopsWithoutPolygon(
            @Param("name") String name,
            @Param("categoryId") UUID categoryId,
            Pageable pageable
    );

    /**
     * 평균 평점 포함 가게 조회
     *
     * @param shopId Shop UUID
     * @return ShopEntity Optional
     */
    @Query(value = """
        SELECT s.*, COALESCE(AVG(r.score), 0) AS avg_score
        FROM p_shops s
        LEFT JOIN p_reviews r ON s.id = r.shop_id
        WHERE s.id = :shopId
        GROUP BY s.id
    """, nativeQuery = true)
    Optional<ShopEntity> findShopWithAvgScore(@Param("shopId") UUID shopId);

    /**
     * 일반 검색: 이름, 카테고리 기준 JPQL 검색
     *
     * @param name       가게 이름
     * @param categoryId 카테고리 UUID
     * @param pageable   페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    @Query("""
       SELECT s 
       FROM ShopEntity s
       WHERE (:name IS NULL OR s.name LIKE CONCAT('%', :name, '%'))
         AND (:categoryId IS NULL OR s.category.id = :categoryId)
         AND s.isDeleted = false
       """)
    Page<ShopEntity> findShops(
            @Param("name") String name,
            @Param("categoryId") UUID categoryId,
            Pageable pageable
    );

    /**
     * Polygon 포함 검색: 이름, 카테고리 기준 NativeQuery
     *
     * @param name        가게 이름
     * @param categoryId  카테고리 UUID
     * @param areaPolygon 검색 영역
     * @param pageable    페이지 정보
     * @return 페이지 처리된 ShopEntity 리스트
     */
    @Query(value = """
            SELECT * 
            FROM p_shops s
            WHERE (:name IS NULL OR s.name ILIKE CONCAT('%', :name, '%'))
              AND (:categoryId IS NULL OR s.category_id = :categoryId)
              AND (:areaPolygon IS NULL OR ST_Contains(:areaPolygon, s.location))
              AND s.is_deleted = false
            """,
            countQuery = """
            SELECT COUNT(*) 
            FROM p_shops s
            WHERE (:name IS NULL OR s.name ILIKE CONCAT('%', :name, '%'))
              AND (:categoryId IS NULL OR s.category_id = :categoryId)
              AND (:areaPolygon IS NULL OR ST_Contains(:areaPolygon, s.location))
              AND s.is_deleted = false
            """,
            nativeQuery = true)
    Page<ShopEntity> findShopsByPolygon(
            @Param("name") String name,
            @Param("categoryId") UUID categoryId,
            @Param("areaPolygon") Polygon areaPolygon,
            Pageable pageable
    );

    @Query(value = """
        SELECT s.id AS shopId,
               COALESCE(AVG(r.score), 0) AS avgScore
        FROM p_shops s
        LEFT JOIN p_reviews r ON s.id = r.shop_id AND r.is_deleted = false
        WHERE s.is_deleted = false
        GROUP BY s.id
        """, nativeQuery = true)
    List<ShopWithAvgScoreDto> findAllWithAvgScore();
}
