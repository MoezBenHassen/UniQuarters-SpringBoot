package esprit.tn.springdemo.authentication;

import esprit.tn.springdemo.config.JwtService;
import esprit.tn.springdemo.entities.Etudiant;
import esprit.tn.springdemo.entities.User;
import esprit.tn.springdemo.services.IEtudiantService;
import esprit.tn.springdemo.services.IUserService;
import esprit.tn.springdemo.token.Token;
import esprit.tn.springdemo.token.TokenRepository;
import esprit.tn.springdemo.token.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final IUserService service;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IEtudiantService etudiantService;

    public Etudiant register(Etudiant request) {
        var user = User.builder()
                .email(request.getUser().getEmail())
                .password(request.getUser().getPassword())
                .role(request.getUser().getRole())
                .enabled(request.getUser().isEnabled())
                .build();
        request.setUser(user);
        return etudiantService.addEtudiant(request);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    ));
        } catch (BadCredentialsException e) {
            log.info(e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }
        User user = null;
        try {
            user = service.findUserByEmail(request.getEmail());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public String refreshToken(String refreshToken) throws ChangeSetPersister.NotFoundException {
        final String userEmail;
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = null;
            try {
                user = this.service.findUserByEmail(userEmail);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                return accessToken;
            }
        }
        throw new ChangeSetPersister.NotFoundException();
    }
}