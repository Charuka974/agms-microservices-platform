import dotenv from "dotenv";
dotenv.config();

import express, { Request, Response } from "express";
import cors from "cors";

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

interface ControlRequest {
  zoneId: number;
  status: "ON" | "OFF";
}

interface AlertRequest {
  zoneId: number;
  message: string;
}

// Health check
app.get("/", (req: Request, res: Response) => {
  res.json({ service: "Automation Service Running" });
});

// Fan control
app.post("/control/fan", (req: Request, res: Response) => {
  const { zoneId, status }: ControlRequest = req.body;

  res.json({
    action: "Fan Control",
    zoneId,
    fanStatus: status,
    message: `Fan turned ${status} for zone ${zoneId}`
  });
});

// Irrigation control
app.post("/control/irrigation", (req: Request, res: Response) => {
  const { zoneId, status }: ControlRequest = req.body;

  res.json({
    action: "Irrigation Control",
    zoneId,
    irrigationStatus: status,
    message: `Irrigation turned ${status} for zone ${zoneId}`
  });
});

// Alert trigger
app.post("/alert", (req: Request, res: Response) => {
  const { zoneId, message }: AlertRequest = req.body;

  res.json({
    alert: true,
    zoneId,
    message
  });
});

app.listen(PORT, () => {
  console.log(`Automation Service running on port ${PORT}`);
});