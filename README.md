# ibk

# 재구축 및 배포
---
DB > WEB > MONITORING 순서로 진행

---

# 1. ibk_mcc_web

## 🧩 1️⃣ Docker 이미지 등록 (load)

먼저 tar 파일을 Docker에 로드합니다.

```bash
docker load -i ibk_mcc_web_image_latest_v2.0.0.tar
```

> ✅ 실행 후 콘솔에 다음과 같은 출력이 뜰 겁니다:
> 
> 
> ```
> Loaded image: ibk_mcc_web_image:2.0.0
> ```
> 
> 👉 여기서 보이는 **이미지 이름(:태그)** 를 꼭 기억해두세요. (예: `ibk_mcc_web_image:2.0.0`)
> 

---

## 🧩 2️⃣ 이미지 등록 확인

```bash
docker images
```

예시 결과:

```
REPOSITORY             TAG       IMAGE ID       CREATED         SIZE
ibk_mcc_web_image      2.0.0    123abc456def   3 hours ago     800MB
```

---

## 🧩 3️⃣ 컨테이너 실행

```bash
docker run -d --name ibk_mcc_web -v /nas/ibk_mcc/ibk_file:/ibk_file -e TZ=Asia/Seoul --restart=always -p 18081:18081 ibk_mcc_web_image:2.0.0
```

옵션 설명:

- `docker run`
    - Docker 컨테이너를 새로 생성하고 실행하는 명령어입니다.
- `d`
    - **Detached 모드**.
    - 백그라운드에서 컨테이너를 실행합니다.
    - 터미널을 점유하지 않고 계속 컨테이너가 실행됨.
- `-name ibk_mcc_web`
    - 컨테이너 이름 지정.
    - 나중에 `docker stop ibk_mcc_web` 또는 `docker logs ibk_mcc_web`처럼 이름으로 관리 가능.
- `v /nas/ibk_mcc/ibk_file:/ibk_file`
    - **볼륨 마운트**.
    - 호스트의 `/nas/ibk_mcc/ibk_file` 디렉토리를 컨테이너 안 `/ibk_file`에 연결.
    - 컨테이너에서 파일 변경 → 호스트에도 반영, 호스트 변경 → 컨테이너에도 반영.
- `e TZ=Asia/Seoul`
    - 컨테이너 환경 변수 설정.
    - 여기서는 **시간대(TimeZone)를 서울로 설정**.
- `-restart=always`
    - 컨테이너가 종료되거나 Docker 데몬이 재시작해도 **자동으로 다시 시작**.
- `p 18081:18081`
    - **포트 포워딩**.
    - 호스트 18081 포트 → 컨테이너 18081 포트 연결.
    - 외부에서 호스트 IP + 18081 포트로 접근 가능.
- `ibk_mcc_web_image:2.0.0`
    - 사용할 Docker 이미지와 태그.
    - 여기서는 `ibk_mcc_web_image` 이미지의 `2.0.0` 버전 사용.

> ⚠️ 이미지 이름이 다르면 실제 docker load 결과에 나온 이름을 써야 합니다.
> 

---

## 🧩 4️⃣ Docker 네트워크 연결

컨테이너를 띄운 뒤, `ibk_network` 네트워크에 연결합니다.

```bash
docker network connect ibk_network ibk_mcc_web
```

> ⚠️ ibk_network 네트워크가 아직 없으면 먼저 생성해야 합니다:
> 
> 
> ```bash
> // 네트워크 생성
>  docker network create ibk_network
>  
>  // 네트워크등록 DB 웹 둘다
>  docker network connect my_network ibk_mcc_web
>  docker network connect my_network ibk_mcc_db
>  
>  // ping 확인
> [minds@192 home]$ docker exec -it ibk_mcc_web ping ibk_mcc_db
> PING ibk_msg_db_master (172.19.0.2) 56(84) bytes of data.
> 64 bytes from ibk_msg_db_master.ibk_network (172.19.0.2): icmp_seq=1 ttl=64 time=0.085 ms
> 64 bytes from ibk_msg_db_master.ibk_network (172.19.0.2): icmp_seq=2 ttl=64 time=0.092 ms
> 
> // dns 설정
> sudo vi /etc/docker/daemon.json 
> 
> // 위 daemon.json에 아래 추가
> {
>   "dns": ["8.8.8.8", "8.8.4.4"]
> }
> 
> // 도커 재시작
> sudo systemctl restart docker
> 
> // web 컨테이너 다시 띄우기
> 
> ```
> 

## 🧩 5️⃣ 컨테이너 동작 확인

```bash
docker ps
```

정상적으로 뜨면 다음처럼 보입니다:

```
CONTAINER ID   IMAGE                         COMMAND               STATUS         PORTS                        NAMES
abcd1234efgh   ibk_mcc_web_image:2.0.0       "catalina.sh run"     Up 10 seconds  0.0.0.0:18081->18081/tcp     ibk_mcc_web

```

# 2. ibk_mcc_monitoring

## 🧩 1️⃣ Docker 이미지 등록 (load)

먼저 tar 파일을 Docker에 로드합니다.

```bash
docker load -i ibk_mcc_monitoring_image_latest_v2.0.0.tar
```

> ✅ 실행 후 콘솔에 다음과 같은 출력이 뜰 겁니다:
> 
> 
> ```
> Loaded image: ibk_mcc_monitoring_image:2.0.0
> ```
> 
> 👉 여기서 보이는 **이미지 이름(:태그)** 를 꼭 기억해두세요. (예: `ibk_mcc_monitoring_image:2.0.0`)
> 

---

## 🧩 2️⃣ 이미지 등록 확인

