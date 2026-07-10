import mysql.connector

def test_connection():
    try:
        connection = mysql.connector.connect(
            host='10.50.3.18',      # 데이터베이스 서버 IP 주소 (Django settings의 HOST)
            port='13306',           # 포트 (Django settings의 PORT)
            user='maummcc',         # 사용자명
            password='maum!2024!',  # 비밀번호
            database='maummcc'      # 데이터베이스 이름
        )
        
        if connection.is_connected():
            print("데이터베이스 접속 성공!")
        else:
            print("데이터베이스에 접속하지 못했습니다.")
    
    except mysql.connector.Error as err:
        print(f"데이터베이스 접속 실패: {err}")
    
    finally:
        if connection.is_connected():
            connection.close()

if __name__ == "__main__":
    test_connection()