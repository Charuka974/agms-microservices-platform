# app/models/sensor_model.py
from pydantic import BaseModel
from typing import Optional, Dict

class SensorData(BaseModel):
    deviceId: str
    zoneId: str
    value: Dict[str, float] # To capture {"temperature": 25.5, "humidity": 60}
    capturedAt: Optional[str] = None