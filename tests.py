#!/usr/bin/python

import unittest
import requests
import json
import random
import time
from requests.exceptions import HTTPError
from decimal import Decimal, getcontext
from faker import Faker
from faker.providers import bank,lorem,python

BANKING_CORE_URL = "http://localhost:8080"
BALANCES_URL = "http://localhost:8081"
CUSTOMERS_URL = "http://localhost:8082"

fake = Faker()
fake.add_provider(bank)
fake.add_provider(python)
fake.add_provider(lorem)

customers = []
transactions = []
currencies = ["EUR","USD","GBP"]
expected_balances = {}
frozen_customer = {}

def call_get(base_url, subpath, data={}):
    response = requests.get(f"{base_url}/{subpath}", data=data)
    response.raise_for_status()
    content = json.loads(response.content)
    return content

def call_post(base_url, subpath, data):
    response = requests.post(f"{base_url}/{subpath}", data=data)
    response.raise_for_status()
    content = json.loads(response.content)
    return content

class IntegrationTests(unittest.TestCase):

    def test_011_core_createCustomer(self):
        print(">")
        for _ in range(0,5):
            data = {"firstName": fake.first_name(), "lastName": fake.last_name(), "address": fake.address()}
            result = call_post(BANKING_CORE_URL, "customers", data)
            self.assertIn("customerId", result)
            self.assertEqual(result['firstName'], data["firstName"])
            self.assertEqual(result['lastName'], data["lastName"])
            self.assertEqual(result['address'], data["address"])
            customers.append(result)
            print(result)
    
    def test_012_core_createCustomerAgain(self):   
        customer = customers[0]
        try:
            call_post(BANKING_CORE_URL, "customers", customer)
        except HTTPError as err:
            self.assertEqual(err.response.status_code, 400)
    
    def test_013_core_createTransactions(self):
        for customer in customers:
            print(">")
            for _ in range(0, random.randint(5,25)):
                data = {
                    "customerId": customer["customerId"], 
                    "currency": random.choice(currencies), 
                    "amount": Decimal(random.randrange(-100000*1000, 100000*1000))/1000
                }
                result = call_post(BANKING_CORE_URL, "transactions", data)
                self.assertIn("transactionId", result)
                self.assertEqual(result['customerId'], data["customerId"])
                self.assertEqual(result['currency'], data["currency"])
                self.assertAlmostEqual(Decimal(result['amount']), data["amount"], places=3)
                print(result)
                transactions.append(result)

            tmp = {}
            for curr in currencies:
                transactions_cc = filter(lambda x: x["customerId"] == customer["customerId"] and x["currency"] == curr, transactions)
                ammounts_cc = map(lambda x: x["amount"], transactions_cc)
                tmp[curr] = sum(ammounts_cc)
            expected_balances[customer["customerId"]] = tmp

    def test_021_balance_getPerCustomer(self):
        print(">")
        time.sleep(1)
        for customer in customers: 
            print(customer)
            cid = customer["customerId"]
            result = call_get(BALANCES_URL, f"balances/{cid}")
            for actual_balance in result:
                print(actual_balance)
                self.assertAlmostEqual(actual_balance['amount'],expected_balances[cid][actual_balance["currency"]])

    def test_031_consumer_getCustomerById(self):
        print(">")
        for customer in customers: 
            print(customer)
            cid = customer["customerId"]
            result = call_get(CUSTOMERS_URL, f"customers/{cid}")
            self.assertEqual(result['customerId'], customer["customerId"])
            self.assertEqual(result['firstName'], customer["firstName"])
            self.assertEqual(result['lastName'], customer["lastName"])
            self.assertEqual(result['address'], customer["address"])
    
    def test_032_consumer_getLatestCustomer(self):
        print(">")
        time.sleep(1)
        customer = customers[-1]
        print(customer)
        result = call_get(CUSTOMERS_URL, f"customers/latest")
        self.assertIn(result["customerId"], map(lambda x: x["customerId"], customers))

    def test_041_moneylaundering_freezeAccountWithPlus100000(self):
        global frozen_customer
        
        print(">")
        data = {"firstName": fake.first_name(), "lastName": fake.last_name(), "address": fake.address()}
        customer = call_post(BANKING_CORE_URL, "customers", data)
        print(customer)
        self.assertEqual(customer["accountFrozen"], False)

        data = {
            "customerId": customer["customerId"], 
            "currency": random.choice(currencies), 
            "amount": Decimal(100000)
        }
        call_post(BANKING_CORE_URL, "transactions", data)
        time.sleep(1)
        cid = customer["customerId"]
        frozen_customer = call_get(CUSTOMERS_URL, f"customers/{cid}")
        self.assertEqual(frozen_customer['accountFrozen'], True)

    def test_051_core_createTransactionOnFrozenAccount(self):   
        print(">")
        try:
            data = {
                "customerId": frozen_customer["customerId"], 
                "currency": random.choice(currencies), 
                "amount": Decimal(random.randrange(-100000*1000, 100000*1000))/1000
            }
            call_post(BANKING_CORE_URL, "transactions", data)
        except HTTPError as err:
            self.assertEqual(err.response.status_code, 403)

    
if __name__ == '__main__':
    unittest.main()
