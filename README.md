# 키친포스

## 퀵 스타트

```sh
cd docker
docker compose -p kitchenpos up -d
```

## 요구 사항

- 상품 (Product)
    - [ ] 상품을 생성한다
        - 조건:
            - [ ] 상품의 가격은 0원 이상이어야 한다
            - [ ] 상품의 이름은 비어 있을 수 없다
            - [ ] 상품의 이름은 욕설, 비속어 등 부적절한 단어 포함 검사를 통과해야 한다 (외부 API 사용)
        - 결과:
            - [ ] 요청한 상품 이름으로 상품을 생성하여야 한다
            - [ ] 생성한 상품의 가격은 요청한 가격과 일치하여야 한다
    - [ ] 상품의 가격을 변경한다
        - 조건:
            - [ ] 변경할 가격은 0원 이상이어야 한다
            - [ ] 변경 대상인 상품이 존재하여야 한다
        - 결과:
            - [ ] 변경한 상품의 가격은 요청한 가격과 일치하여야 한다
            - 상품을 포함된 메뉴의 가격이 해당 메뉴에 포함된 메뉴 상품의 가격 합보다 비싸면
                - [ ] 메뉴의 상태를 비활성화 상태로 변경한다
    - [ ] 상품 목록을 조회한다
- 메뉴 그룹 (MenuGroup)
    - [ ] 메뉴 그룹 생성한다
        - 조건:
            - [ ] 메뉴 그룹의 이름은 비어 있을 수 없다
        - 결과:
            - [ ] 요청한 메뉴 그룹이름으로 메뉴 그룹을 생성하여야 한다
    - [ ] 메뉴 그룹 목록을 조회한다
- 메뉴 상품 (MenuProduct)
    - [ ] 메뉴 상품 (MenuProduct)을 생성한다
        - 조건:
            - [ ] 메뉴 상품의 수량은 0개 이상이어야 한다
            - [ ] 메뉴 상품의 상품은 존재하여야 한다
        - 결과:
            - [ ] 메뉴 상품의 가격은 상품의 가격과 수량을 곱한 값이어야 한다
            - [ ] 메뉴 상품의 가격은 요청한 값과 일치하여야 한다
- 메뉴 (Menu)
    - [ ] 새로운 메뉴를 생성한다
        - 조건:
            - [ ] 메뉴의 가격은 0원 이상이어야 한다 (0원 가능)
            - [ ] 메뉴의 그룹은 존재하여야 한다
            - [ ] 매뉴에 포함된 메뉴 상품은 1개 이상이어야 한다
            - [ ] 메뉴에 포함된 메뉴 상품의 상품은 모두 존재하여야 한다
            - [ ] 포함된 모든 메뉴 상품 (MenuProduct)을 생성한다
            - [ ] 메뉴 가격이 전체 메뉴 상품의 가격 합보다 비싸면 안된다 (일치 가능)
            - [ ] 메뉴의 이름은 비어 있을 수 없다
            - [ ] 메뉴의 이름은 욕설, 비속어 등 부적절한 단어 포함 검사를 통과해야 한다 (외부 API 사용)
        - 결과:
            - [ ] 요청한 메뉴 이름으로 메뉴를 생성하여야 한다
            - [ ] 생성한 메뉴의 가격은 요청한 가격과 일치하여야 한다
            - [ ] 생성한 메뉴는 메뉴 그룹을 포함하고 있어야 한다
            - [ ] 생성한 메뉴의 활성화 상태는 요청한 상태와 일치하여야 한다
                - [ ] 생성한 메뉴는 요청한 메뉴 상품을 포함하고 있어야 한다
    - [ ] 특정 메뉴의 가격을 변경한다
        - 조건:
            - [ ] 변경할 가격은 0원 이상이어야 한다
            - [ ] 변경 대상인 메뉴이 존재하여야 한다
            - [ ] 변경할 가격이 변경 대상인 메뉴에 포함된 메뉴 상품의 가격 합보다 비싸면 안된다 (일치 가능)
    - [ ] 특정 메뉴를 활성화한다
        - 조건:
            - [ ] 메뉴가 존재하여야 한다
            - [ ] 메뉴의 가격이 메뉴에 포함된 메뉴 상품의 가격 합보다 비싸면 안된다
    - [ ] 특정 메뉴를 비활성화한다
        - 조건:
            - [ ] 매뉴가 존재하여야 한다
        - 비고:
            - 조건이 필요 없는 이유는 메뉴 비활성화 기능이 단순 활성화 여부를 변경하는 것이기 때문이다 (시각적으로 비활성화)
    - [ ] 메뉴 목록을 조회한다
