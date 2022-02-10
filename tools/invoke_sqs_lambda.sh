aws sqs send-message \
  --queue-url "http://localhost:4566/000000000000/sample-queue" \
  --message-body '{"test":"test value", "hoge": "hoge value"}' \
  --endpoint-url="http://localhost:4566" \
  --region="us-east-1"