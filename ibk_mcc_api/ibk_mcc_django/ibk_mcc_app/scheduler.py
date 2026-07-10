import logging
from datetime import datetime
import requests
from apscheduler.schedulers.background import BackgroundScheduler
import pytz

logger = logging.getLogger('ibk_mcc_app.scheduler')

class ModelStatusScheduler:
    def __init__(self):
        self.scheduler = BackgroundScheduler()
        self.api_endpoint = "http://127.0.0.1:9001"
        logger.info("ModelStatusScheduler initialized")

    def check_model_status_job(self):
        try:
            response = requests.post(f"{self.api_endpoint}/check-model/")
            if response.status_code == 200:
                result = response.json()
                logger.info(f"Check result: {result}")
        except Exception as e:
            logger.error(f"Error in job execution: {str(e)}")

    def start(self):
        if self.scheduler.running:
            return
            
        self.scheduler.add_job(
            self.check_model_status_job,
            'interval',
            minutes=10,
            id='model_status_check'
        )
        self.scheduler.start() 
