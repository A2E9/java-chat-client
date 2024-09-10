// Use ES module import syntax
import express from 'express';
import fs from 'fs';
import path from 'path';
import cors from 'cors';
import { fileURLToPath } from 'url';

// Necessary for using __dirname with ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = 3000;

// Middleware to parse JSON bodies
app.use(express.json());

// Serve static files from the public directory (if needed)
app.use(express.static('public'));

app.use(cors());
// Get the chat messages from messages.json
app.get('/messages', (req, res) => {
  const filePath = path.join(__dirname, 'public', 'messages.json');
  fs.readFile(filePath, 'utf8', (err, data) => {
    if (err) {
      return res.status(500).json({ error: 'Failed to read messages' });
    }
    res.json(JSON.parse(data));
  });
});

// Post new messages and append to messages.json
app.post('/messages', (req, res) => {
  const newMessage = req.body;
  const filePath = path.join(__dirname, 'public', 'messages.json');

  fs.readFile(filePath, 'utf8', (err, data) => {
    if (err) {
      return res.status(500).json({ error: 'Failed to read messages' });
    }

    const messages = JSON.parse(data);
    messages.push(newMessage);

    fs.writeFile(filePath, JSON.stringify(messages, null, 2), (err) => {
      if (err) {
        return res.status(500).json({ error: 'Failed to save message' });
      }
      res.status(201).json({ message: 'Message saved successfully' });
    });
  });
});

// Start the server
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
