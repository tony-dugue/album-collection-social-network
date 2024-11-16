package com.tonydugue.album_collection.auth;

import com.tonydugue.album_collection.role.RoleRepository;
import com.tonydugue.album_collection.user.Token;
import com.tonydugue.album_collection.user.TokenRepository;
import com.tonydugue.album_collection.user.User;
import com.tonydugue.album_collection.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;

  public void register(RegistrationRequest request) {
    var userRole = roleRepository.findByName("User")
            // todo - better exception handling
            .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));

    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .accountLocked(false)
            .enabled(false)
            .roles(List.of(userRole))
            .build();

    userRepository.save(user);
    sendValidationEmail(user);
  }

  private void sendValidationEmail(User user) {
    // 1) generate new token
    var newToken = generateAndSaveActivationToken(user);
    // 2) send email
  }

  private String generateAndSaveActivationToken(User user) {
    // generate a token
    String generatedToken = generateActivationCode(6);

    var token = Token.builder()
            .token(generatedToken)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .user(user)
            .build();
    tokenRepository.save(token);
    return generatedToken;
  }

  private String generateActivationCode(int length) {
    String characters = "0123456789";
    StringBuilder codeBuilder = new StringBuilder();
    SecureRandom secureRandom = new SecureRandom();

    for (int i = 0; i < length; i++) {
      int randomIndex = secureRandom.nextInt(characters.length());
      codeBuilder.append(characters.charAt(randomIndex));
    }
    return codeBuilder.toString();
  }
}