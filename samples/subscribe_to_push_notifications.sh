#!/bin/bash

curl -X POST \
  http://localhost:8099/notification/push/subscribe \
  -H 'Accept: application/json' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
	"userUuId": "user_id_pogho_poghos_poghosyan",
	"subscribe": "true",
	"deviceUuId": "device_id_poghos_poghosi_poghosya",
	"deviceOperatingSystemType": "ANDROID",
	"application": "test_app",
	"userDeviceToken": "{userDeviceToken obtained for corresponding device(browser as well)}"
}'