#!/bin/bash

./mvnw clean package
aws cloudformation package --template-file aws-template.yml --s3-bucket serverless-map-reduce --output-template-file packaged-template.yml
aws cloudformation deploy --template-file /Users/leonidborisevich/IdeaProjects/serverless-map-reduce-java/packaged-template.yml --stack-name serverless-map-reduce --capabilities CAPABILITY_IAM
rm packaged-template.yml