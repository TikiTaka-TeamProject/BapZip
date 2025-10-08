package com.sparta.bapzip.order.application;

import com.sparta.bapzip.menu.application.MenuServiceV1;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.order.application.dto.OrderCreationResult;
import com.sparta.bapzip.order.application.exception.MenusNotFoundInOrderException;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.repository.OrderRepository;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.ordermenu.application.OrderMenuServiceV1;
import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sparta.bapzip.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

    private final OrderRepository orderRepository;
    private final ShopServiceV1 shopService;
    private final MenuServiceV1 menuService;
    private final OrderMenuServiceV1 orderMenuService;

    /**
     * 새로운 주문을 생성한다.
     *
     * @param request 주문 요청 DTO (가게 ID, 메뉴 ID 및 수량 정보 포함)
     * @return 생성된 주문 응답(ResponseEntity)
     */
    @Transactional
    public OrderCreationResult createOrder(@Valid CreateOrderRequest request) {
        ShopEntity shop = shopService.getShopById(request.getShopId());
        Map<UUID, MenuEntity> menuMap = getMenuMap(request.getMenuInfoList());

        OrderEntity order = OrderEntity.create(request, shop.getId(), menuMap);
        OrderEntity savedOrder = orderRepository.save(order);

        List<OrderMenuEntity> orderMenuList = createOrderMenusFromRequest(
                request.getMenuInfoList(),
                menuMap,
                savedOrder
        );

        orderMenuList.forEach(savedOrder::addOrderMenu);

        orderMenuService.saveAll(orderMenuList);

        return OrderCreationResult.from(savedOrder, orderMenuList, shop, 1L);
    }

    // private 헬퍼 메서드

    /**
     * 메뉴 ID를 키로 갖는 메뉴 엔티티 Map 반환
     *
     * @param menuInfoList 주문 요청에 포함된 메뉴 정보
     * @return 메뉴 ID를 키로 갖는 메뉴 엔티티 Map 반환
     * @throws IllegalArgumentException 존재하지 않는 메뉴 ID가 포함된 경우
     */
    private Map<UUID, MenuEntity> getMenuMap(List<CreateOrderRequest.MenuInfo> menuInfoList) {
        List<UUID> menuIdList = menuInfoList.stream()
                .map(CreateOrderRequest.MenuInfo::getMenuId)
                .toList();

        List<MenuEntity> menuList = menuService.getMenusByIds(menuIdList);

        if (menuList.size() != menuInfoList.size()) {
            throw new MenusNotFoundInOrderException(MENU_NOT_FOUNT_IN_ORDER);
        }

        return menuList.stream()
                .collect(Collectors.toMap(
                        MenuEntity::getId,
                        Function.identity())
                );
    }
    /**
     * 주문 요청 정보를 기반으로 OrderMenu 엔티티 리스트 생성
     *
     * @param menuInfoList 주문 요청의 메뉴 정보 (ID, 수량)
     * @param menuMap 메뉴 ID를 키로 하는 메뉴 엔티티 맵
     * @param orderEntity 생성된 주문 엔티티
     * @return 생성된 주문 메뉴 엔티티 리스트
     */
    private List<OrderMenuEntity> createOrderMenusFromRequest(
            List<CreateOrderRequest.MenuInfo> menuInfoList,
            Map<UUID, MenuEntity> menuMap,
            OrderEntity orderEntity
    ) {
        return menuInfoList.stream()
                .map(menuInfo -> {
                    MenuEntity menu = menuMap.get(menuInfo.getMenuId());

                    return OrderMenuEntity.create(
                            orderEntity,
                            menu,
                            menuInfo
                    );
                }).toList();
    }
}
