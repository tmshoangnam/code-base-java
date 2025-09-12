# Java Base AWS Integration

[![Maven Central](https://img.shields.io/maven-central/v/io.github.tmshoangnam/java-base-aws.svg)](https://mvnrepository.com/artifact/io.github.tmshoangnam/java-base-aws)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/java-17%2B-orange.svg)](https://openjdk.java.net/)

A comprehensive AWS services integration library for Java applications, providing unified APIs for AWS services including S3, DynamoDB, Lambda, SNS, SQS, and Secrets Manager.

## 🚀 Features

- **Unified API**: Single interface for all AWS services
- **Service Abstraction**: Easy to add new AWS services
- **Configuration Management**: Centralized AWS configuration
- **Credential Management**: Secure credential handling
- **Region Support**: Full AWS region support
- **Service Discovery**: SPI-based service discovery
- **Error Handling**: Comprehensive AWS exception handling
- **Retry Logic**: Built-in retry mechanisms

## 📦 Installation

### Maven

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-aws</artifactId>
    <version>1.0.5-SNAPSHOT</version>
</dependency>
```

### Gradle

```gradle
implementation 'io.github.tmshoangnam:java-base-aws:1.0.5-SNAPSHOT'
```

## 🏗️ Architecture

### Core Components

- **AwsServices**: Main service class for AWS operations
- **AwsServiceConfig**: Configuration for AWS services
- **AwsCredentials**: Credential management
- **AwsRegion**: Region management
- **AwsClientConfig**: Client configuration
- **Service Providers**: SPI implementations for AWS services

### Supported AWS Services

- **S3**: Object storage operations
- **DynamoDB**: NoSQL database operations
- **Lambda**: Serverless function operations
- **SNS**: Notification service operations
- **SQS**: Message queue operations
- **Secrets Manager**: Secret management operations

### Package Structure

```
io.github.base.aws
├── api
│   ├── common/            # Common AWS components
│   ├── config/            # Configuration classes
│   ├── s3/               # S3 service APIs
│   ├── dynamodb/         # DynamoDB service APIs
│   ├── lambda/           # Lambda service APIs
│   ├── sns/              # SNS service APIs
│   ├── sqs/              # SQS service APIs
│   └── secrets/          # Secrets Manager APIs
├── spi/                  # Service Provider Interface
└── AwsServices          # Main service class
```

## 🔧 Usage

### Basic AWS Service Operations

```java
import io.github.base.aws.AwsServices;
import io.github.base.aws.api.config.AwsServiceConfig;
import io.github.base.aws.api.common.AwsCredentials;
import io.github.base.aws.api.common.AwsRegion;

// Get AWS service configuration
AwsServiceConfig serviceConfig = AwsServices.getServiceConfig("s3");

// Create credentials
AwsCredentials credentials = AwsCredentials.of(
    "AKIAIOSFODNN7EXAMPLE",
    "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
);

// Create region
AwsRegion region = AwsRegion.of("us-east-1");
```

### S3 Operations

```java
import io.github.base.aws.api.s3.S3Operations;
import io.github.base.aws.api.s3.S3UploadResult;
import io.github.base.aws.api.s3.S3DownloadResult;

// Get S3 operations
S3Operations s3Ops = AwsServices.getServiceConfig("s3").getS3Operations();

// Upload file to S3
S3UploadResult uploadResult = s3Ops.uploadObject(
    "my-bucket",
    "path/to/file.txt",
    "Hello World".getBytes(),
    "text/plain"
);

if (uploadResult.isSuccess()) {
    System.out.println("File uploaded: " + uploadResult.getLocation());
}

// Download file from S3
S3DownloadResult downloadResult = s3Ops.downloadObject(
    "my-bucket",
    "path/to/file.txt"
);

if (downloadResult.isSuccess()) {
    byte[] content = downloadResult.getContent();
    System.out.println("Downloaded content: " + new String(content));
}
```

### DynamoDB Operations

```java
import io.github.base.aws.api.dynamodb.DynamoDbOperations;
import io.github.base.aws.api.dynamodb.DynamoTableInfo;

// Get DynamoDB operations
DynamoDbOperations dynamoOps = AwsServices.getServiceConfig("dynamodb").getDynamoDbOperations();

// Create table
DynamoTableInfo tableInfo = DynamoTableInfo.builder()
    .tableName("my-table")
    .partitionKey("id")
    .sortKey("timestamp")
    .build();

boolean created = dynamoOps.createTable(tableInfo);

// Put item
Map<String, Object> item = Map.of(
    "id", "123",
    "name", "John Doe",
    "email", "john@example.com"
);

boolean put = dynamoOps.putItem("my-table", item);

// Get item
Map<String, Object> retrievedItem = dynamoOps.getItem("my-table", "id", "123");
```

### Lambda Operations

```java
import io.github.base.aws.api.lambda.LambdaOperations;
import io.github.base.aws.api.lambda.LambdaFunctionInfo;
import io.github.base.aws.api.lambda.LambdaInvokeResult;

// Get Lambda operations
LambdaOperations lambdaOps = AwsServices.getServiceConfig("lambda").getLambdaOperations();

// Create function
LambdaFunctionInfo functionInfo = LambdaFunctionInfo.builder()
    .functionName("my-function")
    .runtime("java17")
    .handler("com.example.Handler::handleRequest")
    .build();

boolean created = lambdaOps.createFunction(functionInfo);

// Invoke function
LambdaInvokeResult invokeResult = lambdaOps.invokeFunction(
    "my-function",
    "{\"key\": \"value\"}"
);

if (invokeResult.isSuccess()) {
    String response = invokeResult.getResponse();
    System.out.println("Function response: " + response);
}
```

### SNS Operations

```java
import io.github.base.aws.api.sns.SnsOperations;
import io.github.base.aws.api.sns.SnsTopicInfo;

// Get SNS operations
SnsOperations snsOps = AwsServices.getServiceConfig("sns").getSnsOperations();

// Create topic
SnsTopicInfo topicInfo = SnsTopicInfo.builder()
    .topicName("my-topic")
    .displayName("My Topic")
    .build();

String topicArn = snsOps.createTopic(topicInfo);

// Publish message
boolean published = snsOps.publishMessage(
    topicArn,
    "Hello from SNS",
    "Subject"
);
```

### SQS Operations

```java
import io.github.base.aws.api.sqs.SqsOperations;
import io.github.base.aws.api.sqs.SqsQueueInfo;
import io.github.base.aws.api.sqs.SqsMessage;

// Get SQS operations
SqsOperations sqsOps = AwsServices.getServiceConfig("sqs").getSqsOperations();

// Create queue
SqsQueueInfo queueInfo = SqsQueueInfo.builder()
    .queueName("my-queue")
    .visibilityTimeoutSeconds(30)
    .build();

String queueUrl = sqsOps.createQueue(queueInfo);

// Send message
SqsMessage message = SqsMessage.builder()
    .body("Hello from SQS")
    .build();

boolean sent = sqsOps.sendMessage(queueUrl, message);

// Receive messages
List<SqsMessage> messages = sqsOps.receiveMessages(queueUrl, 10);
```

### Secrets Manager Operations

```java
import io.github.base.aws.api.secrets.SecretsManagerOperations;
import io.github.base.aws.api.secrets.SecretsManagerSecretInfo;

// Get Secrets Manager operations
SecretsManagerOperations secretsOps = AwsServices.getServiceConfig("secrets").getSecretsManagerOperations();

// Create secret
SecretsManagerSecretInfo secretInfo = SecretsManagerSecretInfo.builder()
    .secretName("my-secret")
    .secretValue("my-secret-value")
    .description("My secret")
    .build();

boolean created = secretsOps.createSecret(secretInfo);

// Get secret value
String secretValue = secretsOps.getSecretValue("my-secret");
```

### Configuration Management

```java
import io.github.base.aws.api.common.AwsClientConfig;

// Create client configuration
AwsClientConfig clientConfig = AwsClientConfig.builder()
    .region("us-east-1")
    .credentials(credentials)
    .timeout(Duration.ofSeconds(30))
    .maxConnections(50)
    .retryMode(AwsClientConfig.RetryMode.ADAPTIVE)
    .addProperty("endpoint", "https://s3.amazonaws.com")
    .build();

// Use configuration
AwsServiceConfig serviceConfig = AwsServiceConfig.builder()
    .serviceName("s3")
    .clientConfig(clientConfig)
    .build();
```

### Error Handling

```java
import io.github.base.aws.api.common.AwsException;

try {
    S3Operations s3Ops = AwsServices.getServiceConfig("s3").getS3Operations();
    S3UploadResult result = s3Ops.uploadObject("bucket", "key", content, "text/plain");
    
    if (!result.isSuccess()) {
        System.err.println("Upload failed: " + result.getErrorMessage());
    }
} catch (AwsException e) {
    System.err.println("AWS error: " + e.getMessage());
    System.err.println("Error code: " + e.getErrorCode());
    System.err.println("Request ID: " + e.getRequestId());
}
```

## 🧪 Testing

The library includes comprehensive unit and integration tests with 80%+ code coverage.

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=AwsCredentialsTest
```

### Test Structure

- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test AWS service interactions
- **Mock Tests**: Test with mocked AWS services
- **Edge Case Tests**: Test boundary conditions and error scenarios

## 📊 Metrics and Monitoring

The library provides built-in metrics and monitoring capabilities:

- **Service Metrics**: Track AWS service operations
- **Performance Metrics**: Measure operation latency and throughput
- **Error Tracking**: Monitor failed operations and error rates
- **Cost Tracking**: Monitor AWS service costs
- **Cache Statistics**: Track cache hit rates and performance

## 🔒 Security

- **Credential Management**: Secure handling of AWS credentials
- **Encryption**: Support for encryption at rest and in transit
- **Access Control**: IAM-based access control
- **Audit Logging**: Comprehensive audit trail for all operations
- **Secret Management**: Secure secret storage and retrieval

## 🚀 Performance

- **Connection Pooling**: Efficient AWS service connections
- **Async Operations**: Support for asynchronous operations
- **Retry Logic**: Intelligent retry mechanisms
- **Caching**: Built-in caching for frequently accessed data
- **Batch Operations**: Support for batch operations

## 📝 Configuration

The library supports various configuration options:

```java
// Configure AWS properties
AwsProperties properties = AwsProperties.builder()
    .region("us-east-1")
    .credentials(credentials)
    .timeout(Duration.ofSeconds(30))
    .maxConnections(50)
    .retryMode(RetryMode.ADAPTIVE)
    .build();
```

## 🌍 Region Support

The library supports all AWS regions:

- **US East**: us-east-1, us-east-2
- **US West**: us-west-1, us-west-2
- **Europe**: eu-west-1, eu-west-2, eu-west-3, eu-central-1
- **Asia Pacific**: ap-southeast-1, ap-southeast-2, ap-northeast-1, ap-northeast-2
- **Other Regions**: ca-central-1, sa-east-1, af-south-1, ap-south-1

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Development Setup

1. Clone the repository
2. Install dependencies: `mvn install`
3. Run tests: `mvn test`
4. Build the project: `mvn package`

### Code Style

We follow the Google Java Style Guide with some customizations:
- 4 spaces for indentation
- Maximum line length: 120 characters
- Use `@DisplayName` for test methods
- Comprehensive Javadoc for public APIs

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [Wiki](https://github.com/tmshoangnam/java-base-aws/wiki)
- **Issues**: [GitHub Issues](https://github.com/tmshoangnam/java-base-aws/issues)
- **Discussions**: [GitHub Discussions](https://github.com/tmshoangnam/java-base-aws/discussions)

## 🔄 Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed list of changes.

## 🏷️ Versioning

We use [Semantic Versioning](https://semver.org/) for version numbers:
- **MAJOR**: Breaking changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes (backward compatible)

## 🙏 Acknowledgments

- AWS SDK for Java for reference implementation
- Spring Cloud AWS for design patterns
- AWS documentation for comprehensive examples
- The Java community for feedback and contributions

---

**Made with ❤️ by the Java Base Team**
