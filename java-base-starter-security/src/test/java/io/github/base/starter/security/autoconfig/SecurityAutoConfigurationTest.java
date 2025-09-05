package io.github.base.starter.security.autoconfig;

import io.github.base.security.api.AuthenticationManager;
import io.github.base.security.api.AuthorizationChecker;
import io.github.base.security.api.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SecurityAutoConfiguration.class))
            .withPropertyValues(
                    "base.security.type=jwt",
                    "base.security.jwt.secret=test-secret-key-that-is-long-enough-for-hs256",
                    "base.security.jwt.expiration=PT1H"
            );

    @Test
    void shouldAutoConfigureJwtComponents() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(TokenService.class);
            assertThat(context).hasSingleBean(AuthenticationManager.class);
            assertThat(context).hasSingleBean(AuthorizationChecker.class);
            assertThat(context).hasSingleBean(SecurityProperties.class);
        });
    }

    @Test
    void shouldNotAutoConfigureWhenDisabled() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(SecurityAutoConfiguration.class))
                .withPropertyValues("base.security.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(TokenService.class);
                    assertThat(context).doesNotHaveBean(AuthenticationManager.class);
                    assertThat(context).doesNotHaveBean(AuthorizationChecker.class);
                });
    }

    @Test
    void shouldNotAutoConfigureJwtWhenDisabled() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(SecurityAutoConfiguration.class))
                .withPropertyValues(
                        "base.security.type=jwt",
                        "base.security.config.jwt-enabled=false"
                )
                .run(context -> {
                    assertThat(context).doesNotHaveBean(TokenService.class);
                    assertThat(context).doesNotHaveBean(AuthenticationManager.class);
                    assertThat(context).doesNotHaveBean(AuthorizationChecker.class);
                });
    }

    @Test
    void shouldUseCustomProperties() {
        contextRunner
                .withPropertyValues(
                        "base.security.jwt.secret=custom-secret------------------------------------------dfsdf",
                        "base.security.jwt.expiration=PT2H",
                        "base.security.jwt.issuer=custom-issuer",
                        "base.security.jwt.audience=custom-audience"
                )
                .run(context -> {
                    SecurityProperties properties = context.getBean(SecurityProperties.class);
                    assertThat(properties.getJwt().getSecret()).isEqualTo("custom-secret------------------------------------------dfsdf");
                    assertThat(properties.getJwt().getExpiration()).isEqualTo(java.time.Duration.ofHours(2));
                    assertThat(properties.getJwt().getIssuer()).isEqualTo("custom-issuer");
                    assertThat(properties.getJwt().getAudience()).isEqualTo("custom-audience");
                });
    }
}
