from rest_framework import serializers

class MccServiceSerializer(serializers.Serializer):
    # 문자열 필드와 금액 필드를 리스트로 받아옵니다
    str_fields = serializers.DictField(
        child=serializers.CharField(required=True, allow_blank=False)
    )
    float_fields = serializers.DictField(
        child=serializers.FloatField(required=True)
    )
    model_id = serializers.IntegerField(required=True)

class MccBatchServiceSerializer(serializers.Serializer):
    model_id = serializers.IntegerField(required=True)
    hdqr_bob_dcd = serializers.CharField(required=True)

class ModelConfigSerializer(serializers.Serializer):
    """
        Serializer for starting the training service.

        Fields:
        - model_name: Name of the model.
        - model_cfg: Configuration for the model.
        - file_name: Name of the file.
    """
    learning_rate = serializers.IntegerField()
    batch_size = serializers.IntegerField()
    epochs = serializers.IntegerField()

class StartTrainServiceSerializer(serializers.Serializer):
    model_id = serializers.IntegerField()
    model_cfg = ModelConfigSerializer()  # Nested Serializer
    file_name = serializers.CharField(max_length=255)

class StopModelServiceSerializer(serializers.Serializer):
    model_id = serializers.IntegerField()

class ReplaceModelServiceSerializer(serializers.Serializer):
    model_id = serializers.IntegerField()
    model_name = serializers.CharField(required=True)
    mem_id = serializers.CharField(help_text="멤버 ID")



