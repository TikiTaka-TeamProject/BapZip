package com.sparta.bapzip.kakaolocal.presentation;

import com.sparta.bapzip.kakaolocal.application.KakaoLocalServiceV1;
import com.sparta.bapzip.kakaolocal.application.dto.KakaoLocalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/kakao")
@RequiredArgsConstructor
public class KakaoLocalApiController {

    private final KakaoLocalServiceV1 kakaoLocalServiceV1;

    /**
     * 테스트용: 좌표로 주소 가져오기
     * GET /api/v1/kakao/coordinates-to-address?longitude=126.974971&latitude=37.574825
     */
    @GetMapping("/coordinates-to-address")
    public ResponseEntity<KakaoLocalResponseDto> coordinatesToAddress(
            @RequestParam Double longitude,
            @RequestParam Double latitude
    ) {
        // null 체크 포함
        if (longitude == null || latitude == null) {
            return ResponseEntity.badRequest().body(null);
        }

        KakaoLocalResponseDto response = kakaoLocalServiceV1.getAddressFromCoordinates(longitude, latitude);
        return ResponseEntity.ok(response);
    }
}
