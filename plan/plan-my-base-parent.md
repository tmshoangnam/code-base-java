## 1. Mục tiêu (Objective)

- Xây dựng parent aggregator `java-base-parent` làm gốc multi-module Maven, thống nhất build, versioning, phát hành.
- Tối thiểu hoá cấu hình lặp lại, khoá plugin versions, định danh nơi publish (Nexus/Artifactory).
- Bảo đảm không xung đột giữa modules, dễ mở rộng CI/CD và quy trình release.

## 2. Kiến trúc/Thiết kế tổng quan (Overview)

- Kiểu: `pom` (aggregator/parent), không chứa mã runtime.
- Chức năng: tập trung `properties`, `pluginManagement`, `distributionManagement`, danh sách `<modules>`.
- Modules con chỉ kế thừa `parent`; `parent` không phụ thuộc ngược lại vào modules.

Sơ đồ phụ thuộc cấp cao:

```mermaid
graph TD
  A[java-base-parent (pom)] --> B[java-base-bom (pom)]
  A --> C[java-base-core (jar)]
  A --> D[java-base-security (jar)]
  A --> E[java-base-cache (jar)]
  A --> F[java-base-observability (jar, optional)]
  A --> G[java-base-starter (jar)]
```

## 3. Các bước setup chi tiết (Step-by-step Setup)

1) Khởi tạo `pom.xml` ở root với packaging `pom` và danh sách modules.

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>io.github.tmshoangnam</groupId>
  <artifactId>java-base-parent</artifactId>
  <version>1.0.0</version>
  <packaging>pom</packaging>
  <modules>
    <module>java-base-bom</module>
    <module>java-base-core</module>
    <module>java-base-security</module>
    <module>java-base-cache</module>
    <module>java-base-observability</module>
    <module>java-base-starter</module>
  </modules>
  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <spring.boot.version>3.3.2</spring.boot.version>
  </properties>
  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-enforcer-plugin</artifactId>
          <version>3.4.1</version>
          <executions>
            <execution>
              <id>enforce</id>
              <goals><goal>enforce</goal></goals>
              <configuration>
                <rules>
                  <DependencyConvergence/>
                  <BanDuplicateClasses/>
                </rules>
                <fail>true</fail>
              </configuration>
            </execution>
          </executions>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>3.2.5</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-failsafe-plugin</artifactId>
          <version>3.2.5</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.4.2</version>
          <configuration>
            <archive>
              <manifestEntries>
                <Build-SourceEncoding>${project.build.sourceEncoding}</Build-SourceEncoding>
              </manifestEntries>
              <reproducible>true</reproducible>
            </archive>
          </configuration>
        </plugin>
        <plugin>
          <groupId>io.github.zlika</groupId>
          <artifactId>reproducible-build-maven-plugin</artifactId>
          <version>0.16</version>
          <executions>
            <execution>
              <goals><goal>strip-jar</goal></goals>
            </execution>
          </executions>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-deploy-plugin</artifactId>
          <version>3.1.1</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
  <distributionManagement>
    <snapshotRepository>
      <id>nexus-snapshots</id>
      <url>https://nexus.mycorp.local/repository/maven-snapshots/</url>
    </snapshotRepository>
    <repository>
      <id>nexus-releases</id>
      <url>https://nexus.mycorp.local/repository/maven-releases/</url>
    </repository>
  </distributionManagement>
</project>
```

2) Cấu hình `~/.m2/settings.xml` để deploy:

```xml
<settings>
  <servers>
    <server>
      <id>nexus-snapshots</id>
      <username>${env.NEXUS_USER}</username>
      <password>${env.NEXUS_PASS}</password>
    </server>
    <server>
      <id>nexus-releases</id>
      <username>${env.NEXUS_USER}</username>
      <password>${env.NEXUS_PASS}</password>
    </server>
  </servers>
</settings>
```

3) Thiết lập nhánh và versioning

- Nhánh phát triển: `main` (hoặc `develop`) dùng `-SNAPSHOT`.
- Release: tag `v1.0.0` và đồng bộ version cho toàn bộ modules.

## 4. Cấu hình (Configuration)

- Properties chung: JDK 17, Spring Boot version, encoding.
- PluginManagement: pin version surefire/failsafe/deploy; các module con chỉ kích hoạt khi cần.
- DistributionManagement: id trùng `settings.xml` để CI có thể deploy không tương tác.

Profiles tham khảo:

```xml
<profiles>
  <profile>
    <id>release</id>
    <build>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-gpg-plugin</artifactId>
          <version>3.2.4</version>
          <executions>
            <execution>
              <id>sign-artifacts</id>
              <phase>verify</phase>
              <goals><goal>sign</goal></goals>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
  </profile>
</profiles>
```

## 5. Cách kiểm thử/triển khai (Testing & Deployment)

- Kiểm thử: `mvn -B -U -ntp clean verify` tại root (chạy toàn bộ modules).
- Deploy snapshots: `mvn -B -U -ntp -DskipTests deploy` trên nhánh phát triển.
- Phát hành: cập nhật version non-SNAPSHOT, tag, `mvn -P release deploy`.

CI (GitHub Actions) ví dụ:

```yaml
name: CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven
      - name: Secret scan
        uses: gitleaks/gitleaks-action@v2
      - name: OWASP Dependency-Check
        uses: dependency-check/Dependency-Check_Action@v4
        with:
          project: 'my-base'
          path: '.'
          format: 'HTML'
      - name: Build
        run: mvn -B -U -ntp clean verify
  deploy-snapshot:
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven
          server-id: nexus-snapshots
          server-username: NEXUS_USER
          server-password: NEXUS_PASS
      - run: mvn -B -U -ntp -DskipTests deploy
        env:
          NEXUS_USER: ${{ secrets.NEXUS_USER }}
          NEXUS_PASS: ${{ secrets.NEXUS_PASS }}
```

## 6. Lưu ý mở rộng/Best practices (Scalability, Security, Performance)

- Scalability: tách modules độc lập; tránh phụ thuộc vòng; giữ kích thước artifacts nhỏ.
- Security: dùng CI secrets cho credentials; bật ký số trong profile release; SCAN SCA trước khi phát hành.
- Performance: bật Maven local/remote cache; dùng `-ntp` giảm noise; pin phiên bản plugin để build ổn định.

## 7. Tài liệu tham khảo (References)

- Spring Boot Docs: dependency management và auto-configuration.
- Maven Deploy/Release: `maven-deploy-plugin`, `maven-gpg-plugin`.
- Nexus/Artifactory Publishing Guides.
