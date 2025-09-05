package com.cchat.ingress_service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class AuthController {

    private final UserRepo users;
    private final PasswordEncoder pe;
    private final JwtService jwt;

    public AuthController(UserRepo users, PasswordEncoder pe, JwtService jwt) {
        this.users = users;
        this.pe = pe;
        this.jwt = jwt;
    }

    // DTO
    public record RegisterReq(String username, String password) {}
    public record LoginReq(String username, String password) {}
    public record Tokens(String access) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReq req) {
        if (req.username() == null || req.username().isBlank()
                || req.password() == null || req.password().length() < 4) {
            return ResponseEntity.badRequest().body("invalid username or password");
        }
        Optional<User> existing = users.findByLogin(req.username());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("username already taken");
        }

        User u = new User();
        u.setLogin(req.username());
        u.setPassword(pe.encode(req.password()));
        users.save(u);

        String access = jwt.generateToken(u.getLogin());
        return ResponseEntity.status(HttpStatus.CREATED).body(new Tokens(access));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        User u = users.findByLogin(req.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!pe.matches(req.password(), u.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String access = jwt.generateToken(u.getLogin());
        return ResponseEntity.ok(new Tokens(access));
    }
}
