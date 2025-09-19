package io.github.base.core.qrcode;

import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.api.QrCodeException;
import io.github.base.core.qrcode.spi.QrCodeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for QrCodeServices utility class.
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("QrCodeServices Tests")
class QrCodeServicesTest {
    
    @Mock
    private QrCodeProvider mockProvider;
    
    @Mock
    private QrCodeService mockService;
    
    @BeforeEach
    void setUp() {
        when(mockProvider.getName()).thenReturn("test-provider");
        when(mockProvider.getVersion()).thenReturn("1.0.0");
        when(mockProvider.getDescription()).thenReturn("Test provider");
        when(mockProvider.isAvailable()).thenReturn(true);
        when(mockProvider.getPriority()).thenReturn(100);
        when(mockProvider.createService()).thenReturn(mockService);
    }
    
    @Nested
    @DisplayName("Provider Discovery Tests")
    class ProviderDiscoveryTests {
        
        @Test
        @DisplayName("Should get available providers")
        void shouldGetAvailableProviders() {
            // When
            var providers = QrCodeServices.getAvailableProviders();
            
            // Then
            assertThat(providers).isNotNull();
            // Note: In real tests, this would depend on actual providers loaded via ServiceLoader
        }
        
        @Test
        @DisplayName("Should get available providers details")
        void shouldGetAvailableProvidersDetails() {
            // When
            var providers = QrCodeServices.getAvailableProvidersDetails();
            
            // Then
            assertThat(providers).isNotNull();
            // Note: In real tests, this would depend on actual providers loaded via ServiceLoader
        }
    }
    
    @Nested
    @DisplayName("Cache Management Tests")
    class CacheManagementTests {
        
        @Test
        @DisplayName("Should clear cache successfully")
        void shouldClearCacheSuccessfully() {
            // When & Then
            assertThatCode(() -> QrCodeServices.clearCache())
                .doesNotThrowAnyException();
        }
        
        @Test
        @DisplayName("Should reload providers successfully")
        void shouldReloadProvidersSuccessfully() {
            // When & Then
            assertThatCode(() -> QrCodeServices.reload())
                .doesNotThrowAnyException();
        }
    }
    
    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("Should throw exception when provider name is null")
        void shouldThrowExceptionWhenProviderNameIsNull() {
            // When & Then
            assertThatThrownBy(() -> QrCodeServices.loadProvider(null))
                .isInstanceOf(QrCodeException.class)
                .hasMessageContaining("Provider name cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception when provider name is empty")
        void shouldThrowExceptionWhenProviderNameIsEmpty() {
            // When & Then
            assertThatThrownBy(() -> QrCodeServices.loadProvider("   "))
                .isInstanceOf(QrCodeException.class)
                .hasMessageContaining("Provider name cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception when provider not found")
        void shouldThrowExceptionWhenProviderNotFound() {
            // When & Then
            assertThatThrownBy(() -> QrCodeServices.loadProvider("non-existent-provider"))
                .isInstanceOf(QrCodeException.class)
                .hasMessageContaining("Provider not found");
        }
    }
    
    @Test
    @DisplayName("Should be utility class")
    void shouldBeUtilityClass() {
        // When & Then
        assertThatThrownBy(() -> {
            var constructor = QrCodeServices.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }).isInstanceOf(Exception.class);
    }
}
