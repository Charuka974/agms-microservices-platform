import httpx
import asyncio
import random  # For simulation
from app.models.sensor_model import SensorData
from datetime import datetime

sensor_history = []
access_token = "mock-token-123" # Default token for simulation

async def fetch_external_telemetry():
    global access_token

    try:
        # 1. Try Real API (with a short 2-second timeout)
        async with httpx.AsyncClient(timeout=2.0) as client:
            # Auth Logic
            if access_token == "mock-token-123":
                resp = await client.post("http://104.211.95.241:8080/api/auth/login",
                                         json={"username": "charuka", "password": "123456"})
                access_token = resp.json().get("accessToken")

            # Fetch Device and Telemetry...
            # (Assuming this part fails and triggers the except block)
            headers = {"Authorization": f"Bearer {access_token}"}
            dev_resp = await client.get("http://104.211.95.241:8080/api/devices", headers=headers)
            dev_resp.raise_for_status()

    except (httpx.ConnectTimeout, httpx.ConnectError, Exception):
        # 2. SIMULATION MODE (Fallback)
        print("IoT API Unreachable. Switching to Simulation Mode...")

        # Generate random values to test your Automation Rules
        mock_temp = round(random.uniform(15.0, 40.0), 2)
        mock_hum = round(random.uniform(30.0, 90.0), 2)

        simulated_data = {
            "deviceId": "SIMULATED-DEVICE-01",
            "zoneId": "1", # Match the ID in your Zone Service
            "value": {
                "temperature": mock_temp,
                "humidity": mock_hum
            },
            "capturedAt": datetime.now().isoformat()
        }

        # Push to local history
        sensor_history.append(simulated_data)

        # PUSH to Automation Service (Port 8083)
        try:
            async with httpx.AsyncClient() as client:
                await client.post("http://localhost:8083/api/automation/process", json=simulated_data)
                print(f"Simulated Data Pushed: {mock_temp}°C | {mock_hum}%")
        except Exception as e:
            print(f"Could not reach Automation Service: {e}")

# --- Helper Functions ---
def save_sensor_data(data: SensorData):
    record = data.model_dump()
    record["timestamp"] = datetime.now().isoformat()
    sensor_history.append(record)
    return record

def get_all_sensor_data():
    return sensor_history





# # app/services/sensor_service.py
# import httpx
# import asyncio
# from app.models.sensor_model import SensorData
# from datetime import datetime
#
# # In-memory store for the "history" endpoint
# sensor_history = []
# access_token = None
#
# # --- ADD THESE TWO FUNCTIONS BACK ---
#
# def save_sensor_data(data: SensorData):
#     """Saves manually posted sensor data to history."""
#     # Convert Pydantic model to a dict and add a timestamp
#     record = data.model_dump()
#     record["timestamp"] = datetime.now().isoformat()
#     sensor_history.append(record)
#     return record
#
# def get_all_sensor_data():
#     """Returns all stored telemetry records."""
#     return sensor_history
#
# # In-memory store for the "history" endpoint
# sensor_history = []
# access_token = None
#
# async def fetch_external_telemetry():
#     global access_token
#     # 1. Auth Logic (If token is missing)
#     if not access_token:
#         async with httpx.AsyncClient() as client:
#             resp = await client.post("http://104.211.95.241:8080/api/auth/login",
#                                      json={"username": "charuka", "password": "123456"})
#             access_token = resp.json().get("accessToken")
#
#     # 2. Fetch & Push Logic
#     headers = {"Authorization": f"Bearer {access_token}"}
#     async with httpx.AsyncClient() as client:
#         # Get your registered devices
#         dev_resp = await client.get("http://104.211.95.241:8080/api/devices", headers=headers)
#         if dev_resp.status_code == 200 and dev_resp.json():
#             device_id = dev_resp.json()[0].get("deviceId")
#
#             # Get real-time telemetry
#             tel_resp = await client.get(f"http://104.211.95.241:8080/api/devices/telemetry/{device_id}", headers=headers)
#             if tel_resp.status_code == 200:
#                 data = tel_resp.json()
#                 sensor_history.append(data)
#
#                 # PUSH to Automation Service (Port 8083)
#                 await client.post("http://localhost:8083/api/automation/process", json=data)
#                 print(f"Data pushed to Automation: {data['value']['temperature']}°C")