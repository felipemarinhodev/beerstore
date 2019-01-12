#!/bin/bash

echo "Starting ansible..."

ANSIBLE_HOST_KEY_CHECKING=false
echo "Passou pelo ANSIBLE_HOST_KEY_CHECKING"
ansible-playbook -i ../terraform/hosts --private-key ../terraform/key/beerstore_key beerstore-playbook.yml -v

# informa qual é o arquivo de host = -i <arquivo de host>
# para realizar o login é necessário informar a chave privada = --private-key ../terraform/key/beerstore_key
# informa o arquivo aonde esta o playbook = beerstore-playbook.yml
# rodar em modo debug = -v