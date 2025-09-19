package com.tms.base.report.internal;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.StreamingReportResponse;
import com.tms.base.report.dto.StreamingConfig;
import com.tms.base.report.api.StreamingReportGenerator;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Streaming report executor for large reports.
 *
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public class StreamingReportExecutor<T> {

    private final ExecutorService executorService;
    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    public StreamingReportExecutor() {
        this.executorService = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r, "streaming-report-" + System.currentTimeMillis());
            thread.setDaemon(true);
            return thread;
        });
    }

    /**
     * Execute streaming report generation.
     *
     * @param generator Streaming report generator
     * @param request Report request
     * @param config Streaming configuration
     * @return CompletableFuture with streaming response
     */
    public CompletableFuture<StreamingReportResponse> executeStreaming(
        StreamingReportGenerator<T> generator,
        ReportRequest<T> request,
        StreamingConfig config
    ) {
        if (shutdown.get()) {
            return CompletableFuture.failedFuture(
                new IllegalStateException("Executor is shutdown")
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return generator.generateStreaming(request, config);
            } catch (Exception e) {
                throw new RuntimeException("Streaming report generation failed", e);
            }
        }, executorService);
    }

    /**
     * Execute streaming report generation with progress callback.
     *
     * @param generator Streaming report generator
     * @param request Report request
     * @param config Streaming configuration
     * @param progressCallback Progress callback
     * @return CompletableFuture with streaming response
     */
    public CompletableFuture<StreamingReportResponse> executeStreamingWithProgress(
        StreamingReportGenerator<T> generator,
        ReportRequest<T> request,
        StreamingConfig config,
        Consumer<com.tms.base.report.dto.ReportProgress> progressCallback
    ) {
        if (shutdown.get()) {
            return CompletableFuture.failedFuture(
                new IllegalStateException("Executor is shutdown")
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return generator.generateStreamingWithProgress(request, config, progressCallback);
            } catch (Exception e) {
                throw new RuntimeException("Streaming report generation failed", e);
            }
        }, executorService);
    }

    /**
     * Create streaming input stream from data.
     *
     * @param data Data to stream
     * @param config Streaming configuration
     * @return Streaming input stream
     */
    public InputStream createStreamingInputStream(byte[] data, StreamingConfig config) {
        return new ChunkedInputStream(data, config.getChunkSize());
    }

    /**
     * Create streaming input stream with callbacks.
     *
     * @param data Data to stream
     * @param config Streaming configuration
     * @param chunkConsumer Chunk consumer callback
     * @param onComplete Completion callback
     * @param onError Error callback
     * @return Streaming input stream
     */
    public InputStream createStreamingInputStreamWithCallbacks(
        byte[] data,
        StreamingConfig config,
        Consumer<byte[]> chunkConsumer,
        Runnable onComplete,
        Runnable onError
    ) {
        return new ChunkedInputStreamWithCallbacks(
            data, config.getChunkSize(), chunkConsumer, onComplete, onError
        );
    }

    /**
     * Shutdown executor gracefully.
     */
    public void shutdown() {
        if (shutdown.compareAndSet(false, true)) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Chunked input stream implementation.
     */
    private static class ChunkedInputStream extends InputStream {
        private final byte[] data;
        private final int chunkSize;
        private int position = 0;

        public ChunkedInputStream(byte[] data, int chunkSize) {
            this.data = data;
            this.chunkSize = chunkSize;
        }

        @Override
        public int read() {
            if (position >= data.length) {
                return -1;
            }
            return data[position++] & 0xFF;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            if (position >= data.length) {
                return -1;
            }

            int bytesToRead = Math.min(len, Math.min(chunkSize, data.length - position));
            System.arraycopy(data, position, b, off, bytesToRead);
            position += bytesToRead;

            return bytesToRead;
        }
    }

    /**
     * Chunked input stream with callbacks.
     */
    private static class ChunkedInputStreamWithCallbacks extends ChunkedInputStream {
        private final Consumer<byte[]> chunkConsumer;
        private final Runnable onComplete;
        private final Runnable onError;
        private final byte[] buffer;
        private boolean completed = false;

        public ChunkedInputStreamWithCallbacks(
            byte[] data,
            int chunkSize,
            Consumer<byte[]> chunkConsumer,
            Runnable onComplete,
            Runnable onError
        ) {
            super(data, chunkSize);
            this.chunkConsumer = chunkConsumer;
            this.onComplete = onComplete;
            this.onError = onError;
            this.buffer = new byte[chunkSize];
        }

        @Override
        public int read(byte[] b, int off, int len) {
            try {
                int bytesRead = super.read(b, off, len);

                if (bytesRead > 0 && chunkConsumer != null) {
                    byte[] chunk = new byte[bytesRead];
                    System.arraycopy(b, off, chunk, 0, bytesRead);
                    chunkConsumer.accept(chunk);
                }

                if (bytesRead == -1 && !completed) {
                    completed = true;
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }

                return bytesRead;
            } catch (Exception e) {
                if (onError != null) {
                    onError.run();
                }
                throw e;
            }
        }
    }
}
