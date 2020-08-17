package com.rmit.sept.septbackend.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtUtilsTests {

    private JwtUtils jwtUtils = new JwtUtils();

    @BeforeAll
    public void initProperties() {
        jwtUtils.setJwtExpirationMs(86400000);
        jwtUtils.setJwtSecret("test_secret");
    }

    @Test
    public void testGenerateJwt() throws InterruptedException, IOException {
        String testUsername = "test_username";
        String testPassword = "test_password";
        List<SimpleGrantedAuthority> customer = Collections.singletonList(new SimpleGrantedAuthority("CUSTOMER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new UserDetailsImpl(
                        001,
                        testUsername,
                        testPassword,
                        customer
                ),
                null,
                customer
        );

        String first = jwtUtils.generateJwtToken(authentication);
        // Increment expiry so tokens don't match
        jwtUtils.setJwtExpirationMs(1000);
        String second = jwtUtils.generateJwtToken(authentication);

        String middleFirst = first.substring(first.indexOf('.') + 1, first.lastIndexOf('.'));
        String middleSecond = second.substring(second.indexOf('.') + 1, second.lastIndexOf('.'));

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] decode = Base64.getDecoder().decode(middleFirst);
        JsonNode tree = objectMapper.readTree(decode);
        String firstActual = tree.get("sub").textValue();

        decode = Base64.getDecoder().decode(middleSecond);
        tree = objectMapper.readTree(decode);
        String secondActual = tree.get("sub").textValue();

        Assertions.assertEquals(testUsername, firstActual);
        Assertions.assertEquals(testUsername, secondActual);

        // Signature must not be the same as different expiry times
        Assertions.assertNotEquals(first, second);
    }
}
