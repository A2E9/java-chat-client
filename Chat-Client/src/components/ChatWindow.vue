<template>
  <div>
    <h1>Chat Room {{ $route.params.id }}</h1>
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
  name: "ChatWindow",
  data() {
    return {
      newMessage: '',
      messages: [],
      socket: null,
      username: localStorage.getItem('username')  // Fetch username from localStorage
    };
  },
  mounted() {
    this.connectToWebSocket();
  },
  methods: {
    connectToWebSocket() {
      this.socket = new WebSocket('ws://192.168.8.141:8001');

      this.socket.onmessage = (event) => {
        let message;
        try {
          message = JSON.parse(event.data);
        } catch (e) {
          message = { username: 'Server', text: event.data };
        }
        this.messages.push(message);
      };

      this.socket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };

      this.socket.onclose = () => {
        console.log('WebSocket connection closed.');
      };
    },
    sendMessage() {
      if (this.newMessage.trim() !== '') {
        this.socket.send(this.newMessage);

        const message = {
          username: this.username,  // Use the stored username
          text: this.newMessage,
        };
        this.messages.push(message);
        this.newMessage = '';
      }
    }
  },
  beforeDestroy() {
    if (this.socket) {
      this.socket.close();
    }
  }
};
</script>

<style scoped>
.chat-container {
  width: 70vw;
  height: 90vh;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
}

.chat-box {
  flex-grow: 1;
  height: calc(90vh - 100px);
  overflow-y: auto;
  border-bottom: 1px solid var(--color-divider-light-1);
  padding: 10px;
  background-color: var(--color-white-soft);
}

.message {
  margin-bottom: 10px;
  color: var(--color-text-light);
}

.input-area {
  display: flex;
  justify-content: space-between;
  padding: 10px;
}
</style>
