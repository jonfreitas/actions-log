package plurallid.auth.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Utilities for generating a JWT
 * <p>
 * Caught at this address
 * <a href="https://quarkus.io/guides/security-jwt">...</a>
 */
@Component
@Configuration
public class JwtActionUtil {

    private JwtActionUtil() {
    }

    public static String generateTokenString(final String jsonResName, final Map<String, Long> timeClaims, String applicationId) throws Exception {
        final PrivateKey pk = readPrivateKey("/privateKey.pem");
        return generateTokenString(pk, "/privateKey.pem", jsonResName, timeClaims, applicationId);
    }

    public static String generateTokenString(final PrivateKey privateKey, final String kid,
            final String jsonResName, final Map<String, Long> timeClaims, String applicationId) throws Exception {

        final JwtClaims claims = JwtClaims.parse(readTokenContent(jsonResName));
        final long currentTimeInSecs = currentTimeInSecs();
        final long exp = timeClaims != null && timeClaims.containsKey(Claims.exp.name())
                ? timeClaims.get(Claims.exp.name())
                : currentTimeInSecs + 86400;

        claims.setIssuedAt(NumericDate.fromSeconds(currentTimeInSecs));
        claims.setClaim("applicationId", applicationId);
        claims.setExpirationTime(NumericDate.fromSeconds(exp));

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(privateKey);
        jws.setKeyIdHeaderValue(kid);
        jws.setHeader("typ", "JWT");
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }

    private static String readTokenContent(final String jsonResName) throws IOException {
        final InputStream contentIS = JwtActionUtil.class.getResourceAsStream(jsonResName);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(contentIS))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    public static PrivateKey readPrivateKey(final String pemResName) throws Exception {
        final InputStream contentIS = JwtActionUtil.class.getResourceAsStream(pemResName);
        final byte[] tmp = new byte[4096];
        final int length = contentIS.read(tmp);
        return decodePrivateKey(new String(tmp, 0, length, "UTF-8"));
    }

    public static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
        final byte[] encodedBytes = toEncodedBytes(pemEncoded);

        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        final KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

    public static int currentTimeInSecs() {
        final long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }

}