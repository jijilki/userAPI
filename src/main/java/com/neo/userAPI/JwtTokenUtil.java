package com.neo.userAPI;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    private static final long serialVersionUID = -2550185165626007488L;
    private static final String USER_ROLES = "user_roles";

    /*    private static final String AUTHORITIES_KEY = "auth";*/
    @Value("${jwt.secret}")
    public String SECRET_KEY;

    public static Long getUserIdFromSecurityContext() {

        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

    }


    public UsernamePasswordAuthenticationToken getAuthentication(String token) {

        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(claims.get(USER_ROLES).toString().split(",")).stream()
                .map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }

    public boolean validateToken(String jsonToken) {
        boolean isValidated;


        String algoType = "";


        // Validate JSON token : Check for the Encryption , Algo exists, Issuer etc.


        // Splitting Json Token to chunks String array
        String[] chunks = jsonToken.split("\\.");

        // Getting the Base 64 Decoder
        Base64.Decoder decoder = Base64.getDecoder();


        String tokenHeader = new String(decoder.decode(chunks[0]));
        String tokenBody = new String(decoder.decode(chunks[1]));
        System.out.println(tokenHeader + " " + tokenBody);

        //TODO: Identify the header for the valid algo from token // Using always the HS512 to create though.
        SignatureAlgorithm sa = switch (tokenHeader.substring(8, 13)) {
            case "HS512" -> SignatureAlgorithm.HS512;
            case "HS256" -> SignatureAlgorithm.HS256;
            case "null" , default -> throw new UnsupportedOperationException();
        };


        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        SecretKeySpec secretKeySpec = new SecretKeySpec(apiKeySecretBytes, sa.getJcaName());
        DefaultJwtSignatureValidator defaultJwtSignatureValidator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String tokenSignature = chunks[2];

       /* System.out.println(tokenWithoutSignature + " === " + tokenSignature + "====" + apiKeySecretBytes);*/

        //Verifying JWT token integrity
        if (defaultJwtSignatureValidator.isValid(tokenWithoutSignature, tokenSignature)) {
            isValidated = true;
        } else {
            isValidated = false;
        }

        return isValidated;
    }


    public String generateToken(UserEntity userEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ROLES, userEntity.getUserRoles());
        return doGenerateToken(claims, Long.toString(userEntity.getUserId()));
    }


    public String doGenerateToken(Map<String, Object> claims, String subject) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .setIssuer("UserAPI")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, signingKey) // TODO - To implement using asymmetric key , use RSA Asymmetric key with Private Public key For Verifying Signature
                .compact();

    }

}
