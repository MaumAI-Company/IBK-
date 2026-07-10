#!/usr/bin/env python
"""Django's command-line utility for administrative tasks."""
import os
import sys
import warnings
from django.conf import settings
# 경고 메시지 무시
warnings.filterwarnings("ignore", category=RuntimeWarning, message=r".*migrations.*")


def check_database_connection():
    """Check if the database connection is successful."""
    from django.db import connections
    from django.db.utils import OperationalError

    db_conn = connections['default']
    try:
        db_conn.cursor()  # 데이터베이스에 커넥션 시도
        print("Database connection successful.")
    except OperationalError:
        print("Database connection failed.")
        sys.exit(1)


def main():
    """Run administrative tasks."""
    os.environ.setdefault("DJANGO_SETTINGS_MODULE", "ibk_mcc_django.settings")
    try:
        from django.core.management import execute_from_command_line
        import django
        django.setup()
        print(f'DATA_DIR: {settings.DATA_DIR}')
        # `runserver` 명령어일 때만 데이터베이스 연결을 확인
        if 'runserver' in sys.argv:
            check_database_connection()  # 데이터베이스 연결 확인 추가

        # 'migrate' 명령어가 포함되어 있으면 무시
        if 'migrate' not in sys.argv:
            execute_from_command_line(sys.argv)
        else:
            print("Skipping migrate command.")

    except ImportError as exc:
        raise ImportError(
            "Couldn't import Django. Are you sure it's installed and "
            "available on your PYTHONPATH environment variable? Did you "
            "forget to activate a virtual environment?"
        ) from exc


if __name__ == "__main__":
    main()
