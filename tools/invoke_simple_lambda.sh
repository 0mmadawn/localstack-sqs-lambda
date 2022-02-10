aws lambda invoke \
    --function-name simple-function \
    --endpoint-url http://localhost:4566 \
    --region us-east-1 \
    --payload '{"test":"test value", "hoge": "hoge value"}' \
    --cli-binary-format raw-in-base64-out \
    result.log