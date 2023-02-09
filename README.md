# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# 1. 실습 - 단위테스트 작성
* [ ] 지하철 구간 단위 테스트를 완성하세요.
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
* 1. 새 구간의 하행역이 기존구간의 중간에 삽입되는 경우
* 2. 새 구간의 상행역이 기존구간의 상행종점으로 등록되는 경우
* 3. 새 구간의 하행역이 기존구간의 하행종점으로 등록되는 경우

### 공통 예외케이스
* 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같다면 예외처리
* 상행역과 하행역이 모두 등록되어 있다면 예외
* 상행역과 하행역이 둘 중 하나도 포함되어 있지 않다며 예외

## 구간 추가 변경사항 적용
* 구간추가로직 변경 전에 인수테스트 작성
* 먼저 1번 요구사항에 대한 조건을 설정해 테스트 코드 작성
