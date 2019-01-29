zip -d lambda.zip lambda.py
zip -g lambda.zip lambda.py
aws lambda update-function-code --function-name lambda_handler --zip-file fileb://lambda.zip
{
    "FunctionName": "lambda_handler",
    "FunctionArn": "arn:aws:lambda:eu-west-1:494498621269:function:lambda_handler",
    "Runtime": "python3.7",
    "Role": "arn:aws:iam::494498621269:role/lambda_exec_basic_VA",
    "Handler": "lambda_handler",
    "CodeSize": 815,
    "Description": "Virtual Architect lambda handler code",
    "Timeout": 3,
    "MemorySize": 128,
    "LastModified": "2018-11-20T20:41:16.647+0000",
    "CodeSha256": "GcZ05oeHoJi61VpQj7vCLPs8DwCXmX5sE/fE2IHsizc=",
    "Version": "$LATEST",
    "VpcConfig": {
        "SubnetIds": [],
        "SecurityGroupIds": [],
        "VpcId": ""
    },
    "TracingConfig": {
        "Mode": "Active"
    },
    "RevisionId": "d1e983e3-ca8e-434b-8dc1-7add83d72ebd"
}