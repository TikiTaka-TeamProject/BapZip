package com.sparta.bapzip.ai.domain.entity;

import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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

    private UUID menuId;

    public static AiEntity create(String prompt, String response, UserEntity user, UUID menuId){
        return AiEntity.builder()
                .prompt(prompt)
                .response(response)
                .user(user)
                .menuId(menuId)
                .build();
    }

}
