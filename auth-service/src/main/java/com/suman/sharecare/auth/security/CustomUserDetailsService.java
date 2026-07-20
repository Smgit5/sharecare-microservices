package com.suman.sharecare.auth.security;

import com.suman.sharecare.auth.entity.User;
import com.suman.sharecare.auth.enums.ErrorCode;
import com.suman.sharecare.auth.exception.EmailVerificationException;
import com.suman.sharecare.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        if(!user.isEmailVerified()) {
            throw new EmailVerificationException(ErrorCode.EMAIL_NOT_VERIFIED.name(), "Email must be verified first");
        }
        return user;
    }
}
