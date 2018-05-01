중앙대 2018년 1학기 소프트웨어 공학 team 2 
===================

공강 시간에 시간을 때우기 위해서 빈 강의실을 찾으려는 사람들에게 빈 강의실 정보를 제공 해주기 위한 안드로이드 어플리케이션 개발 프로젝트 입니다.


----------


개발 진행 상황
-------------
1. requirement 명세서 작성
2. 중앙대 time table 크롤링
3. DB설계 및 desgin 명세서 작성 중


중앙대 time table 크롤링 상세 설명
-------------

time table은 every time이라는 시간표 관리 웹에서 크롤링 하였습니다. 중앙대 포탈은 웹 반응이 느려 every time 에서 크롤링 하였습니다.

parsing_college_cau.py는 각 단과대 코드와 학과 코드를 크롤링하여 cau_table.txt파일을 생성합니다. 

parsing_table_cau.py는 cau_table.txt파일을 읽어 단과대 코드와 학과 코드를 이용하여 시간표 정보를 크롤링하여 cau_table.txt파일을 생성합니다.

각각 post방식의 request를 하며 알맞는 data와 header정보를 작성하여 놓았습니다. 다만 header에 cookies 정보만 every time에 로그인 하여 추가해 주시면 코드가 동작합니다.

response된 html파일을 beautiful soup라이브러리로 tag별 트리를 탐색하여 원하는 정보를 파싱합니다.



진행 예정 
-------------
DB설계에 맞는 table선언문과 insert문을 c++로 cau_table.txt를 정리하여 sql 구문을 생성할 예정입니다.

생성된 구문으로 mysql의 db파일을 생성하여 안드로이드 스튜디오에서 open하여 사용할 예정입니다.

화면 설계와 안드로이드 desgin pattern은 설계 중입니다.

