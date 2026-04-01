# main.py
import uvicorn
from fastapi import FastAPI
from contextlib import asynccontextmanager
from apscheduler.schedulers.background import BackgroundScheduler
import py_eureka_client.eureka_client as eureka_client
from app.routes.sensor_routes import router as sensor_router
from app.services.sensor_service import fetch_external_telemetry
import asyncio


def fetch_config():
    try:
        response = requests.get("http://localhost:8888/sensor-service/default")
        config = response.json()
        # Extract values from the Spring Cloud Config JSON format
        # This logic parses the propertySources returned by the Config Server
        props = {}
        for source in config['propertySources']:
            props.update(source['source'])
        return props
    except Exception as e:
        print(f"Config Server Offline: Using local defaults. Error: {e}")
        return {
            "server.port": 8082,
            "eureka.client.service-url.defaultZone": "http://localhost:8761/eureka"
        }

CONFIG = fetch_config()
PORT = int(CONFIG.get("server.port", 8082))

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Use the URL fetched from the Config Server
    eureka_url = CONFIG.get("eureka.client.service-url.defaultZone")

    await eureka_client.init_async(
        eureka_server=eureka_url,
        app_name="sensor-service",
        instance_port=PORT,
        instance_host="127.0.0.1" # Fixed the IP timeout issue from earlier!
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