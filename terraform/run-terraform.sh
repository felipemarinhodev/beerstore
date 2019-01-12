#!/bin/bash

echo "Provisioing infrastructure..."

echo "Finding my ip address..."
MY_PUBLIC_IP="$(curl -s ipinfo.io/ip)"
echo "... your public ip is $MY_PUBLIC_IP"

echo "Staring terraform"
terraform apply -var "my_public_ip=$MY_PUBLIC_IP/32"
