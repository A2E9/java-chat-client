<template>
  <div>
    <h1>Welcome to the Simple Chat App</h1>
    <router-link to="/chat">
      <button>Go to Chat</button>
    </router-link>
    <router-link to="/ChatWebsocket">
      <button>Go to Websocket</button>
    </router-link>
  </div>
</template>

<script>
export default {
  name: "Landing"
};
</script>

<style scoped>
h1 {
  text-align: center;
}
button {
  display: block;
  margin: 20px auto;
  padding: 10px 20px;
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