```bash
docker images
```

예시 결과:

```
REPOSITORY             TAG       IMAGE ID       CREATED         SIZE
ibk_mcc_monitoring_image      2.0.0    123abc456def   3 hours ago     800MB
```

---

## 🧩 3️⃣ 컨테이너 실행

```bash
docker run -d --name ibk_mcc_monitoring -e TZ=Asia/Seoul --restart=always -p 18082:18082 ibk_mcc_monitoring_image:2.0.0
```

옵션 설명:

- `docker run`
    - 새 컨테이너를 생성하고 실행하는 명령어.
- `d`
    - **Detached 모드**.
    - 터미널을 점유하지 않고 백그라운드에서 컨테이너 실행.
- `-name ibk_mcc_monitoring`
    - 컨테이너 이름 지정.
    - 예: `docker stop ibk_mcc_monitoring`, `docker logs ibk_mcc_monitoring` 등 이름으로 관리 가능.
- `e TZ=Asia/Seoul`
    - 컨테이너 환경 변수 설정.
    - 여기서는 **시간대(TimeZone)를 서울로 설정**.
- `-restart=always`
    - 컨테이너 종료, 시스템 재시작 시 자동으로 재시작.
- `p 18082:18082`
    - **포트 포워딩**.
    - 호스트 18082 포트 → 컨테이너 18082 포트 연결.
    - 외부에서 `호스트IP:18082`로 접근 가능.
- `ibk_mcc_monitoring_image:2.0.0`
    - 사용할 Docker 이미지와 태그.
    - 여기서는 `ibk_mcc_monitoring_image` 이미지의 `2.0.0` 버전 사용.

> ⚠️ 이미지 이름이 다르면 실제 docker load 결과에 나온 이름을 써야 합니다.
> 

---

## 🧩 4️⃣ Docker 네트워크 연결

컨테이너를 띄운 뒤, `ibk_network` 네트워크에 연결합니다.

```bash
docker network connect ibk_network ibk_mcc_web
```

> ⚠️ ibk_network 네트워크가 아직 없으면 먼저 생성해야 합니다:
> 
> 
> ```bash
> // 네트워크 생성
>  docker network create ibk_network
>  
>  // 네트워크등록 DB 웹 둘다
>  docker network connect my_network ibk_mcc_monitoring
>  docker network connect my_network ibk_mcc_db
>  
>  // ping 확인
> [minds@192 home]$ docker exec -it ibk_mcc_monitoring ping ibk_mcc_db
> PING ibk_msg_db_master (172.19.0.2) 56(84) bytes of data.
> 64 bytes from ibk_msg_db_master.ibk_network (172.19.0.2): icmp_seq=1 ttl=64 time=0.085 ms
> 64 bytes from ibk_msg_db_master.ibk_network (172.19.0.2): icmp_seq=2 ttl=64 time=0.092 ms
> 
> // dns 설정
> sudo vi /etc/docker/daemon.json 
> 
> // 위 daemon.json에 아래 추가
> {
>   "dns": ["8.8.8.8", "8.8.4.4"]
> }
> 
> // 도커 재시작
> sudo systemctl restart docker
> 
> // 컨테이너 다시 띄우기
> 
> ```
> 

## 🧩 5️⃣ 컨테이너 동작 확인

```bash
docker ps
```

정상적으로 뜨면 다음처럼 보입니다:

```
CONTAINER ID   IMAGE                         COMMAND               STATUS         PORTS                        NAMES
89b28a29f85d        ibk_mcc_monitoring_image:0.0.2   "java -jar /app.war"     8 months ago        Up 13 days          0.0.0.0:18082->18082/tcp             ibk_mcc_monitoring

```

# 3. ibk_mcc_db

아래 도커 파일을 만들고 이미지를 생성해서 컨테이너를 띄우는 과정은 예시일 뿐

같은 버전의 mariadb를 설치하여 아래 github의 sql문을 실행하면 이상 없다.

### 1️⃣ Dockerfile 예시

```docker
# 베이스 이미지
FROM mariadb:10.8.2

# 환경 변수 설정
ENV MYSQL_ROOT_PASSWORD=root \
    PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin \
    GOSU_VERSION=1.14 \
    MARIADB_MAJOR=10.8 \
    MARIADB_VERSION=1:10.8.2+maria~focal

# 데이터 디렉토리 볼륨
VOLUME ["/var/lib/mysql"]

# 엔트리포인트
ENTRYPOINT ["docker-entrypoint.sh"]

# 기본 실행 명령
CMD ["mariadbd"]

```

---

### 2️⃣ 이미지 빌드

```bash
docker build -t ibk_mcc_db:0.0.1 .
```

- 현재 디렉토리에 Dockerfile이 있어야 함.
- 빌드가 끝나면 `docker images`에서 확인 가능.

---

### 3️⃣ 컨테이너 실행

```bash
docker run -d \
  --name ibk_mcc_db \
  -p 13306:3306 \
  --network ibk_network \
  -e MYSQL_ROOT_PASSWORD=root \
  --restart always \
  ibk_mcc_db:0.0.1

```

### 4️⃣ 사용자 계정 및 스키마 생성

---

- maummcc 스키마
- 초기 배포용
- 버전 2.0.0
- 전체 내용(DDL + 데이터 + 함수) 포함
- 생성 날짜: 2025-10-13
- root 권한으로 실행 필요

---

https://github.com/IBK-Cash-Disbursement-Voucher-AI-System/ibk_AI_cash_disbursement_voucher/blob/develop/ibk_maummcc_%20init_v2.0.0_full_20251013.sql




---
admin/admin
