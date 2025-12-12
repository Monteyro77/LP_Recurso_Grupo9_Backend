package com.expensetracker.service;

import com.expensetracker.config.JwtService;
import com.expensetracker.dto.LoginRequest;
import com.expensetracker.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserService userService;
    
    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            // Autentica o usuário
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            // Obtém o UserDetails
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Gera o token JWT
            String jwtToken = jwtService.generateToken(userDetails);
            
            // Busca informações adicionais do usuário
            com.expensetracker.model.User user = userService.findByUsername(userDetails.getUsername());
            
            // Retorna a resposta com o token
            return new LoginResponse(
                jwtToken,
                user.getUsername(),
                user.getName(),
                user.getEmail()
            );
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
    
    // Método para registrar usuário (opcional, pode estar no UserService)
    public com.expensetracker.model.User registerUser(
            com.expensetracker.model.User user) {
        // Verifica se usuário já existe
        if (userService.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userService.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        return userService.saveUser(user);
    }
}