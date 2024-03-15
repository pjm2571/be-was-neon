[BE] Web Server 구현 (WAS)
===

# 미션 학습 계획 [Day -1]

## 1) Concurrent Package

- 적은 수의 Thread가 동작하는 경우, JVM이 Thread를 관리하는 것 만으로 좋을 수 있다.
- 하지만, 웹 소켓 통신의 경우 여러개의 클라이언트 소켓이 붙을 수 있고, 각각의 소켓은 '스레드'로서 동작을 한다.
- 위 경우에, 스레드가 몇십, 몇백개 생성될 수 있으므로 다중 스레드를 효율적으로 관리하여야 한다.

### ExecutorService
- ```ExecutorService```는 스레드 풀을 생성하고 관리하며, 작업을 스케줄링하고 제어하는 메소드를 제공한다.
- ```Executor```인터페이스를 구현한 클래스이다.

- ExecutorService의 newFixedThreadPool을 이용하여 미션을 구현해본다.
  - 크기를 100으로 한정하여 먼저 생성해본다.


## 2) HTTP Request 처리

- HTTP Request란?
  - 웹 클라이언트 -> 웹 서버에게 ```리소스``` 요청
- 리소스란?
  - 크게 3가지로 나눌 수 있다
  1) Request Line
  2) Headers
  3) Body

### 1) Request Line
- HTTP 메서드(GET, POST, PUT, DELETE 등)
- 요청 대상(URI 또는 URL)
- HTTP 버전
```GET /index.html HTTP/1.1과 같이 구성
HTTP 메소드 : GET
요청 대상 : /index.html
HTTP 버전 : HTTP/1.1
```

>Q) HTTP 버전은 왜 필요할까??
>
>HTTP 버전은 클라이언트와 서버가 상호 작용하는 방식을 결정하고, 웹 통신의 효율성과 보안을 강화하는 데 중요한 역할을 한다.
>
> HTTP 1.1 : 파이프 라이닝 방식
>
> HTTP / 2 : 멀티 플렉싱 방식
>
> 따라서, HTTP의 버전 방식에 따라 동작 방식이 다르기 때문에, 명시해주어야 한다.


### 2) Headers
- 요청에 대한 여러 메타데이터 정보가 포함
- 클라이언트가 어떤 종류의 데이터를 수락할 수 있는지(Accept)
- 어떤 언어를 선호하는지(Accept-Language)
- 사용자의 쿠키 정보(Cookie)

> Q) Headers가 필요한 이유는 무엇일까?
>
> 프로토콜의 헤더의 내용은 특정 프로토콜의 기능을 제공하기 위해 담고 있는 최소한의 정보
>
> 따라서, 헤더를 알면 어떤 종류인지, 캐싱, 인증 기법을 적용할 수 있기 때문
>
> 이를 통해 통신의 효율성을 개선하고 보안을 강화할 수 있다.

### 3) Body
- 요청 메시지에 대한 추가 데이터가 포함
- POST 요청과 같이 데이터를 서버로 전송해야 할 때 사용된다.

> Q) index.html을 입력하면 Body가 있어야 하는 것 아니야?

### ```GET /index.html HTTP/1.1``` : 서버에게 /index.html이라는 파일을 요청하는 것
- 클라이언트가 /index.html이라는 파일을 웹 서버로부터 가져오고자 하는 것
  - 따라서, byte[] 배열에 body의 정보를 담아서 리턴해주어야 한다.

### ```GET /index HTTP/1.1``` : 서버에게 /index라는 경로를 요청하는 것
- 확장자가 없는 /index라는 경로를 요청하는 것
- 웹 서버가 이러한 요청을 기본적으로 인덱스 페이지로 처리해주어야 한다.
- 즉, 서버는 클라이언트에게 인덱스 페이지를 반환한다.

> 두 요청은 요청하는 리소스의 경로가 다르므로 서로 다른 요청이다.
>
> 처음 요청은 특정 파일을 요청하는 것
>
> 두 번째 요청은 경로만을 요청하는 것

---

# 미션 학습 계획 [Day - 2, Day -3]

## http 통신과 소켓 통신의 차이

### HTTP 통신
- http 통신은 일종의 Protocol (규약)이다.

```
[ HTTP 통신 약속]

1) Client가 Server로 Request를 보낸다.
2) Server가 Client에게 Response를 보낸다.
```
- Server to Client 통신은 하지 못한다는 단점이 있다.

### Socket 통신
- 서버와 클라이언트간의 양방향 통신이 가능하게 됨
- hand-shaking 과정을 통해 클라이언트와 서버끼리의 연결이 생성된다.
```
클라이언트에서 서버로의 연결 요청
‘연결 요청’에 대한 서버에서 클라이언트로의 확인 응답
‘확인 응답’에 대한 클라이언트에서 서버로의 추가 응답
```
- 위와 같은 3-way hand shake로 연결이 생성된다.

## Port 번호
- Port번호란, “프로세스”를 구분짓기 위한 16비트 크기의 할당
- 0 ~ 2^16 까지의 번호가 할당된다.

### 1) Well-Know Port 번호
- 0 ~ 1023 번까지의 포트 번호로 역할이 이미 정해져 있다.

### 2) Registered Port 번호
- 1024번부터 49151까지의 번호

### 3) Private Port 번호
- 49152 ~ 65535까지의 번호
> 이 번호 사이의 값들로 client 소켓의 포트 번호가 생성되어 serverSocket과 통신을 이루어 동작한다.



# 미션 학습 계획 [Day - 4]

## 객체 분리를 하자
- Request, Response, RequestHandler, ResponseHandler .. 등으로 객체를 나누어보자

### Request 객체
- Request 요청은 RequestHeader, RequestBody 로 이루어져 있다.

- Get 요청은 RequestBody가 없다.
- Post 요청은 RequestBody가 있다.

> 일단은 RequestHeader만 가져와서 객체로 생성

### Response 객체
- Response는 ResponseHeader, ResponseBody 로 이루어져 있다.
- 200 Response : responseBody에 html이나 값들을 가져와야 한다.
- 302 Response : responseBody가 필요없다. -> null

### Create 객체
- Request의 Method를 분석해서 ```/create```요청이라면, CreateHandler로 유저를 생성하자

## Port, Source에 대한 외부 주입!
- 기존 코드에서는 값을 하드코딩 해주는 식으로 구현을 했다.
```
private final static int port = 8080;
...
private final static String STATIC = "/src/....";
```
- 위와 같은 상수 값들을 내부적으로 처리하지 않고, 외부로 관리하는 방법에 대해 고민!

### config.properties & config.yaml
- config.properties 파일로 관리를 해줄 수도, config.yaml 파일로 관리를 해줄 수도 있다.
- 이번 프로젝트에서는 yaml 파일을 통해 static 경로와 port 번호를 가져올 수 있도록 구현해본다.


```
config.yaml 파일

port: 8080
staticSourcePath: ./src/main/resources/static
```

```
YamlConfigReader 클래스

1) loadStaticSourcePathFromYaml
2) loadPortFromYaml
```
- 위의 메소드를 통해 yaml 파일에 저장된 경로, 포트 번호를 가져와서 사용한다!