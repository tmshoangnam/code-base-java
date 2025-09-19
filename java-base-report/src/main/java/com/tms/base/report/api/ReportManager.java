package com.tms.base.report.api;

import com.tms.base.report.enums.ReportFormat;
import java.util.Map;

/**
 * Registry for report generators.
 * Thread-safe and supports multiple generators.
 *
 * @since 1.0.0
 */
public interface ReportManager {

    /**
     * Get generator by format.
     *
     * @param format Report format
     * @return Report generator
     * @throws ReportException if generator not found
     */
    <T> ReportGenerator<T> getGenerator(ReportFormat format) throws ReportException;

    /**
     * Get async generator by format.
     *
     * @param format Report format
     * @return Async report generator
     * @throws ReportException if generator not found
     */
    <T> AsyncReportGenerator<T> getAsyncGenerator(ReportFormat format) throws ReportException;

    /**
     * Get streaming generator by format.
     *
     * @param format Report format
     * @return Streaming report generator
     * @throws ReportException if generator not found
     */
    <T> StreamingReportGenerator<T> getStreamingGenerator(ReportFormat format) throws ReportException;

    /**
     * Get generator by name.
     *
     * @param name Generator name
     * @return Report generator
     * @throws ReportException if generator not found
     */
    <T> ReportGenerator<T> getGenerator(String name) throws ReportException;

    /**
     * Get async generator by name.
     *
     * @param name Generator name
     * @return Async report generator
     * @throws ReportException if generator not found
     */
    <T> AsyncReportGenerator<T> getAsyncGenerator(String name) throws ReportException;

    /**
     * Get streaming generator by name.
     *
     * @param name Generator name
     * @return Streaming report generator
     * @throws ReportException if generator not found
     */
    <T> StreamingReportGenerator<T> getStreamingGenerator(String name) throws ReportException;

    /**
     * Get all registered generators.
     *
     * @return Map of generator name to generator instance
     */
    Map<String, ReportGenerator<?>> getAllGenerators();

    /**
     * Get all registered async generators.
     *
     * @return Map of generator name to async generator instance
     */
    Map<String, AsyncReportGenerator<?>> getAllAsyncGenerators();

    /**
     * Get all registered streaming generators.
     *
     * @return Map of generator name to streaming generator instance
     */
    Map<String, StreamingReportGenerator<?>> getAllStreamingGenerators();

    /**
     * Register a generator.
     *
     * @param generator Report generator
     */
    void registerGenerator(ReportGenerator<?> generator);

    /**
     * Register an async generator.
     *
     * @param generator Async report generator
     */
    void registerAsyncGenerator(AsyncReportGenerator<?> generator);

    /**
     * Register a streaming generator.
     *
     * @param generator Streaming report generator
     */
    void registerStreamingGenerator(StreamingReportGenerator<?> generator);

    /**
     * Unregister a generator.
     *
     * @param name Generator name
     */
    void unregisterGenerator(String name);

    /**
     * Check if streaming is supported for format.
     *
     * @param format Report format
     * @return true if streaming is supported
     */
    boolean isStreamingSupported(ReportFormat format);

    /**
     * Get supported streaming modes for format.
     *
     * @param format Report format
     * @return Array of supported streaming modes
     */
    com.tms.base.report.enums.StreamingMode[] getSupportedStreamingModes(ReportFormat format);
}
