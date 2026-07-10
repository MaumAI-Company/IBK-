from django.urls import path
from . import views

urlpatterns = [
    path('mcc-classify-batch/',views.mcc_classify_batch_service),
    path('mcc-classify/', views.mcc_classify_service),
    path('train-model/', views.start_train_service),
    path('stop-model/', views.stop_train_service),
    path('check-engine/', views.check_engine_status_service),
    path('check-model/',views.check_model_status_service),
    path('replace-model/', views.replace_model_service),
]
