import requests
import random
import time
import string
import threading
from concurrent.futures import ThreadPoolExecutor

BASE_URL = "http://localhost:8080/api"

def generate_cpf():
    def calculate_digit(digits):
        weight = len(digits) + 1
        total = sum(d * (weight - i) for i, d in enumerate(digits))
        remainder = (total * 10) % 11
        return 0 if remainder == 10 else remainder

    digits = [random.randint(0, 9) for _ in range(9)]
    digits.append(calculate_digit(digits))
    digits.append(calculate_digit(digits))
    return "".join(map(str, digits))

def generate_email():
    return "".join(random.choices(string.ascii_lowercase, k=10)) + "@test.com"

def register_user():
    cpf = generate_cpf()
    # Format CPF as 000.000.000-00
    cpf_formatted = f"{cpf[:3]}.{cpf[3:6]}.{cpf[6:9]}-{cpf[9:]}"
    email = generate_email()
    password = "password123"
    
    # Date to timestamp (milliseconds)
    # 1990-01-01 -> 631152000000
    
    payload = {
        "login": email,
        "password": password,
        "nome": "Test User",
        "cpf": cpf_formatted,
        "dataNascimento": "1990-01-01", 
        "nomeDaMae": "Mother Name",
        "senhaTransacao": "123456"
    }
    
    response = requests.post(f"{BASE_URL}/auth/register", json=payload)
    if response.status_code != 200:
        print(f"Failed to register. Status: {response.status_code}")
        print(f"Headers: {response.headers}")
        try:
            print(f"Body: {response.json()}")
        except:
            print(f"Body: {response.text}")
        return None
    return email, password

def login(email, password):
    payload = {"login": email, "password": password}
    response = requests.post(f"{BASE_URL}/auth/login", json=payload)
    if response.status_code == 200:
        return response.json() # Expects {token, userId}
    print(f"Failed to login: {response.text}")
    return None

def deposit(user_id, amount):
    # Endpoint: /api/wallets/{id}/deposit
    # No auth needed as we set permitAll
    payload = {"amount": amount}
    response = requests.post(f"{BASE_URL}/wallets/{user_id}/deposit", json=payload)
    if response.status_code not in [200, 204]:
        print(f"Failed to deposit: {response.text}")

def transfer(sender_token, sender_id, receiver_id, amount):
    headers = {"Authorization": f"Bearer {sender_token}"}
    payload = {
        "senderId": sender_id,
        "receiverId": receiver_id,
        "amount": amount,
        "transactionType": "TRANSFER"
    }
    start = time.time()
    response = requests.post(f"{BASE_URL}/transactions", json=payload, headers=headers)
    end = time.time()
    return response.status_code, (end - start)

def run_test():
    print("--- Starting Performance Test ---")
    
    # 0. Ping Check
    try:
        resp = requests.get(f"{BASE_URL}/wallets/ping")
        print(f"Ping Status: {resp.status_code}, Body: {resp.text}")
    except Exception as e:
        print(f"Ping failed: {e}")
    
    # 1. Setup Users
    print("Creating User A...")
    creds_a = register_user()
    if not creds_a: return
    auth_a = login(*creds_a)
    if not auth_a: return
    id_a = auth_a['userId']
    token_a = auth_a['token']
    print(f"User A Created: {id_a}")

    print("Creating User B...")
    creds_b = register_user()
    if not creds_b: return
    auth_b = login(*creds_b)
    if not auth_b: return
    id_b = auth_b['userId']
    print(f"User B Created: {id_b}")

    # 2. Deposit Money to A
    print("Depositing 1,000,000 to User A...")
    deposit(id_a, 1000000)

    # 3. Load Test
    NUM_REQUESTS = 100
    CONCURRENCY = 10
    
    print(f"Starting {NUM_REQUESTS} transactions with {CONCURRENCY} threads...")
    
    latencies = []
    success_count = 0
    
    start_time = time.time()
    
    def worker():
        nonlocal success_count
        code, latency = transfer(token_a, id_a, id_b, 1.0)
        latencies.append(latency)
        if code == 201:
            # Thread safety for counter not strictly handled here but ok for approx metrics
            success_count += 1
        else:
            print(f"Error: {code}")

    with ThreadPoolExecutor(max_workers=CONCURRENCY) as executor:
        futures = [executor.submit(worker) for _ in range(NUM_REQUESTS)]
        for future in futures:
            future.result()
            
    total_time = time.time() - start_time
    
    # 4. Results
    avg_latency = sum(latencies) / len(latencies) if latencies else 0
    tps = NUM_REQUESTS / total_time
    
    print("\n--- Results ---")
    print(f"Total Requests: {NUM_REQUESTS}")
    print(f"Successful: {success_count}")
    print(f"Total Time: {total_time:.2f}s")
    print(f"TPS (Transactions Per Second): {tps:.2f}")
    print(f"Average Latency: {avg_latency*1000:.2f}ms")
    print("----------------")

if __name__ == "__main__":
    run_test()
