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
import axios from 'axios';

export default {
    name: "Chat",
  data() {
    return {
      newMessage: '',
      messages: [],
    };
  },
  mounted() {
    this.fetchMessages();
    setInterval(this.fetchMessages, 5000);
  },
  methods: {
    fetchMessages() {
      axios.get('/messages.json')
      .then(response => {
        this.messages = response.data;
      })
      .catch(error => {
        console.error('Error fetching message:', error);
      });
    },
    sendMessage() {
      if (this.newMessage.trim() !== '') {
        const newMessage = {
          username: 'You',
          text: this.newMessage,
        };

        axios.post('/messages.json', newMessage)
          .then(() => {
            this.messages.push(newMessage);
            this.newMessage = ''; // Clear input after sending
          })
          .catch(error => {
            console.error('Error saving message:', error);
          });
      }
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
