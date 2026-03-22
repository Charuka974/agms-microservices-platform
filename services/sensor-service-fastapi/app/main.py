# main.py
import uvicorn
from fastapi import FastAPI
from contextlib import asynccontextmanager
from apscheduler.schedulers.background import BackgroundScheduler
import py_eureka_client.eureka_client as eureka_client
from app.routes.sensor_routes import router as sensor_router
from app.services.sensor_service import fetch_external_telemetry
import asyncio

PORT = 8082

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Register with Eureka
    await eureka_client.init_async(
        eureka_server="http://localhost:8761/eureka",
        app_name="SENSOR-SERVICE",
        instance_port=PORT
    )

    # Start the 10-second background task
    scheduler = BackgroundScheduler()
    # Using a helper to bridge Sync Scheduler to Async Function
    scheduler.add_job(lambda: asyncio.run(fetch_external_telemetry()), 'interval', seconds=10)
    scheduler.start()

    yield
    scheduler.shutdown()

app = FastAPI(title="Sensor Telemetry Service", lifespan=lifespan)
app.include_router(sensor_router, prefix="/api/sensors")

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=PORT)