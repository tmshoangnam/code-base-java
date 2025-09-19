package io.github.base.core.qrcode.model;

import java.util.Objects;

/**
 * Content model for QR Code data.
 * 
 * <p>This class provides structured content types for QR Code generation,
 * supporting various standard formats like URLs, vCards, WiFi credentials, etc.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // URL content
 * QrCodeContent urlContent = QrCodeContent.url("https://example.com");
 * 
 * // vCard content
 * QrCodeContent vCardContent = QrCodeContent.vCard("John Doe", "john@example.com", "123-456-7890");
 * 
 * // WiFi content
 * QrCodeContent wifiContent = QrCodeContent.wifi("MyNetwork", "WPA", "password123");
 * }</pre>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public final class QrCodeContent {
    
    private final ContentType type;
    private final String content;
    private final String description;
    
    private QrCodeContent(ContentType type, String content, String description) {
        this.type = type;
        this.content = content;
        this.description = description;
    }
    
    /**
     * Gets the type of content.
     * 
     * @return the content type
     */
    public ContentType getType() {
        return type;
    }
    
    /**
     * Gets the raw content string.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Gets the human-readable description of the content.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QrCodeContent that = (QrCodeContent) o;
        return type == that.type &&
               Objects.equals(content, that.content) &&
               Objects.equals(description, that.description);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, content, description);
    }
    
    @Override
    public String toString() {
        return "QrCodeContent{" +
               "type=" + type +
               ", content='" + content + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
    
    /**
     * Creates a URL content.
     * 
     * @param url the URL
     * @return the content
     * @throws IllegalArgumentException if url is null or empty
     */
    public static QrCodeContent url(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        return new QrCodeContent(ContentType.URL, url, "URL: " + url);
    }
    
    /**
     * Creates a plain text content.
     * 
     * @param text the text
     * @return the content
     * @throws IllegalArgumentException if text is null or empty
     */
    public static QrCodeContent text(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
        return new QrCodeContent(ContentType.TEXT, text, "Text: " + text);
    }
    
    /**
     * Creates a vCard content.
     * 
     * @param name the contact name
     * @param email the email address
     * @param phone the phone number
     * @return the content
     * @throws IllegalArgumentException if name is null or empty
     */
    public static QrCodeContent vCard(String name, String email, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        StringBuilder vCard = new StringBuilder();
        vCard.append("BEGIN:VCARD\n");
        vCard.append("VERSION:3.0\n");
        vCard.append("FN:").append(name).append("\n");
        
        if (email != null && !email.trim().isEmpty()) {
            vCard.append("EMAIL:").append(email).append("\n");
        }
        
        if (phone != null && !phone.trim().isEmpty()) {
            vCard.append("TEL:").append(phone).append("\n");
        }
        
        vCard.append("END:VCARD");
        
        return new QrCodeContent(ContentType.VCARD, vCard.toString(), "vCard: " + name);
    }
    
    /**
     * Creates a WiFi content.
     * 
     * @param ssid the network SSID
     * @param security the security type (WPA, WEP, nopass)
     * @param password the password
     * @return the content
     * @throws IllegalArgumentException if ssid is null or empty
     */
    public static QrCodeContent wifi(String ssid, String security, String password) {
        if (ssid == null || ssid.trim().isEmpty()) {
            throw new IllegalArgumentException("SSID cannot be null or empty");
        }
        
        StringBuilder wifi = new StringBuilder();
        wifi.append("WIFI:T:").append(security != null ? security : "nopass");
        wifi.append(";S:").append(ssid);
        
        if (password != null && !password.trim().isEmpty()) {
            wifi.append(";P:").append(password);
        }
        
        wifi.append(";;");
        
        return new QrCodeContent(ContentType.WIFI, wifi.toString(), "WiFi: " + ssid);
    }
    
    /**
     * Creates a custom content.
     * 
     * @param content the custom content
     * @param description the description
     * @return the content
     * @throws IllegalArgumentException if content is null or empty
     */
    public static QrCodeContent custom(String content, String description) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        return new QrCodeContent(ContentType.CUSTOM, content, description != null ? description : "Custom content");
    }
    
    /**
     * Content types supported by QR Code.
     */
    public enum ContentType {
        /**
         * Plain text content.
         */
        TEXT("text/plain"),
        
        /**
         * URL content.
         */
        URL("text/uri-list"),
        
        /**
         * vCard contact information.
         */
        VCARD("text/vcard"),
        
        /**
         * WiFi network credentials.
         */
        WIFI("application/x-wifi-config"),
        
        /**
         * Custom content.
         */
        CUSTOM("application/octet-stream");
        
        private final String mimeType;
        
        ContentType(String mimeType) {
            this.mimeType = mimeType;
        }
        
        /**
         * Gets the MIME type for this content type.
         * 
         * @return the MIME type
         */
        public String getMimeType() {
            return mimeType;
        }
    }
}
