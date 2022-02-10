## about

sample of localstack: sqs, lambda(kotlin)

lambda function written in kotlin, and use log4j2 and slf4j for logging.

## required

* docker
* awscli

## ready

### set aws-cli
```
% aws configure --profile localstack
AWS Access Key ID [None]: dummy
AWS Secret Access Key [None]: dummy
Default region name [None]: us-east-1
Default output format [None]: json

% cat ~/.aws/credentials
[localstack]
aws_access_key_id = dummy
aws_secret_access_key = dummy

% cat ~/.aws/config
[profile localstack]
region = us-east-1
output = json

% export AWS_PROFILE=localstack
% aws configure list

      Name                    Value             Type    Location
      ----                    -----             ----    --------
   profile               localstack              env    ['AWS_PROFILE', 'AWS_DEFAULT_PROFILE']
access_key     ********************              env    
secret_key     ********************              env    
    region                us-east-1      config-file    ~/.aws/config
```

### launch container
```
cd docker
docker-compose up
```
Don't use option `-d` to see the log in console.

## try

### try lambda only

```
# create jar and create lambda...
% ./tools/deploy-simple-lambda

# invoke lambda
% ./tools/ivoke-simple-lambda
```

When localstack is in a good mood(!?), the following log can be seen in docker-compose stdout.

```
2022-02-10T17:08:58:DEBUG:localstack.services.awslambda.lambda_executors: Lambda arn:aws:lambda:us-east-1:000000000000:function:simple-function result / log output:
"result... test:test value,hoge:hoge value"
> WARNING: sun.reflect.Reflection.getCallerClass is not supported. This will impact performance.
> START RequestId: 9ec9e22c-c7ee-178f-1e8d-0b088e2f5a8e Version: $LATEST
> 2022-02-10 17:08:58 9ec9e22c-c7ee-178f-1e8d-0b088e2f5a8e INFO  SimpleHandler:14 - INFO!
> 2022-02-10 17:08:58 9ec9e22c-c7ee-178f-1e8d-0b088e2f5a8e DEBUG SimpleHandler:15 - DEBUG!
> 2022-02-10 17:08:58 9ec9e22c-c7ee-178f-1e8d-0b088e2f5a8e WARN  SimpleHandler:17 - WARN!
> 2022-02-10 17:08:58 9ec9e22c-c7ee-178f-1e8d-0b088e2f5a8e ERROR SimpleHandler:18 - ERROR!
> 2022-02-10 17:08:58 9ec9e22c-c7ee-178f-1e8d-0b088e2f5a8e INFO  SimpleHandler:20 - test:test value
> 2022-02-10 17:08:58 9ec9e22c-c7ee-178f-1e8d-0b088e2f5a8e INFO  SimpleHandler:20 - hoge:hoge value
> END RequestId: 9ec9e22c-c7ee-178f-1e8d-0b088e2f5a8e
> REPORT RequestId: 9ec9e22c-c7ee-178f-1e8d-0b088e2f5a8e        Init Duration: 2113.37 ms       Duration: 103.74 ms     Billed Duration: 104 ms Memory Size: 1536 MB    Max Memory Used: 91 MB  
```

### try lambda and sqs

```
# create jar and create lambda and create sqs and mapping them
% ./tools/deploy-simple-lambda

# send message to sqs
% ./tools/ivoke-simple-lambda
```

After running `ivoke-simple-lambda` for a while, you can see the log like above.

## bugs

When localstack is not in a good mood, the following error may be output...

* `java.lang.NoClassDefFoundError: kotlin/UninitializedPropertyAccessException`
* `java.lang.NoClassDefFoundError: org/slf4j/LoggerFactory`
* and more!

## memo: other useful code

get lambda function list

```
# https://qiita.com/yut0201/items/543ada113790b10c8913#1-%E5%AE%9F%E8%A1%8C%E7%B5%90%E6%9E%9C%E3%81%AE%E4%B8%80%E8%A6%A7%E8%A1%A8%E7%A4%BA%E3%83%86%E3%83%BC%E3%83%96%E3%83%AB%E5%BD%A2%E5%BC%8F
% aws lambda list-functions --query "Functions[].[FunctionName,Handler,runtime,memory]" \
    --output table \
    --endpoint-url http://localhost:4566 \
    --region us-east-1
```

get sqs que status

```
aws sqs get-queue-attributes \
  --queue-url "http://localhost:4566/000000000000/sample-queue" \
  --attribute-names All \
  --endpoint-url="http://localhost:4566" \
  --region="us-east-1"
```
