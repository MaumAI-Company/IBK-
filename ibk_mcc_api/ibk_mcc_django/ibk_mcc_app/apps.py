from django.apps import AppConfig
import logging
import os
import sys

logger = logging.getLogger('ibk_mcc_app.scheduler')

class IbkMccAppConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'ibk_mcc_app'
    scheduler_started = False
    
    def ready(self):
        """
        Django 애플리케이션이 준비되었을 때 호출되는 메서드
        """
        # runserver 명령어로 실행된 경우에만 실행
        if 'runserver' not in sys.argv:
            return
            
        # 메인 프로세스에서만 실행 (autoreloader 방지)
        if not os.environ.get('RUN_MAIN'):
            return
            
        logger.debug("=== AppConfig ready() method called ===")
        
        # 이미 시작된 경우 중복 실행 방지
        if IbkMccAppConfig.scheduler_started:
            logger.info("Scheduler already started, skipping initialization")
            return
            
        try:
            from . import scheduler
            
            # 스케줄러 인스턴스 생성 및 시작
            if not hasattr(scheduler, 'scheduler'):
                logger.info("Creating new scheduler instance")
                scheduler.scheduler = scheduler.ModelStatusScheduler()
            
            if not scheduler.scheduler.scheduler.running:
                logger.info("Starting scheduler")
                scheduler.scheduler.start()
                IbkMccAppConfig.scheduler_started = True
                logger.info("Scheduler started successfully")
            else:
                logger.info("Scheduler is already running")
            
        except Exception as e:
            logger.error(f"Failed to initialize scheduler: {str(e)}")
            logger.exception("Detailed error trace:")

        logger.debug("=== AppConfig ready() method completed ===")