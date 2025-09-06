package backend.mydays.service;

import backend.mydays.dto.kakao.KakaoUserInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import java.util.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoUserInfoResponse getKakaoUserInfo(String idToken) {
        try {
            // In a real app, you must validate the token's signature, expiration, issuer, etc.
            // For example: Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(idToken);

            String[] tokenParts = idToken.split("\\.");
            if (tokenParts.length < 2) {
                throw new IllegalArgumentException("Invalid ID token");
            }
            String payload = tokenParts[1];
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

            Map<String, Object> claims = objectMapper.readValue(decodedPayload, Map.class);

            String email = (String) claims.get("email");
            String nickname = (String) claims.get("nickname");

            // Construct a JSON string that mimics the structure expected by KakaoUserInfoResponse
            String json = String.format(
                "{\"kakao_account\": {\"email\": \"%s\", \"profile\": {\"nickname\": \"%s\"}}}",
                email, nickname
            );

            // Deserialize the JSON string into a KakaoUserInfoResponse object
            return objectMapper.readValue(json, KakaoUserInfoResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to decode or parse ID token", e);
        }
    }
}