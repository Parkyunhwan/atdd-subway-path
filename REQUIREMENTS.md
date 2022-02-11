# 요구사항 정리
## 구간 관리
### 역 사이에 새로운 역 추가
* 기존 구간의 역을 기준으로 새로운 구간을 추가
  * 기존 구간 A-C에 신규 구간 A-B를 추가하는 경우 A역을 기준으로 추가
  * 기존 구간과 신규 구간이 모두 같을 순 없음(아래 예외사항에 기재됨)
  * 결과로 A-B, B-C 구간이 생김
* 새로운 길이를 뺀 나머지를 새롭게 추가된 역과의 길이로 설정

### 노선 조회시 응답되는 역 목록 수정
* 구간이 저장되는 순서로 역 목록을 조회할 경우 순서가 다르게 조회될 수 있음
* 아래의 순서대로 역 목록을 응답하는 로직을 변경해야 함
  1. 상행 종점이 상행역인 구간을 먼저 찾는다.
  2. 그 다음, 해당 구간의 하행역이 상행역인 다른 구간을 찾는다.
  3. 2번을 반복하다가 하행 종점역을 찾으면 조회를 멈춘다.

### 예외 케이스
* 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음
* 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음
  * 아래의 이미지 에서 A-B, B-C 구간이 등록된 상황에서 B-C 구간을 등록할 수 없음(A-C 구간도 등록할 수 없음)
* 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음

### 구간 삭제에 대한 제약 사항 변경 구현
* 기존에는 마지막 역 삭제만 가능했는데 위치에 상관 없이 삭제가 가능하도록 수정
* 종점이 제거될 경우 다음으로 오던 역이 종점이 됨
* 중간역이 제거될 경우 재배치를 함
  * 노선에 A - B - C 역이 연결되어 있을 때 B역을 제거할 경우 A - C로 재배치 됨
  * 거리는 두 구간의 거리의 합으로 정함

### 구간이 하나인 노선에서 마지막 구간을 제거할 때
* 제거할 수 없음

### 이 외 예외 케이스를 고려
* 기능 설명을 참고하여 예외가 발생할 수 있는 경우를 검증할 수 있는 인수 테스트 생성