# CI/CD cho Base Spring Boot (Cách 2) – Kế hoạch triển khai chi tiết

> Mục tiêu: Tách **base framework** (logging, exception, security, config, utils, starter) thành **artifact** độc lập; publish lên **Nexus/Artifactory**; app business chỉ cần **add dependency**. Tài liệu này bao gồm: cấu trúc repo, POM/BOM mẫu, versioning, workflow CI/CD cho GitHub Actions/GitLab/Jenkins, cấu hình publish, scan bảo mật, và checklist release.

---

## 0) Tổng quan kiến trúc base

- Repo: **my-base/** (riêng với repo app business)
- Modules khuyến nghị (tách nhỏ để dùng linh hoạt):
  - `java-base-bom` (type=pom) – quản lý version tập trung
  - `java-base-core` – error handling, logging, common utils, config
  - `java-base-security` – Spring Security + JWT (không nhúng policy app-specific)
  - `java-base-cache` – cấu hình cache abstraction (Redis/Caffeine) tối thiểu
  - `java-base-starter` – Spring Boot starter import core + auto-config (spring.factories/spring-autoconfigure-metadata)
  - (tuỳ chọn) `java-base-observability` – actuator, micrometer, prometheus

```
my-base/
├─ pom.xml                  (parent aggregator)
├─ java-base-bom/             (dependencyManagement)
├─ java-base-core/
├─ java-base-security/
├─ java-base-cache/
├─ java-base-observability/   (optional)
└─ java-base-starter/
```

---

## 1) Parent POM & BOM

**1.1 Parent aggregator `pom.xml` (root)** – build & plugin config tối thiểu:

```xml
<!-- my-base/pom.xml -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.mycompany.base</groupId>
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

**1.2 BOM `java-base-bom/pom.xml`** – quản lý phiên bản tập trung:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.mycompany.base</groupId>
    <artifactId>java-base-parent</artifactId>
    <version>1.0.0</version>
  </parent>
  <artifactId>java-base-bom</artifactId>
  <packaging>pom</packaging>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring.boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
      <!-- lock version libs khác tại đây -->
      <dependency>
        <groupId>com.mycompany.base</groupId>
        <artifactId>java-base-core</artifactId>
        <version>${project.version}</version>
      </dependency>
      <dependency>
        <groupId>com.mycompany.base</groupId>
        <artifactId>java-base-security</artifactId>
        <version>${project.version}</version>
      </dependency>
      <dependency>
        <groupId>com.mycompany.base</groupId>
        <artifactId>java-base-cache</artifactId>
        <version>${project.version}</version>
      </dependency>
      <dependency>
        <groupId>com.mycompany.base</groupId>
        <artifactId>java-base-observability</artifactId>
        <version>${project.version}</version>
        <optional>true</optional>
      </dependency>
      <dependency>
        <groupId>com.mycompany.base</groupId>
        <artifactId>java-base-starter</artifactId>
        <version>${project.version}</version>
      </dependency>
    </dependencies>
  </dependencyManagement>
</project>
```

---

## 2) Module mẫu (core & starter)

**2.1 `java-base-core/pom.xml`**
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.mycompany.base</groupId>
    <artifactId>java-base-parent</artifactId>
    <version>1.0.0</version>
  </parent>
  <artifactId>java-base-core</artifactId>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-logging</artifactId>
    </dependency>
  </dependencies>
</project>
```

**2.2 `java-base-starter/pom.xml`** – để app chỉ cần add 1 starter:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.mycompany.base</groupId>
    <artifactId>java-base-parent</artifactId>
    <version>1.0.0</version>
  </parent>
  <artifactId>java-base-starter</artifactId>

  <dependencies>
    <dependency>
      <groupId>com.mycompany.base</groupId>
      <artifactId>java-base-core</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-autoconfigure</artifactId>
    </dependency>
  </dependencies>
</project>
```

**2.3 Auto-config** – trong `java-base-starter`:
```
src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```
Nội dung (ví dụ):
```
com.mycompany.base.autoconfig.CoreAutoConfiguration
com.mycompany.base.autoconfig.ExceptionHandlingAutoConfiguration
com.mycompany.base.autoconfig.LoggingAutoConfiguration
```

---

## 3) Versioning & Branching

- **Semantic Versioning**: `MAJOR.MINOR.PATCH` (breaking/minor/patch)
- **Branches**:
  - `main`: bảo vệ, chỉ merge qua PR
  - `release/*`: chuẩn bị release lớn nếu cần
  - `feature/*`, `fix/*`
- **Tag release**: `v1.2.0` (trigger publish release)
- **Commits**: theo Conventional Commits (`feat:`, `fix:`, `docs:`, `refactor:`…). Có thể dùng commitlint/husky.

---

## 4) CI/CD – GitHub Actions

> Sử dụng **3 workflow**: PR check, SNAPSHOT publish (push main), RELEASE publish (tag v*).

**4.1 `.github/workflows/pr-check.yml`** – build, test, unit, lint
```yaml
name: PR Check
on:
  pull_request:
    branches: [ main ]

jobs:
  build-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: 'maven'
      - name: Build & Test
        run: mvn -B -U -ntp clean verify
      - name: OWASP Dependency Check (optional)
        run: |
          mvn -B -ntp org.owasp:dependency-check-maven:check -DskipTests
      - name: Upload Test Report
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: surefire-reports
          path: '**/target/surefire-reports/*.xml'
```

**4.2 `.github/workflows/snapshot-publish.yml`** – auto publish SNAPSHOT khi push `main`
```yaml
name: Snapshot Publish
on:
  push:
    branches: [ main ]

jobs:
  snapshot:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: 'maven'
          server-id: nexus-snapshots
          server-username: MAVEN_USERNAME
          server-password: MAVEN_PASSWORD
      - name: Set SNAPSHOT version (optional)
        run: |
          mvn -B -ntp -q versions:set -DnewVersion=1.0.0
      - name: Deploy SNAPSHOT
        env:
          MAVEN_USERNAME: ${{ secrets.NEXUS_USER }}
          MAVEN_PASSWORD: ${{ secrets.NEXUS_PASS }}
        run: mvn -B -U -ntp -DskipTests deploy
```

**4.3 `.github/workflows/release-publish.yml`** – publish khi tag `v*.*.*`
```yaml
name: Release Publish
on:
  push:
    tags:
      - 'v*.*.*'

jobs:
  release:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: 'maven'
          server-id: nexus-releases
          server-username: MAVEN_USERNAME
          server-password: MAVEN_PASSWORD
      - name: Extract version from tag
        id: v
        run: echo "version=${GITHUB_REF_NAME#v}" >> $GITHUB_OUTPUT
      - name: Set release version
        run: mvn -B -ntp -q versions:set -DnewVersion=${{ steps.v.outputs.version }}
      - name: Deploy Release
        env:
          MAVEN_USERNAME: ${{ secrets.NEXUS_USER }}
          MAVEN_PASSWORD: ${{ secrets.NEXUS_PASS }}
        run: mvn -B -U -ntp -DskipTests deploy -Prelease
      - name: Generate & Upload Changelog (optional)
        uses: softprops/action-gh-release@v2
        with:
          tag_name: ${{ github.ref_name }}
          name: Release ${{ github.ref_name }}
          body: Auto-generated release notes
```

> **Lưu ý**: set secrets `NEXUS_USER`, `NEXUS_PASS`. Nếu SonarQube: thêm job sonar với token.

---

## 5) CI/CD – GitLab CI (tuỳ chọn)

**`.gitlab-ci.yml`**
```yaml
stages: [ verify, snapshot, release ]

image: maven:3.9-eclipse-temurin-17

cache:
  key: ${CI_COMMIT_REF_SLUG}
  paths:
    - .m2/repository

variables:
  MAVEN_CLI_OPTS: "-B -U -ntp"

verify:
  stage: verify
  script:
    - mvn $MAVEN_CLI_OPTS clean verify
  artifacts:
    when: always
    paths:
      - **/target/surefire-reports/*.xml

snapshot:
  stage: snapshot
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
  script:
    - mvn $MAVEN_CLI_OPTS -DskipTests deploy
  dependencies: [ verify ]

release:
  stage: release
  rules:
    - if: '$CI_COMMIT_TAG =~ /^v\d+\.\d+\.\d+$/'
  script:
    - VERSION=${CI_COMMIT_TAG#v}
    - mvn -q versions:set -DnewVersion=$VERSION
    - mvn $MAVEN_CLI_OPTS -DskipTests deploy -Prelease
```

---

## 6) CI/CD – Jenkins (tuỳ chọn)

**`Jenkinsfile`**
```groovy
pipeline {
  agent any
  environment {
    MAVEN_OPTS = '-DskipTests'
    MVN = 'mvn -B -U -ntp'
  }
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Build & Test') { steps { sh "${MVN} clean verify" } }
    stage('Publish') {
      when {
        anyOf { branch 'main'; buildingTag() }
      }
      steps {
        script {
          if (env.TAG_NAME) {
            def version = env.TAG_NAME.startsWith('v') ? env.TAG_NAME.substring(1) : env.TAG_NAME
            sh "${MVN} -q versions:set -DnewVersion=${version}"
            sh "${MVN} deploy -Prelease ${MAVEN_OPTS}"
          } else {
            sh "${MVN} deploy ${MAVEN_OPTS}"
          }
        }
      }
    }
  }
  post {
    always { junit '**/target/surefire-reports/*.xml' }
  }
}
```

---

## 7) Cấu hình publish lên Nexus/Artifactory

**7.1 `distributionManagement`** – đã có trong parent POM (snapshots/releases)

**7.2 `settings.xml`** – cấu hình credentials (không commit vào repo; đưa vào secret của CI)

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>nexus-snapshots</id>
      <username>${env.MAVEN_USERNAME}</username>
      <password>${env.MAVEN_PASSWORD}</password>
    </server>
    <server>
      <id>nexus-releases</id>
      <username>${env.MAVEN_USERNAME}</username>
      <password>${env.MAVEN_PASSWORD}</password>
    </server>
  </servers>
</settings>
```

Trong GitHub Actions, dùng `actions/setup-java` với `server-id`/`username`/`password` như cấu hình ở workflow.

---

## 8) Chất lượng & bảo mật (khuyến nghị)

- **Static Analysis**: SonarQube scan trong PR → chặn merge khi Quality Gate fail
- **Dependency Scan**: OWASP Dependency-Check (CVE) – fail build nếu CVSS >= threshold
- **License Scan**: kiểm soát license không phù hợp (GPLv3, AGPL…) nếu policy hạn chế
- **SCA/Container** (nếu phát hành image cho demo): Trivy/Grype (tuỳ chọn)

**Sonar (GitHub Actions) mẫu:**
```yaml
- name: SonarQube Scan
  env:
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
  run: |
    mvn -B -ntp sonar:sonar \
      -Dsonar.projectKey=my-base \
      -Dsonar.host.url=https://sonarqube.mycorp.local \
      -Dsonar.login=$SONAR_TOKEN
```

---

## 9) Release flow chuẩn

1. PR vào `main` (đã pass test + scan) → auto deploy **SNAPSHOT**
2. Tạo tag **vX.Y.Z** → pipeline set version & deploy **RELEASE**
3. Tạo GitHub Release/GitLab Release với changelog
4. Gửi thông báo (#dev-infra / email) + cập nhật wiki

**Changelog**: dùng Conventional Commits → có thể auto-generate bằng `git-chglog`/`conventional-changelog`.

---

## 10) Sử dụng từ phía ứng dụng (consumer)

**10.1 Thêm repo** (nếu không dùng mirror maven central):
```xml
<repositories>
  <repository>
    <id>mycorp-releases</id>
    <url>https://nexus.mycorp.local/repository/maven-releases/</url>
  </repository>
  <repository>
    <id>mycorp-snapshots</id>
    <url>https://nexus.mycorp.local/repository/maven-snapshots/</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
  </repository>
</repositories>
```

**10.2 Import BOM**
```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.mycompany.base</groupId>
      <artifactId>java-base-bom</artifactId>
      <version>1.0.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

**10.3 Add starter**
```xml
<dependencies>
  <dependency>
    <groupId>com.mycompany.base</groupId>
    <artifactId>java-base-starter</artifactId>
  </dependency>
</dependencies>
```

---

## 11) Rollback & Compatibility

- Không xoá release khỏi repo Maven; phát hành **patch** nếu có lỗi nghiêm trọng
- Giữ **backward compatibility**; nếu breaking change → tăng **MAJOR** và ghi rõ migration guide
- Cho phép app pin version (không ép dùng latest)

---

## 12) Checklist triển khai hạ tầng

- [ ] Nexus/Artifactory đã có hosted repo `maven-releases` & `maven-snapshots`
- [ ] Service account & quyền `deploy` (ID: `nexus-releases`, `nexus-snapshots`)
- [ ] Secrets đã được cấu hình trong CI (`NEXUS_USER`, `NEXUS_PASS`, `SONAR_TOKEN`)
- [ ] Branch protection trên `main` (require PR, checks pass)
- [ ] Temurin JDK 17 trong runners
- [ ] Quy ước Conventional Commits & pre-commit hook/commitlint (tuỳ chọn)

---

## 13) Phần mở rộng (tuỳ chọn nên có)

- **Release automation**: dùng `maven-release-plugin` hoặc action tự sinh version từ commit message
- **SBOM**: CycloneDX Maven plugin để tạo SBOM (yêu cầu audit)
- **Docs**: publish Javadoc của base lên internal site (GitHub Pages/Pages on GitLab)
- **Samples**: repo `my-base-examples` để minh họa cách dùng starter

---

## 14) Rủi ro & biện pháp

- *Rủi ro*: Coupling chính sách bảo mật vào base → *Biện pháp*: để policy ở app, base chỉ cung cấp hook/filters
- *Rủi ro*: Breaking changes khó kiểm soát → *Biện pháp*: semantic version + deprecate trước 1-2 minor
- *Rủi ro*: CI publish nhầm credentials → *Biện pháp*: dùng secrets; hạn chế permission; không commit `settings.xml`

---

**Done.** Tài liệu này có thể copy thẳng vào repo `my-base` (README.md + sample YAML/XML).

