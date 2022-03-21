package org.linkworld.yuansystem.util;


/*
*@Author  LXC BlueProtocol
*@Since   2022/1/28
*/

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Slf4j
@Component
public class JWTTokenUtil {


private String secret="BlueProtocol";

private static String header="token";
private Long expiration=1000*60*30l;



public static String getHeader(){
  return header;
}




public String generateToken(String openId,String session_key) {
    HashMap<String, Object> claims = new HashMap<>();
    claims.put("openId",openId);
    claims.put("session_key",session_key);
    return generateToken(claims);
}



public String getOpenIdFromToken(String token) {
    String openId;
    try {
        DecodedJWT decode = JWT.decode(token);
        openId = decode.getClaim("openId").asString();
        return openId;
    } catch (JWTDecodeException e) {
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE,"从token中解析OpenId失败"));
    }
    return null;
}

public String getSessionKeyFromToken(String token) {
    String session_key = null;
    try {
        Claims claims = getClaimsFromToken(token);
        session_key = claims.get("session_key", String.class);
        return session_key;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}


/**
  * 从令牌中获取用户名
  *
  * @param token 令牌
  * @return 用户名
  */


public String getUsernameFromToken(String token) {
  String username;
  try {
   Claims claims = getClaimsFromToken(token);
   username = claims.getSubject();
  } catch (Exception e) {
   e.printStackTrace();
   username = null;
  }
  return username;
}


public String getRoleFromToken(String token){
  String role;
  try {
   DecodedJWT decode = JWT.decode(token);
   role = decode.getClaim("role").asString();
//            Claims claims = getClaimsFromToken(token);
//            role = claims.get("role",String.class);
  } catch (Exception e) {
   role = null;
  }
  return role;
}




/**
  * 判断令牌是否过期
  *
  * @param token 令牌
  * @return 是否过期
  */


public Boolean isTokenExpired(String token) {
  try {
   Claims claims = getClaimsFromToken(token);
   Date expiration = claims.getExpiration();
   return expiration.before(new Date());
  } catch (Exception e) {
   return false;
  }
}




/**
  * 刷新令牌
  *
  * @param token 原令牌
  * @return 新令牌
  */


public String refreshToken(String token) {
  String refreshedToken;
  try {
   Claims claims = getClaimsFromToken(token);
   claims.put("created", new Date());
   refreshedToken = generateToken(claims);
  } catch (Exception e) {
   refreshedToken = null;
  }
  return refreshedToken;
}






/**
  * 从claims生成令牌,如果看不懂就看谁调用它
  *
  * @param claims 数据声明
  * @return 令牌
  */


private String generateToken(Map<String, Object> claims) {
  Date expirationDate = new Date(System.currentTimeMillis() + expiration);
  String token= Jwts.builder().setClaims(claims)
          .setExpiration(expirationDate)
          .signWith(SignatureAlgorithm.HS512, secret)
          .compact();
  return token;
}




/**
  * 从令牌中获取数据声明,如果看不懂就看谁调用它
  *
  * @param token 令牌
  * @return 数据声明
  */


private Claims getClaimsFromToken(String token) {
  Claims claims;
  try {
   claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  } catch (Exception e) {
   e.printStackTrace();
   claims = null;
  }
  return claims;
}


public Date getExpiration(String token){
  Date date;
  Claims claims=getClaimsFromToken(token);
  try {
   date = claims.getExpiration();
  }catch (Exception e){
   return null;
  }
  return date;
}
public Date getCreated(String token){
  Date date;
  Claims claims=getClaimsFromToken(token);
  try {
   date = claims.get("created",Date.class);
  }catch (Exception e){
   return null;
  }
  return date;
}


public String getSecret() {
  return secret;
}


public void setSecret(String secret) {
  this.secret = secret;
}


public static void setHeader(String header) {
  JWTTokenUtil.header = header;
}


public Long getExpiration() {
  return expiration;
}


public void setExpiration(Long expiration) {
  this.expiration = expiration;
}


public JWTTokenUtil() {
}
}