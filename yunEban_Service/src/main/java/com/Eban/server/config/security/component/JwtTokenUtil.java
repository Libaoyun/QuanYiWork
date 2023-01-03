package com.Eban.server.config.security.component;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: yunEban_Service
 * @description: JwtToken工具类
 * @packagename: com.Eban.server.config.security
 * @author: Libaoyun
 * @date: 2022-08-15 13:08
 **/
@Component
public class JwtTokenUtil {

//    将载荷用户名的键设置为sub，下面同理
    private static final String CLAIM_KEY_USERNAME="sub";
    private static final String CLAIM_KEY_CREATED="created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

//    根据用户信息生成token
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

//    根据载荷claims生成JWT Token
    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
         }

//         设置token失效时间
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration*1000);
    }


//   以上是根据信息生成token

//   下面是根据token解析信息,获取用户名
    public String getUsernameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

//    从Token中获取荷载
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        }
        return claims;
    }

//    验证token是否有效，从两个方面，1：有效期是否过了，2：载荷中的用户名与UserDetails中用户名是否一致
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

//    判断token是否失效,这里用了before，所以取反了
    private boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

//    判断token是否可以被刷新，只要过期就可以被刷新
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

//    刷新token，只要将过期时间更新即可
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

}
