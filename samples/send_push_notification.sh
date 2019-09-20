#!/bin/bash

curl -X POST \
  http://localhost:8099/notification/push/create \
  -H 'Accept: application/json' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
	"subject": "Ehey!!!",
	"properties":[ {"propertyKey":"testKey","propertyValue": "Test value"},
	{"propertyKey":"priority","propertyValue": "HIGH"}],
	"userUuId": "user_id_poghos_poghosi_poghosyan",
	"body": "Hey!"
}'