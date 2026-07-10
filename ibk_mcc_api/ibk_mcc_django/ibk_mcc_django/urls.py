# from django.contrib import admin
from django.urls import path, include
from django.contrib import admin
from drf_yasg.views import get_schema_view
from drf_yasg import openapi
from rest_framework import permissions

schema_view = get_schema_view(
   openapi.Info(
      title="ibk_mcc",  # 원하는 제목 작성
      default_version='1.1',  # 애플리케이션의 버전
      description="",  # 설명
      terms_of_service="",
      contact=openapi.Contact(email=""),
      license=openapi.License(name=""),
   ),
   public=True,
   permission_classes=(permissions.AllowAny,),
#    authentication_classes=[]  # settings.py의 REST_FRAMEWORK > DEFAULT_AUTHENTICTION_CLASSES 가 적용되어 있다면 추가해줄 것
)

urlpatterns = [
    path('swagger.json/', schema_view.with_ui(cache_timeout=0), name='schema-json'),
    path('swagger/', schema_view.with_ui('swagger', cache_timeout=0), name='schema-swagger-ui'),
    path('redoc/', schema_view.with_ui('redoc', cache_timeout=0), name='schema-redoc'),
    path('admin/', admin.site.urls),  # 관리자 페이지 URL 추가,
    path('', include('ibk_mcc_app.urls'))
]