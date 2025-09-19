/**
 * QR Code package.
 * 
 * <p>This package provides a framework-agnostic QR Code foundation for enterprise applications.
 * It follows the contract-first design principle and supports multiple implementations
 * through the Service Provider Interface (SPI) mechanism.</p>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Framework-agnostic design</li>
 *   <li>Contract-first architecture</li>
 *   <li>Pluggable implementations via SPI</li>
 *   <li>Multiple output formats (PNG, JPEG, SVG, Base64)</li>
 *   <li>Comprehensive error handling</li>
 *   <li>Production-ready with observability support</li>
 * </ul>
 * 
 * <p>Quick start:</p>
 * <pre>{@code
 * // Load default service
 * QrCodeService service = QrCodeServices.loadDefault();
 * 
 * // Create request
 * QrCodeRequest request = QrCodeRequest.builder()
 *     .content("https://example.com")
 *     .format(QrCodeFormat.PNG)
 *     .size(300)
 *     .build();
 * 
 * // Generate QR Code
 * QrCodeResult result = service.encode(request);
 * }</pre>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
package io.github.base.core.qrcode;
