#!/bin/bash

REGION="us-east-1"
ENDPOINT_URL="http://localhost:4566"
LAMBDA_FUNCTION_NAME="simple-function"

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
    --handler com.example.localstack.SimpleHandler \
    --timeout 900 \
    --role dummyrole \
    --endpoint-url $ENDPOINT_URL \
    --region $REGION
# timeout: default is 3, max is 900. short value will cause error `Task timed out after 3.00 seconds`
