# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# 1. 실습 - 단위테스트 작성
* 지하철 구간 단위 테스트를 완성하세요.
  * 구간 단위 테스트
    * [x] 서비스클래스 내 라인저장메서드를 도메인로직으로 이동
      * [x] 단위테스트 코드 (성공, 실패)
    * [x] updateLine 단위테스트 추가
  * [x] 구간 서비스 단위테스트 with mock
  * [x] 구간 서비스 단위테스트 without mock
* [x] 단위테스트를 기반으로 비즈니스 로직을 리팩터링하세요.

### a. 리팩터링 요구사항
* 구간 추가/삭제 기능에 대한 비즈니스로직을 LineSerivice에서 도메인클래스(Line)으로 옮기기
* 리팩터링 진행 시 LineTest의 테스트 메서드를 활용하여 TDD 사이클로 진행

# 2. 1단계 - 지하철 구간 추가 기능 개선
## 요구사항
1. [x] 새 구간의 하행역이 기존구간의 중간에 삽입되는 경우
   * [x] 노선 내 구간 위치 확인
   * A - B - C
   * A - D
   * A - D, D - B 추가 & A - B 삭제
   * K - B
   * A - K, K - B 추가 & A - B 삭제
   * 추가하행역이 노선에 없는 경우 사용
2. [x] 새 구간의 상행역이 기존구간의 상행종점으로 등록되는 경우
   * A - B - C
   * Z - A
   * Z - A만 추가
   * 추가하행역이 노선의 상행역인 경우
3. [x] 새 구간의 하행역이 기존구간의 하행종점으로 등록되는 경우
   * A - B - C
   * C - D
   * C - D만 추가
   * 추가상행역이 노선의 하행역인 경우

### 공통 예외케이스
* 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같다면 예외처리
* [x] 상행역과 하행역이 모두 등록되어 있다면 예외
* [x] 상행역과 하행역이 둘 중 하나도 포함되어 있지 않다며 예외

## 구간 추가 변경사항 적용
* [x] 구간추가로직 변경 전에 인수테스트 작성
* [x] 노선의 구간을 조회해올 때 순서보장 안되는 문제 해결

### 리팩터링 작업
* [x] line내 List<Section>을 Sections 객체로 따로 추출
* [x] stations 조회 시 순서보장하도록 메서드 수정
  * 노선상행역 조회하기
    * 현재구간상행역이 모든 노선의 하행역과 같지 않은 경우
  * 다음구간 조회
    * 현재구간의 하행역과 일치하는 상행역이 다음구간임
* 구간 삽입 
  * 중간삽입이라면 일치하는 구간을 선택
  * 해당구간삭제하고 그 사이에 2개 구간 추가
  * 삽입하는 구간의 상행역과 기존구간의 상행역이 일치하는 경우
  * 삽입하는 구간의 하행역과 기존구간의 하행역이 일치하는 경우

# 2. 2단계 - 지하철 구간 제거 기능 개선
## 기능 요구사항
* [x] 인수 테스트 주도 개발 프로세스에 맞춰서 개발
  1. 인수조건 정의
  2. 인수조건을 검증하는 인수테스트 작성
  3. 인수 테스트를 충족하는 기능 구현

## 요구사항 설명
**구간 삭제에 대한 제약 사항 변경 구현**
* 마지막 역 삭제만 가능 --> 위치에 상관없이 삭제 가능하도록 수정
* 종점이 제거되면 다음역이 종점이 됨
* [x] 중간역이 제거되면 재배치
  * A - B - C에서 B역을 제거하면 A - C로 재배치
  * 거리는 두 구간의 합으로 결정