# app/services/sensor_service.py
from datetime import datetime

sensor_history = []

def save_sensor_data(data):
    record = {
        "zoneId": data.zoneId,
        "temperature": data.temperature,
        "humidity": data.humidity,
        "timestamp": datetime.now()
    }
    sensor_history.append(record)
    return record

def get_all_sensor_data():
    return sensor_history