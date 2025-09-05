package io.github.base.cache.api;

import java.util.Arrays;
import java.util.Objects;

/**
 * Default implementation of KeyGenerator that produces colon-separated keys.
 * <p>
 * This implementation creates cache keys by joining string representations of
 * the input parameters with colons as separators.
 * <p>
 * <strong>Examples:</strong>
 * <ul>
 *   <li>{@code generate("user", 123, "orders")} → {@code "user:123:orders"}</li>
 *   <li>{@code generate("product", "ABC-123")} → {@code "product:ABC-123"}</li>
 *   <li>{@code generate("config")} → {@code "config"}</li>
 * </ul>
 * <p>
 * <strong>Null Handling:</strong> Null parameters are converted to the string "null".
 * <p>
 * <strong>Thread Safety:</strong> This class is thread-safe and stateless.
 *
 * @since 1.0.0
 */
public final class SimpleKeyGenerator implements KeyGenerator {

    /**
     * The default separator used to join key components.
     */
    public static final String DEFAULT_SEPARATOR = ":";

    private final String separator;

    /**
     * Creates a new SimpleKeyGenerator with the default separator (":").
     */
    public SimpleKeyGenerator() {
        this(DEFAULT_SEPARATOR);
    }

    /**
     * Creates a new SimpleKeyGenerator with the specified separator.
     *
     * @param separator the separator to use when joining key components
     * @throws NullPointerException if separator is null
     */
    public SimpleKeyGenerator(String separator) {
        this.separator = Objects.requireNonNull(separator, "Separator cannot be null");
    }

    @Override
    public String generate(Object... params) {
        if (params == null || params.length == 0) {
            return "";
        }

        return Arrays.stream(params)
                .map(param -> param == null ? "null" : param.toString())
                .reduce((a, b) -> a + separator + b)
                .orElse("");
    }
}
