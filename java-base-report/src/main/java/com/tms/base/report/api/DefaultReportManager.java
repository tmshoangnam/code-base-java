package com.tms.base.report.api;

import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.StreamingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Default implementation of ReportManager.
 * Thread-safe registry for report generators.
 * 
 * @since 1.0.0
 */
public class DefaultReportManager implements ReportManager {
    
    private final Map<String, ReportGenerator<?>> generators = new ConcurrentHashMap<>();
    private final Map<String, AsyncReportGenerator<?>> asyncGenerators = new ConcurrentHashMap<>();
    private final Map<String, StreamingReportGenerator<?>> streamingGenerators = new ConcurrentHashMap<>();
    private final Map<ReportFormat, String> formatToGenerator = new ConcurrentHashMap<>();
    private final Map<ReportFormat, String> formatToAsyncGenerator = new ConcurrentHashMap<>();
    private final Map<ReportFormat, String> formatToStreamingGenerator = new ConcurrentHashMap<>();
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> ReportGenerator<T> getGenerator(ReportFormat format) throws ReportException {
        lock.readLock().lock();
        try {
            String generatorName = formatToGenerator.get(format);
            if (generatorName == null) {
                throw new ReportException("No generator found for format: " + format);
            }
            ReportGenerator<?> generator = generators.get(generatorName);
            if (generator == null) {
                throw new ReportException("Generator not found: " + generatorName);
            }
            return (ReportGenerator<T>) generator;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> AsyncReportGenerator<T> getAsyncGenerator(ReportFormat format) throws ReportException {
        lock.readLock().lock();
        try {
            String generatorName = formatToAsyncGenerator.get(format);
            if (generatorName == null) {
                throw new ReportException("No async generator found for format: " + format);
            }
            AsyncReportGenerator<?> generator = asyncGenerators.get(generatorName);
            if (generator == null) {
                throw new ReportException("Async generator not found: " + generatorName);
            }
            return (AsyncReportGenerator<T>) generator;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> StreamingReportGenerator<T> getStreamingGenerator(ReportFormat format) throws ReportException {
        lock.readLock().lock();
        try {
            String generatorName = formatToStreamingGenerator.get(format);
            if (generatorName == null) {
                throw new ReportException("No streaming generator found for format: " + format);
            }
            StreamingReportGenerator<?> generator = streamingGenerators.get(generatorName);
            if (generator == null) {
                throw new ReportException("Streaming generator not found: " + generatorName);
            }
            return (StreamingReportGenerator<T>) generator;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> ReportGenerator<T> getGenerator(String name) throws ReportException {
        lock.readLock().lock();
        try {
            ReportGenerator<?> generator = generators.get(name);
            if (generator == null) {
                throw new ReportException("Generator not found: " + name);
            }
            return (ReportGenerator<T>) generator;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> AsyncReportGenerator<T> getAsyncGenerator(String name) throws ReportException {
        lock.readLock().lock();
        try {
            AsyncReportGenerator<?> generator = asyncGenerators.get(name);
            if (generator == null) {
                throw new ReportException("Async generator not found: " + name);
            }
            return (AsyncReportGenerator<T>) generator;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> StreamingReportGenerator<T> getStreamingGenerator(String name) throws ReportException {
        lock.readLock().lock();
        try {
            StreamingReportGenerator<?> generator = streamingGenerators.get(name);
            if (generator == null) {
                throw new ReportException("Streaming generator not found: " + name);
            }
            return (StreamingReportGenerator<T>) generator;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public Map<String, ReportGenerator<?>> getAllGenerators() {
        lock.readLock().lock();
        try {
            return new ConcurrentHashMap<>(generators);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public Map<String, AsyncReportGenerator<?>> getAllAsyncGenerators() {
        lock.readLock().lock();
        try {
            return new ConcurrentHashMap<>(asyncGenerators);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public Map<String, StreamingReportGenerator<?>> getAllStreamingGenerators() {
        lock.readLock().lock();
        try {
            return new ConcurrentHashMap<>(streamingGenerators);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public void registerGenerator(ReportGenerator<?> generator) {
        lock.writeLock().lock();
        try {
            String name = generator.getName();
            generators.put(name, generator);
            
            // Update format mapping
            for (ReportFormat format : generator.getSupportedFormats()) {
                formatToGenerator.put(format, name);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public void registerAsyncGenerator(AsyncReportGenerator<?> generator) {
        lock.writeLock().lock();
        try {
            String name = generator.getName();
            asyncGenerators.put(name, generator);
            
            // Update format mapping
            for (ReportFormat format : generator.getSupportedFormats()) {
                formatToAsyncGenerator.put(format, name);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public void registerStreamingGenerator(StreamingReportGenerator<?> generator) {
        lock.writeLock().lock();
        try {
            String name = generator.getName();
            streamingGenerators.put(name, generator);
            
            // Update format mapping
            for (ReportFormat format : generator.getSupportedFormats()) {
                formatToStreamingGenerator.put(format, name);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public void unregisterGenerator(String name) {
        lock.writeLock().lock();
        try {
            ReportGenerator<?> generator = generators.remove(name);
            if (generator != null) {
                // Remove format mappings
                for (ReportFormat format : generator.getSupportedFormats()) {
                    if (name.equals(formatToGenerator.get(format))) {
                        formatToGenerator.remove(format);
                    }
                }
            }
            
            AsyncReportGenerator<?> asyncGenerator = asyncGenerators.remove(name);
            if (asyncGenerator != null) {
                // Remove format mappings
                for (ReportFormat format : asyncGenerator.getSupportedFormats()) {
                    if (name.equals(formatToAsyncGenerator.get(format))) {
                        formatToAsyncGenerator.remove(format);
                    }
                }
            }
            
            StreamingReportGenerator<?> streamingGenerator = streamingGenerators.remove(name);
            if (streamingGenerator != null) {
                // Remove format mappings
                for (ReportFormat format : streamingGenerator.getSupportedFormats()) {
                    if (name.equals(formatToStreamingGenerator.get(format))) {
                        formatToStreamingGenerator.remove(format);
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean isStreamingSupported(ReportFormat format) {
        lock.readLock().lock();
        try {
            return formatToStreamingGenerator.containsKey(format);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public StreamingMode[] getSupportedStreamingModes(ReportFormat format) {
        lock.readLock().lock();
        try {
            String generatorName = formatToStreamingGenerator.get(format);
            if (generatorName == null) {
                return new StreamingMode[0];
            }
            StreamingReportGenerator<?> generator = streamingGenerators.get(generatorName);
            if (generator == null) {
                return new StreamingMode[0];
            }
            return generator.getSupportedStreamingModes();
        } finally {
            lock.readLock().unlock();
        }
    }
}
