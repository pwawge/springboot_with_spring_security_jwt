package com.auth.controller;

import com.auth.service.CustomUserDetailsService;
import com.auth.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class SampleController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(value = "/dashboard")
    public ResponseEntity<?> openDashboard(){
        Map<String,Object> responseMap = new  HashMap<String,Object>();
        responseMap.put("Message","Hello , Buddy");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> login(@RequestBody Map<String,String> credential) {
        Map<String,Object> responseMap = new  HashMap<String,Object>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credential.get("username").toString(), credential.get("password")));
        }catch (BadCredentialsException ex){
            responseMap.put("Message","Your password or username is incorrect");
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
       final UserDetails userDetails =  customUserDetailsService.loadUserByUsername(credential.get("username"));
         final String jwtToken = jwtUtil.generateToken(userDetails);

        responseMap.put("Token",jwtToken);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
