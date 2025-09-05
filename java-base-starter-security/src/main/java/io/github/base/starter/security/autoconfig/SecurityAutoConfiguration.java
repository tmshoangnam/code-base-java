package io.github.base.starter.security.autoconfig;

import io.github.base.security.api.AuthenticationManager;
import io.github.base.security.api.AuthorizationChecker;
import io.github.base.security.api.TokenService;
import io.github.base.starter.security.jwt.JwtAuthenticationManager;
import io.github.base.starter.security.jwt.JwtAuthorizationChecker;
import io.github.base.starter.security.jwt.JwtSecurityProvider;
import io.github.base.starter.security.jwt.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for base security.
 */
@AutoConfiguration
@ConditionalOnClass({TokenService.class, AuthenticationManager.class, AuthorizationChecker.class})
@ConditionalOnProperty(prefix = "base.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAutoConfiguration.class);

    /**
     * JWT Token Service configuration.
     */
    @Configuration
    @ConditionalOnProperty(prefix = "base.security", name = "type", havingValue = "jwt", matchIfMissing = true)
    static class JwtConfiguration {

        @Configuration
        @ConditionalOnProperty(prefix = "base.security.config", name = "jwt-enabled", havingValue = "true", matchIfMissing = true)
        static class JwtEnabledConfiguration {

            @Bean
            @ConditionalOnMissingBean
            public JwtTokenService jwtTokenService(SecurityProperties securityProperties) {
                logger.info("Creating JWT token service");
                return new JwtTokenService(securityProperties.getJwt());
            }

            @Bean
            @ConditionalOnMissingBean
            public JwtAuthenticationManager jwtAuthenticationManager(JwtTokenService jwtTokenService,
                                                                     SecurityProperties securityProperties) {
                logger.info("Creating JWT authentication manager");
                return new JwtAuthenticationManager(jwtTokenService, securityProperties.getConfig());
            }

            @Bean
            @ConditionalOnMissingBean
            public JwtAuthorizationChecker jwtAuthorizationChecker() {
                logger.info("Creating JWT authorization checker");
                return new JwtAuthorizationChecker();
            }

            @Bean
            @ConditionalOnMissingBean
            public JwtSecurityProvider jwtSecurityProvider(JwtTokenService jwtTokenService,
                                                           SecurityProperties securityProperties) {
                logger.info("Creating JWT security provider");
                return new JwtSecurityProvider(securityProperties.getJwt(), securityProperties.getConfig());
            }
        }
    }

    /**
     * OAuth2 configuration.
     */
    @Configuration
    @ConditionalOnProperty(prefix = "base.security", name = "type", havingValue = "oauth2")
    static class OAuth2Configuration {

        @Configuration
        @ConditionalOnProperty(prefix = "base.security.config", name = "oauth2-enabled", havingValue = "true", matchIfMissing = true)
        static class OAuth2ConfigurationConfiguration {

            // OAuth2 configuration will be implemented later
            // For now, we'll just log that OAuth2 is not yet implemented
            static {
                logger.warn("OAuth2 configuration is not yet implemented");
            }

        }

    }

    /**
     * Default beans for when no specific provider is configured.
     */
    @Configuration
    @ConditionalOnMissingBean({TokenService.class, AuthenticationManager.class, AuthorizationChecker.class})
    static class DefaultConfiguration {

        static {
            logger.warn("OAuth2 configuration is not yet implemented");
        }
    }
}
