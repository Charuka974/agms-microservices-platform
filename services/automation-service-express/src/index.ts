import express from 'express';
import type { Request, Response } from 'express';
import axios from 'axios';
import { Eureka } from 'eureka-js-client';

const app = express();
app.use(express.json());

const PORT = 8083; // Port specified for Automation Service

// --- 1. Interfaces ---
interface TelemetryData {
    deviceId: string;
    zoneId: string;
    temperature: number; // Simplified for rule engine processing [cite: 143]
    humidity?: number;
}

interface ZoneThresholds {
    minTemp: number;
    maxTemp: number;
}

interface ActionLog {
    timestamp: string;
    zoneId: string;
    temperature: number;
    action: string;
}

// In-memory log for the farmer to view [cite: 150]
const actionLogs: ActionLog[] = [];

// --- 2. Eureka Registration [cite: 10, 116] ---
const client = new Eureka({
    instance: {
        app: 'AUTOMATION-SERVICE',
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        port: { '$': PORT, '@enabled': true },
        vipAddress: 'AUTOMATION-SERVICE',
        dataCenterInfo: { '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo', name: 'MyOwn' },
    },
    eureka: {
        host: 'localhost', // Eureka Server Host
        port: 8761,
        servicePath: '/eureka/apps/',
    },
});
client.start();

// --- 3. Rule Engine Logic [cite: 138, 149] ---
app.post('/api/automation/process', async (req: Request, res: Response): Promise<any> => {
    const { zoneId, temperature } = req.body;

    if (!zoneId || temperature === undefined) {
        return res.status(400).json({ error: "Missing zoneId or temperature" });
    }

    try {
        // Step 1: Fetch thresholds from Zone Service (Port 8081) [cite: 120, 144]
        // In a real microservice env, you'd use the Gateway or Eureka name
        const zoneResponse = await axios.get<ZoneThresholds>(`http://localhost:8081/api/zones/${zoneId}`);
        const { minTemp, maxTemp } = zoneResponse.data;

        let action = "KEEP_STABLE";

        // Step 2: Apply Decision Rules
        if (temperature > maxTemp) {
            action = "TURN_FAN_ON";
        } else if (temperature < minTemp) {
            action = "TURN_HEATER_ON";
        }

        // Step 3: Log the action if a change was triggered
        if (action !== "KEEP_STABLE") {
            const newLog = {
                timestamp: new Date().toISOString(),
                zoneId,
                temperature,
                action
            };
            actionLogs.push(newLog);
            console.log(`[RULE TRIGGERED] Zone: ${zoneId} | Action: ${action}`);
        }

        return res.status(200).json({ status: "Processed", action });

    } catch (error: any) {
        console.error("Failed to fetch thresholds from Zone Service:", error.message);
        return res.status(500).json({ error: "Inter-service communication failed" });
    }
});

// --- 4. Logs Endpoint [cite: 150] ---
app.get('/api/automation/logs', (req: Request, res: Response) => {
    res.status(200).json(actionLogs);
});

app.listen(PORT, () => {
    console.log(`Brain (Automation Service) active on port ${PORT}`);
});