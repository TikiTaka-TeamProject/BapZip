package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import lombok.NonNull;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ShopJpaRepository extends JpaRepository<ShopEntity, UUID> {
    boolean existsByOwnerId(Long ownerId);

    @NonNull
    Optional<ShopEntity> findById(@NonNull UUID shopId);

    Page<ShopEntity> findByStatus(ShopStatusEnum shopStatusEnum, Pageable pageable);

    Page<ShopEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId, Pageable pageable);
    Optional<ShopEntity> findByIdAndIsDeletedFalse(UUID shopId);

    Page<ShopEntity> findByCategoryId(UUID categoryId, Pageable pageable);

    /**
     * 이름, 카테고리, 영역 Polygon으로 검색
     * - name: 부분 일치 (ILIKE)
     * - categoryId: 일치
     * - areaPolygon: 위치가 폴리곤 내부인지 확인
     */
    @Query(value = """
            SELECT * 
            FROM p_shops s
            WHERE (:name IS NULL OR s.name ILIKE %:name%)
              AND (:categoryId IS NULL OR s.category_id = :categoryId)
              AND (:areaPolygon IS NULL OR ST_Contains(:areaPolygon, s.location))
              AND s.is_deleted = false
            """,
            countQuery = """
            SELECT COUNT(*) 
            FROM p_shops s
            WHERE (:name IS NULL OR s.name ILIKE %:name%)
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

    @Query(value = """
        SELECT * 
        FROM p_shops s
        WHERE (:name IS NULL OR s.name ILIKE %:name%)
          AND (:categoryId IS NULL OR s.category_id = :categoryId)
          AND s.is_deleted = false
        """,
            nativeQuery = true)
    Page<ShopEntity> findShopsWithoutPolygon(
            @Param("name") String name,
            @Param("categoryId") UUID categoryId,
            Pageable pageable
    );

    @Query(value = """
        SELECT s.*, COALESCE(AVG(r.score), 0) AS avg_score
        FROM p_shops s
        LEFT JOIN p_reviews r ON s.id = r.shop_id
        WHERE s.id = :shopId
        GROUP BY s.id
    """, nativeQuery = true)
    Optional<ShopEntity> findShopWithAvgScore(@Param("shopId") UUID shopId);
}
