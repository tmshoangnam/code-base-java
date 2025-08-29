# CI/CD Pipeline Documentation

## Tổng quan

Dự án này sử dụng GitHub Actions để tự động hóa quá trình build, test, quality check và deployment. Pipeline được thiết kế để đảm bảo chất lượng code và tự động hóa quy trình phát triển.

## Workflows

### 1. Main CI/CD Pipeline (`ci-cd.yml`)

**Trigger:** Push to main/develop, Pull Request, Release

**Jobs:**
- **Build and Test**: Compile, test, build artifacts
- **Quality Check**: Code style, coverage, SonarQube analysis
- **Security Scan**: OWASP dependency check
- **Deploy**: Deploy to Nexus (release only)
- **Notify**: Team notifications

### 2. Pull Request Check (`pr-check.yml`)

**Trigger:** Pull Request to main/develop

**Features:**
- Fast feedback loop
- Basic quality checks
- Coverage reporting

### 3. Nightly Build (`nightly-build.yml`)

**Trigger:** Daily at 2 AM UTC, manual trigger

**Features:**
- Comprehensive testing
- Full report generation
- Failure notifications

## Cấu hình

### Environment Variables

Cần thiết lập các secrets sau trong GitHub repository:

```bash
# SonarQube
SONAR_TOKEN=your_sonar_token
SONAR_HOST_URL=https://sonar.company.com

# Nexus Repository
NEXUS_USERNAME=your_nexus_username
NEXUS_PASSWORD=your_nexus_password

# Slack Notifications (Optional)
SLACK_WEBHOOK_URL=your_slack_webhook_url
```

### Code Quality Tools

#### Checkstyle
- File: `checkstyle.xml`
- Enforces coding standards
- Configurable rules for Java code

#### SpotBugs
- File: `spotbugs-exclude.xml`
- Static analysis for potential bugs
- Excludes false positives

#### JaCoCo
- Test coverage reporting
- Minimum 80% line coverage required
- Excludes generated/config classes

#### OWASP Dependency Check
- Security vulnerability scanning
- Checks for known CVEs
- Generates HTML reports

## Sử dụng

### Local Development

```bash
# Validate POM files
mvn validate

# Compile
mvn clean compile

# Run tests
mvn test

# Run integration tests
mvn verify

# Check code style
mvn checkstyle:check

# Run security scan
mvn dependency-check:check

# Generate coverage report
mvn jacoco:report
```

### Pull Request Process

1. Tạo feature branch từ `develop`
2. Commit và push code
3. Tạo Pull Request
4. CI/CD pipeline tự động chạy
5. Review kết quả và fix issues nếu cần
6. Merge sau khi tất cả checks pass

### Release Process

1. Tạo release tag
2. CI/CD pipeline tự động deploy
3. Artifacts được upload lên Nexus
4. GitHub Release được tạo tự động

## Monitoring và Troubleshooting

### Pipeline Status

- Kiểm tra Actions tab trong GitHub repository
- Monitor job status và logs
- Review artifacts và reports

### Common Issues

#### Build Failures
- Kiểm tra compilation errors
- Verify test failures
- Check dependency conflicts

#### Quality Check Failures
- Review Checkstyle violations
- Fix SpotBugs warnings
- Improve test coverage

#### Security Issues
- Update vulnerable dependencies
- Review OWASP reports
- Address security findings

### Performance Optimization

- Cache Maven dependencies
- Parallel job execution
- Optimize test execution time

## Best Practices

### Code Quality
- Fix all Checkstyle violations
- Address SpotBugs warnings
- Maintain high test coverage
- Follow naming conventions

### Security
- Regular dependency updates
- Security scan integration
- Vulnerability monitoring
- Secure coding practices

### Testing
- Unit test coverage > 80%
- Integration test coverage
- Performance testing
- Security testing

## Tùy chỉnh

### Thêm Quality Checks

1. Cập nhật workflow file
2. Thêm Maven plugin configuration
3. Cập nhật exclusion files nếu cần

### Thay đổi Deployment

1. Cập nhật Nexus configuration
2. Modify deployment steps
3. Update environment variables

### Notification Channels

1. Thêm Slack/Teams integration
2. Email notifications
3. Custom webhook endpoints

## Support

- Review GitHub Actions logs
- Check Maven plugin documentation
- Consult team members
- Review CI/CD best practices
