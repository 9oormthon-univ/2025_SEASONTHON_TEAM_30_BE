package backend.mydays.service;

import backend.mydays.dto.kakao.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    public KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<KakaoUserInfoResponse> response = restTemplate.postForEntity(kakaoUserInfoUri, request, KakaoUserInfoResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        // In a real app, you'd throw a custom exception here
        throw new RuntimeException("Failed to get user info from Kakao");
    }
}
