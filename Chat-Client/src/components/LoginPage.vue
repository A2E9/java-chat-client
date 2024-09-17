<template>
  <div class="login-container">
    <div class="login-box">
      <h1>Login</h1>
      <input type="text" placeholder="Enter username" v-model="username" />
      <button @click="login">Login</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
    };
  },
  methods: {
    login() {
      // Handle login logic
      if (this.username.trim()) {
        const chatId = '123';
        this.$router.push({ name: 'Chat', params: { id: chatId } }); // Navigate to chat page after login
      }
    },
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
}
</style>
