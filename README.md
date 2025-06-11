# SmokeZone

**SmokeZone**는 안드로이드 기반 위치기반 흡연 구역 탐색 및 사용자 기여형 커뮤니티 앱입니다.
사용자는 주변 흡연 공간을 지도에서 확인하고, 새로운 흡연 구역을 등록하거나 댓글을 남길 수 있습니다.

---

## 주요 기능

* 🗺️ **지도 뷰 (MapActivity)**

  * Google Maps SDK를 활용해 주변 흡연 구역을 마커로 표시
  * 마커 클릭 시 상세 정보 및 댓글 확인 가능

* ➕ **흡연 구역 등록 (PostWriteActivity)**

  * 제목, 설명, 위치(위도·경도) 입력 후 Firebase에 저장

* 💬 **댓글 기능 (Comment)**

  * 각 흡연 구역별로 사용자 댓글 등록 및 열람

* 📅 **흡연 활동 기록 (CalendarActivity)**

  * 일별·월별 흡연 이력을 캘린더 형태로 확인

* 🔔 **알림 기능**

  * 지정 시간에 흡연 활동 알림 수신 (AlarmReceiver, AlarmWorker 활용)

* 👤 **회원가입 & 로그인 (SignupActivity, LoginActivity)**

  * Firebase Authentication 연동

---

## 기술 스택 & 라이브러리

* **언어 & 플랫폼**: Kotlin · Android SDK (minSdk 21)
* **UI**: AndroidX, Material Components
* **지도**: Google Maps SDK for Android
* **백엔드**: Firebase Realtime Database · Firebase Authentication · Firebase Cloud Messaging
* **아키텍처**: MVVM 패턴 (ViewModel, Repository), WorkManager (알림 스케줄링)
* **DI (향후 적용 권장)**: Hilt / Dagger2
* **테스트**: JUnit, Espresso (추가 개발 필요)

---

## 설치 및 실행

1. **레포지토리 클론**

   ```bash
   git clone https://github.com/csm123455/Smokezone.git
   cd Smokezone
   ```

2. **Android Studio로 열기**

   * `SmokeZone` 폴더를 Android Studio에서 Open 또는 Import Project
   * Gradle 빌드가 자동으로 실행됩니다.

3. **환경 설정**

   * `app/google-services.json` 파일을 Firebase Console에서 발급받아 `app/` 폴더에 추가
   * `AndroidManifest.xml`에 Google Maps API Key 설정

     ```xml
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="YOUR_GOOGLE_MAPS_API_KEY"/>
     ```

4. **앱 실행**

   * 에뮬레이터 또는 실제 디바이스에서 `app` 모듈을 Run
   * 최초 회원가입 후 로그인을 통해 기능 사용 가능

---

## 프로젝트 구조

```
SmokeZone/
├─ app/
│  ├─ src/main/
│  │  ├─ AndroidManifest.xml
│  │  ├─ java/com/smokezone/
│  │  │  ├─ ui/                  # Activity와 Adapter
│  │  │  ├─ data/                # 모델 데이터 클래스
│  │  │  ├─ firebase/            # Firebase 통신 헬퍼
│  │  │  ├─ notification/        # 알림 관련 클래스
│  │  │  ├─ worker/              # WorkManager 작업 클래스
│  │  │  └─ SmokeZoneApplication.kt
│  │  └─ res/                    # 리소스 (layout, values, xml 등)
│  └─ build.gradle
├─ build.gradle
├─ settings.gradle
└─ gradlew*
```

---

## 기여 방법

1. 이슈(issue) 또는 기능 요청(feature request) 등록
2. 프로젝트를 Fork & Clone
3. 브랜치를 분리하여 기능 개발

   ```bash
   git checkout -b feature/YourFeatureName
   ```
4. 변경사항 커밋 & 푸시

   ```bash
   git commit -m "Add: YourFeatureName"
   git push origin feature/YourFeatureName
   ```
5. Pull Request 생성 후 리뷰 요청

---

## 라이선스

이 프로젝트는 특별한 라이선스 없이 공개되어 있습니다.
상업적 이용 및 수정·배포 모두 자유롭게 가능합니다.

---

## 문의

* 개발자: 조성민, 최경채
* 이메일: [csm123455@gmail.com](mailto:csm123455@gmail.com)
