/**
 * QR Code SPI (Service Provider Interface) package.
 * 
 * <p>This package contains the SPI interfaces and classes that allow different
 * QR Code implementations to be plugged into the system through the Java ServiceLoader mechanism.</p>
 * 
 * <p>Key components:</p>
 * <ul>
 *   <li>{@link io.github.base.core.qrcode.spi.QrCodeProvider} - Main SPI interface</li>
 *   <li>{@link io.github.base.core.qrcode.spi.QrCodeProviderException} - Provider-specific exceptions</li>
 * </ul>
 * 
 * <p>To register a new provider, create a file named
 * {@code io.github.base.core.qrcode.spi.QrCodeProvider} in the
 * {@code META-INF/services} directory and list your provider class name.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
package io.github.base.core.qrcode.spi;
