# app/routes/sensor_routes.py
from fastapi import APIRouter
from app.models.sensor_model import SensorData
from app.services.sensor_service import save_sensor_data, get_all_sensor_data

router = APIRouter()

@router.post("/data")
def receive_sensor_data(data: SensorData):
    # This calls the function we just added back to the service
    record = save_sensor_data(data)
    return {"message": "Sensor data stored successfully", "data": record}

@router.get("/history")
def get_sensor_history():
    # This returns the sensor_history list from the service
    return get_all_sensor_data()