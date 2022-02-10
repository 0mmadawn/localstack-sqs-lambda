#!/bin/bash

REGION="us-east-1"
ENDPOINT_URL="http://localhost:4566"
QUEUE_NAME="sample-queue"
LAMBDA_FUNCTION_NAME="sqs-function"

./gradlew shadowJar
cd ./build/libs/

mv localstack-sqs-lambda-1.0.0.jar localstack-sqs-lambda-1.0.0.zip

# delete lambda if already exist
aws lambda get-function \
    --function-name $LAMBDA_FUNCTION_NAME \
    --endpoint-url $ENDPOINT_URL \
    --region $REGION &> /dev/null && \
aws lambda delete-function \
    --function-name $LAMBDA_FUNCTION_NAME \
    --endpoint-url $ENDPOINT_URL \
    --region $REGION

# create lambda
aws lambda create-function \
    --function-name $LAMBDA_FUNCTION_NAME \
    --zip-file fileb://localstack-sqs-lambda-1.0.0.zip \
    --runtime java11 \
    --handler com.example.localstack.HandlerForSqs \
    --timeout 900 \
    --role dummyrole \
    --endpoint-url $ENDPOINT_URL \
    --region $REGION
# timeout: default is 3, max is 900. short value will cause error `Task timed out after 3.00 seconds`

# create sqs
aws sqs create-queue \
  --queue-name $QUEUE_NAME \
  --endpoint-url $ENDPOINT_URL \
  --attributes '{"VisibilityTimeout":"100"}' \
  --region $REGION
# VisibilityTimeout setting is for `Unable to delete Lambda events from SQS queue (please check SQS visibility timeout settings)`

# mapping lambda and sqs
aws lambda create-event-source-mapping \
  --event-source-arn arn:aws:sqs:${REGION}:000000000000:${QUEUE_NAME} \
  --batch-size 1 \
  --function-name $LAMBDA_FUNCTION_NAME \
  --endpoint-url $ENDPOINT_URL \
  --region $REGION
