from fastapi import FastAPI
from app.routes.sensor_routes import router as sensor_router
import os
from dotenv import load_dotenv

load_dotenv()

PORT = int(os.getenv("PORT", 5001))

app = FastAPI(title="Sensor Service", version="1.0")
app.include_router(sensor_router, prefix="/sensor")