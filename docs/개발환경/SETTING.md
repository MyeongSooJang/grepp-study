# 프로젝트 시작 과정 정리

이 문서는 프로젝트를 처음 만들고, Java 21까지 맞춘 과정을 순서대로 정리한 기록입니다.

## 1. start.spring.io에서 프로젝트 만들기

1. 브라우저에서 `https://start.spring.io`에 접속합니다.
2. 화면에서 프로젝트 옵션을 선택합니다.
    - Project: `Gradle - Groovy`
    - Language: `Java`
    - Spring Boot: 21선택
3. 프로젝트 이름 관련 정보를 입력합니다.
    - Group, Artifact, Name, Package Name
4. 필요한 의존성(Dependencies)을 추가합니다.
5. `GENERATE` 버튼을 눌러 프로젝트 압축 파일을 다운로드합니다.
6. 다운로드한 압축 파일을 원하는 위치에 풉니다.

## 2. IDE에서 프로젝트 열기

1. IntelliJ 같은 IDE를 실행합니다.
2. 방금 압축 해제한 프로젝트 폴더를 엽니다.
3. IDE가 Gradle 프로젝트를 자동으로 인식하고 동기화(Sync)하는지 확인합니다.

## 3. JDK 21 설치하고 프로젝트에 연결하기

1. JDK 21을 설치합니다.
2. IDE에서 프로젝트가 사용할 Java 버전을 `21`로 설정합니다.
    - IntelliJ 기준: `Project Structure` → `Project SDK` → `JDK 21`
3. Gradle이 사용하는 JVM도 `JDK 21`로 설정합니다.
4. 프로젝트를 실행하거나 빌드해서 Java 21이 적용되었는지 확인합니다.

## 현재 완료된 내용

- start.spring.io에서 만든 프로젝트를 다운로드하고 로컬에서 열었습니다.
- 프로젝트 SDK와 Gradle JVM을 모두 JDK 21로 맞췄습니다.