- 주문 테이블 (OrderTable)
    - [ ] 주문 테이블을 생성한다
        - 조건:
            - [ ] 주문 테이블의 이름은 비어 있을 수 없다
        - 결과:
            - [ ] 요청한 주문 테이블 이름으로 주문 테이블을 생성하여야 한다
          - [ ] 생성한 주문 테이블은 인원 수가 0명이어야 한다
            - [ ] 생성한 주문 테이블은 빈 상태여야 한다
    - [ ] 주문 테이블을 사용한다
        - 조건:
            - [ ] 주문 테이블이 존재하여야 한다
        - 결과:
            - [ ] 주문 테이블은 점유된 상태로 변경되어야 한다
    - [ ] 주문 테이블을 비운다
        - 조건:
            - [ ] 주문 테이블이 존재하여야 한다
            - [ ] 주문 테이블에 포함된 주문이 모두 완료 상태여야 한다
        - 결과:
            - [ ] 주문 테이블은 빈 상태로 변경되어야 한다
          - [ ] 주문 테이블의 인원 수가 0명으로 변경되어야 한다
    - [ ] 주문 테이블의 인원을 변경한다
        - 조건:
            - [ ] 변경할 인원 수는 0명 이상이어야 한다
            - [ ] 주문 테이블이 존재하여야 한다
            - [ ] 주문 테이블이 점유된 상태에만 인원 수를 변경할 수 있다
        - 결과:
            - [ ] 주문 테이블의 인원 수는 변경된 값으로 설정되어야 한다
    - [ ] 주문 테이블 목록 조회한다
- 주문 상세 항목 (OrderLineItem)
    - [ ] 주문 상세 항목을 생성한다
        - 조건:
            - [ ] 주문 상세 항목의 메뉴는 존재하여야 한다
            - [ ] 주문 상세 항목의 메뉴는 활성화된 상태여야 한다
          - [ ] 주문 상세 항목의 가격은 포함된 메뉴의 가격과 일치하여야 한다
        - 결과:
            - [ ] 요청한 주문 상세 항목으로 주문 상세 항목을 생성하여야 한다
            - [ ] 생성한 주문 상세 항목의 수량은 요청한 수량과 일치하여야 한다
            - [ ] 생성한 주문 상세 항목의 가격은 포함된 메뉴의 가격과 일치하여야 한다
