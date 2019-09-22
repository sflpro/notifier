#!/bin/bash

curl -X POST \
  http://localhost:8099/notification/email/create \
  -H 'Accept: application/json' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
	"recipientEmail": "poghos.poghosyan@mailinator.com",
	"senderEmail": "noreply@testmail.com",
	"subject": "Hey!",
	"body": "Hey! Bro, how are you?"
}'