package tn.fst.spring.netvoyage.utils;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tn.fst.spring.netvoyage.enums.Role;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtils {
    @Value("${jwt.secret}")

    private String secretString;

    private Key jwtSecret; // Clé au format Key (pour HS512)
    private long jwtExpirationMs = 86400000; // Expiration du token (1 jour)

    @PostConstruct
    public void init() {
        // Décoder la clé secrète depuis la chaîne base64
        byte[] secretBytes = Base64.getDecoder().decode(secretString);
        jwtSecret = Keys.hmacShaKeyFor(secretBytes); // Crée l'objet Key
    }

    // Génération du token
    public String generateToken(String username , Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecret,SignatureAlgorithm.HS256)
                .compact();
    }
    public String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }


    // Récupération du nom d'utilisateur à partir du token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret) // Utilisation de la clé sécurisée
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validation du token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token); // Validation avec la clé sécurisée
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Récupération de la date d'expiration du token
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret) // Utilisation de la clé sécurisée
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // Vérification si le token est expiré
    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }
}
