package com.suman.sharecare.auth.service;

import com.suman.sharecare.auth.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.auth.dto.user_dtos.*;
import com.suman.sharecare.auth.entity.*;
import com.suman.sharecare.auth.exception.ActionNotAllowedException;
import com.suman.sharecare.auth.exception.DuplicateResourceException;
import com.suman.sharecare.auth.exception.ResourceNotFoundException;
import com.suman.sharecare.auth.repository.PasswordResetTokenRepository;
import com.suman.sharecare.auth.repository.UserRepository;
import com.suman.sharecare.auth.security.jwt.JwtService;
import com.suman.sharecare.auth.util.UserMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordService passwordService;

    public void register(UserRegisterRequestDto userRegisterRequestDto) {
        if(userRepository.existsByUsername(userRegisterRequestDto.getUsername())) {
            throw new DuplicateResourceException("Username already exists.");
        }
        if(userRepository.existsByEmail(userRegisterRequestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        User user = userMapper.toEntity(userRegisterRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleService.getRoleByName(RoleService.CITIZEN);
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        EmailVerificationToken emailVerificationToken = emailVerificationService.generateEmailVerificationToken(savedUser);
        emailService.sendVerificationEmail(emailVerificationToken);
    }

    public UserResponseDto viewProfile(String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return userMapper.toDto(user);
    }

    public UserResponseDto updateProfile(String id, UserUpdateRequestDto userUpdateRequestDto) {
        User existingUser = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        userMapper.updateToEntity(userUpdateRequestDto, existingUser);
        return userMapper.toDto(userRepository.save(existingUser));
    }

    public ApiResponseDto deleteProfile(String id) {
        userRepository.deleteById(UUID.fromString(id));
        return new ApiResponseDto(HttpStatus.OK.value(), "Your account has been deleted.");
    }

    public AuthResponseDto login(@Valid AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
        );
        User user = (User) (authentication.getPrincipal());
        UUID userId = user.getId();
        String username = user.getUsername();
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String accessToken = jwtService.generateToken(username, roles, userId);
        String refreshToken = refreshTokenService.generateRefreshToken(user);
        return new AuthResponseDto(accessToken, refreshToken);
    }

    public AuthResponseDto refresh(RefreshTokenRequestDto tokenRequestDto) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(tokenRequestDto.getToken());
        refreshTokenService.revokeRefreshToken(refreshToken);
        User user = refreshToken.getUser();
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String newRefreshToken = refreshTokenService.generateRefreshToken(user);
        String newAccessToken = jwtService.generateToken(user.getUsername(), roles, user.getId());
        return new AuthResponseDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void verifyEmail(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationService.verifyEmail(token);
        User user = emailVerificationToken.getUser();
        user.setEmailVerified(true);
    }

    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            return;
        }
        if(user.isEmailVerified()) {
            throw new ActionNotAllowedException("Email is already verified.");
        }
        EmailVerificationToken emailVerificationToken = emailVerificationService.reuseOrGenerateToken(user, LocalDateTime.now());
        emailService.sendVerificationEmail(emailVerificationToken);
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            return;
        }
        if(!user.isEmailVerified()) {
            throw new ActionNotAllowedException("Please verify your email id first.");
        }
        PasswordResetToken passwordResetToken = passwordService.reuseOrGenerateToken(user, LocalDateTime.now());
        emailService.sendPasswordResetEmail(passwordResetToken);
    }

    @Transactional
    public void resetPassword(NewPasswordRequestDto newPasswordRequestDto) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(newPasswordRequestDto.getToken()).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
        if(passwordResetToken.isUsed() || passwordResetToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new ActionNotAllowedException("The token has expired or has been used before.");
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPasswordRequestDto.getNewPassword()));
        passwordResetToken.setUsed(true);
    }
}