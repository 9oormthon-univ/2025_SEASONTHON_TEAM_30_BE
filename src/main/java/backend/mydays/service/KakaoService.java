package backend.mydays.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.issuer-uri}")
    private String issuerUri;

    @Value("${kakao.jwks-uri}")
    private String jwksUri;

    public Claims getClaimsFromIdToken(String idToken) {
        String kid = getKidFromHeader(idToken);
        Map<String, Object> jwks = getJwks();
        Map<String, Object> keyInfo = (Map<String, Object>) jwks.get(kid);

        PublicKey publicKey = getPublicKey(keyInfo);

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .requireIssuer(issuerUri)
                .requireAudience(clientId)
                .build();

        return parser.parseClaimsJws(idToken).getBody();
    }

    private String getKidFromHeader(String idToken) {
        try {
            String header = idToken.substring(0, idToken.indexOf('.'));
            String decodedHeader = new String(Base64.getUrlDecoder().decode(header));
            Map<String, Object> headerMap = objectMapper.readValue(decodedHeader, Map.class);
            return (String) headerMap.get("kid");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse ID token header", e);
        }
    }

    private Map<String, Object> getJwks() {
        // In a real application, you should cache this response.
        return restTemplate.getForObject(jwksUri, Map.class);
    }

    private PublicKey getPublicKey(Map<String, Object> keyInfo) {
        String n = (String) keyInfo.get("n");
        String e = (String) keyInfo.get("e");

        byte[] nBytes = Base64.getUrlDecoder().decode(n);
        byte[] eBytes = Base64.getUrlDecoder().decode(e);

        BigInteger nInt = new BigInteger(1, nBytes);
        BigInteger eInt = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(nInt, eInt);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException("Failed to generate public key", ex);
        }
    }
}