<template>
  <div id="app">
    <h1>Simple Chat App</h1>
    <div class="chat-container">
      <div class="chat-box">
        <div v-for="(message, index) in messages" :key="index" class="message">
          <p><strong>{{ message.username }}:</strong> {{ message.text }}</p>
        </div>
      </div>
      <div class="input-area">
        <input 
          v-model="newMessage" 
          @keyup.enter="sendMessage" 
          placeholder="Type a message..." 
        />
        <button @click="sendMessage">Send</button>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: "ChatWebsocket",
  data() {
    return {
      newMessage: '',
      messages: [],
      socket: null,  // This will hold the WebSocket connection
    };
  },
  mounted() {
    // Establish the WebSocket connection
    this.connectToWebSocket();
  },
  methods: {
    connectToWebSocket() {
      // Replace 'ws://your-websocket-server' with your actual WebSocket server URL
      this.socket = new WebSocket('ws://192.168.8.141:8001');

      // Listen for messages from the WebSocket server
      this.socket.onmessage = (event) => {
  // Assuming the message is plain text
  const message = event.data;  // No need to JSON.parse if it's not JSON
  console.log('Received plain text message:', message);

  // Push the received message to the messages array
  this.messages.push({ username: 'Server', text: message });
};

      // Handle connection errors
      this.socket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };

      // Handle WebSocket connection closure
      this.socket.onclose = () => {
        console.log('WebSocket connection closed.');
      };
    },
    sendMessage() {
    if (this.newMessage.trim() !== '') {
      // Send the message as plain text (just the message text, no JSON)
      this.socket.send(this.newMessage);

      // Add the message to the chat for the sender's side (optional if you want instant feedback)
      const message = {
        username: 'Bensch',  // Or use a dynamic username if necessary
        text: this.newMessage,
      };
      this.messages.push(message);

      // Clear the input field after sending the message
      this.newMessage = '';
    }
  }
  },
  beforeDestroy() {
    // Clean up WebSocket connection when the component is destroyed
    if (this.socket) {
      this.socket.close();
    }
  }
};
</script>


<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
}

.chat-container {
  width: 80%;
  max-height: 80%;
  margin: 0 auto;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
  background-color: #f9f9f9;
}

.chat-box {
  overflow-y: auto;
  border-bottom: 1px solid #ddd;
  padding: 10px;
}

.message {
  margin-bottom: 10px;
  border: #000;
  color: #000;
}

.input-area {
  display: flex;
  justify-content: space-between;
  padding: 10px;
}

input {
  width: 80%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  color: #000;
}

button {
  width: 18%;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>