- 주문 (Order)
    - 공통:
        - [ ] 주문 목록을 조회한다
    - 배달(`DELIVERY`)인 경우:
        - [ ] 주문을 생성한다
            - 조건:
                - [ ] 주문 타입은 배달(`DELIVERY`)이여야 한다
                - [ ] 주문에 주문 상세 항목 (`OrderLineItem`)이 포함되어야 한다
                - [ ] 주문 상세 항목 (`OrderLineItem`)의 개수와 메뉴의 개수가 일치해야 한다
                - [ ] 각 주문 상세 항목 (`OrderLineItem`)의 수량은 0개 이상이어야 한다
                - [ ] 주문에 포함된 모든 주문 상세 항목 (`OrderLineItem`)을 생성한다
                - [ ] 주문에 포함된 각 주문 상세 항목의 메뉴 수량이 1개 및 그 이상이어야 한다
                - [ ] 배달 주소는 비어 있을 수 없다
            - 결과:
                - [ ] 주문의 타입은 배달(`DELIVERY`)로 설정되어야 한다
                - [ ] 주문의 상태는 대기(`WAITING`)로 설정되어야 한다
                - [ ] 주문의 주문 시간은 생성 시간으로 설정되어야 한다
                - [ ] 주문은 모든 주문 상세 항목 (`OrderLineItem`)을 포함하고 있어야 한다
                - [ ] 주문은 배달 주소를 포함하고 있어야 한다
        - [ ] 주문을 접수 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문의 상태가 대기(`WAITING`)인 경우만 접수 상태로 변경할 수 있다
                - [ ] 배달 업체에 배달을 요청한다 (외부 API 호출)
            - 결과:
                - [ ] 주문의 상태는 접수(`ACCEPTED`)로 변경되어야 한다
        - [ ] 주문을 조리 완료 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문 상태가 접수(`ACCEPTED`)인 경우만 조리 완료 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 조리 완료(`SERVED`)로 변경되어야 한다
        - [ ] 주문을 배달 중 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문 타입이 배달(`DELIVERY`)인 경우만 배달 중 상태로 변경할 수 있다
                - [ ] 주문 상태가 조리 완료(`SERVED`)인 경우만 배달 중 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 배달 중(`DELIVERING`)로 변경되어야 한다
        - [ ] 주문을 배달 완료 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문 상태가 배달 중(`DELIVERING`)인 경우만 배달 완료 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 배달 완료(`DELIVERED`)로 변경되어야 한다
        - [ ] 주문을 완료 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문 타입이 배달(`DELIVERY`)이고 주문 상태가 배달 완료(`DELIVERED`)인 경우만 완료 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 완료(`COMPLETED`)로 변경되어야 한다
    - 포장(`TAKE_OUT`)인 경우:
        - [ ] 주문을 생성한다
            - 조건:
                - [ ] 주문 타입은 포장(`TAKE_OUT`)이여야 한다
                - [ ] 주문에 주문 상세 항목 (`OrderLineItem`)이 포함되어야 한다
                - [ ] 주문 상세 항목 (`OrderLineItem`)마다 메뉴 아이디로 메뉴를 포함하고 있어야 한다
                - [ ] 주문 상세 항목 (`OrderLineItem`)의 개수와 메뉴의 개수가 일치해야 한다
                - [ ] 주문에 포함된 모든 주문 상세 항목 (`OrderLineItem`)을 생성한다
                - [ ] 주문에 포함된 각 주문 상세 항목의 메뉴 수량이 1개 및 그 이상이어야 한다
            - 결과:
                - [ ] 주문의 타입은 포장(`TAKE_OUT`)로 설정되어야 한다
                - [ ] 주문의 상태는 대기(`WAITING`)로 설정되어야 한다
                - [ ] 주문의 주문 시간은 생성 시간으로 설정되어야 한다
                - [ ] 주문은 모든 주문 상세 항목 (`OrderLineItem`)을 포함하고 있어야 한다
        - [ ] 주문을 접수 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문의 상태가 대기(`WAITING`)인 경우만 접수 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 접수(`ACCEPTED`)로 변경되어야 한다
        - [ ] 주문을 조리 완료 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문 상태가 접수(`ACCEPTED`)인 경우만 조리 완료 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 조리 완료(`SERVED`)로 변경되어야 한다
        - [ ] 주문을 완료 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문 타입이 포장(`TAKE_OUT`)이고 주문 상태가 조리 완료(`SERVED`)인 경우만 완료 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 완료(`COMPLETED`)로 변경되어야 한다
    - 매장 식사(`EAT_IN`)인 경우:
        - [ ] 주문을 생성한다
            - 조건:
                - [ ] 주문 타입은 매장 식사(`EAT_IN`)이여야 한다
                - [ ] 주문에 주문 상세 항목 (`OrderLineItem`)이 포함되어야 한다
                - [ ] 주문 상세 항목 (`OrderLineItem`)마다 메뉴 아이디로 메뉴를 포함하고 있어야 한다
                - [ ] 주문 상세 항목 (`OrderLineItem`)의 개수와 메뉴의 개수가 일치해야 한다
                - [ ] 주문에 포함된 모든 주문 상세 항목 (`OrderLineItem`)을 생성한다
                - [ ] 주문에 포함된 각 주문 상세 항목의 메뉴 수량이 제한되지 않는다 (0, 음정수 포함)
                - [ ] 주문 테이블이 존재하여야 한다
                - [ ] 주문 테이블이 빈 상태여야 한다
            - 결과:
                - [ ] 주문의 타입은 매장 식사(`EAT_IN`)로 설정되어야 한다
                - [ ] 주문의 상태는 대기(`WAITING`)로 설정되어야 한다
                - [ ] 주문의 주문 시간은 생성 시간으로 설정되어야 한다
                - [ ] 주문은 모든 주문 상세 항목 (`OrderLineItem`)을 포함하고 있어야 한다
                - [ ] 주문은 주문 테이블을 포함하고 있어야 한다
        - [ ] 주문을 접수 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문의 상태가 대기(`WAITING`)인 경우만 접수 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 접수(`ACCEPTED`)로 변경되어야 한다
        - [ ] 주문을 조리 완료 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문 상태가 접수(`ACCEPTED`)인 경우만 조리 완료 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 조리 완료(`SERVED`)로 변경되어야 한다
        - [ ] 주문을 완료 상태로 변경한다
            - 조건:
                - [ ] 주문이 존재하여야 한다
                - [ ] 주문 타입이 매장 식사(`EAT_IN`)이고 주문 상태가 조리 완료(`SERVED`)인 경우만 완료 상태로 변경할 수 있다
            - 결과:
                - [ ] 주문의 상태는 완료(`COMPLETED`)로 변경되어야 한다
                - 주문 테이블의 모든 주문이 완료 상태인 경우
                    - [ ] 주문 테이블을 빈 상태로 변경되어야 한다
                  - [ ] 주문 테이블의 인원 수가 0명으로 변경되어야 한다

## 용어 사전

| 한글명 | 영문명 | 설명 |
|-----|-----|----|
|     |     |    |

## 모델링
