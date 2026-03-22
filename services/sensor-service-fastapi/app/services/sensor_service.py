# app/services/sensor_service.py
import httpx
import asyncio
from app.models.sensor_model import SensorData
from datetime import datetime

# In-memory store for the "history" endpoint
sensor_history = []
access_token = None

# --- ADD THESE TWO FUNCTIONS BACK ---

def save_sensor_data(data: SensorData):
    """Saves manually posted sensor data to history."""
    # Convert Pydantic model to a dict and add a timestamp
    record = data.model_dump() 
    record["timestamp"] = datetime.now().isoformat()
    sensor_history.append(record)
    return record

def get_all_sensor_data():
    """Returns all stored telemetry records."""
    return sensor_history

# In-memory store for the "history" endpoint
sensor_history = []
access_token = None

async def fetch_external_telemetry():
    global access_token
    # 1. Auth Logic (If token is missing)
    if not access_token:
        async with httpx.AsyncClient() as client:
            resp = await client.post("http://104.211.95.241:8080/api/auth/login",
                                     json={"username": "hasindu", "password": "123456"})
            access_token = resp.json().get("accessToken")

    # 2. Fetch & Push Logic
    headers = {"Authorization": f"Bearer {access_token}"}
    async with httpx.AsyncClient() as client:
        # Get your registered devices
        dev_resp = await client.get("http://104.211.95.241:8080/api/devices", headers=headers)
        if dev_resp.status_code == 200 and dev_resp.json():
            device_id = dev_resp.json()[0].get("deviceId")

            # Get real-time telemetry
            tel_resp = await client.get(f"http://104.211.95.241:8080/api/devices/telemetry/{device_id}", headers=headers)
            if tel_resp.status_code == 200:
                data = tel_resp.json()
                sensor_history.append(data)

                # PUSH to Automation Service (Port 8083)
                await client.post("http://localhost:8083/api/automation/process", json=data)
                print(f"Data pushed to Automation: {data['value']['temperature']}°C")