package com.sparta.bapzip.ai.domain.entity;

import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Entity
@Table(name = "p_ai_logs")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AiEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String prompt;

    @Column(nullable = false)
    private String response;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @JoinColumn(name = "menu_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuEntity menu;

    public AiEntity(String prompt, @Nullable String response, UserEntity user, MenuEntity menu) {
        this.prompt = prompt;
        this.response = response;
        this.user = user;
        this.menu = menu;
    }
}
