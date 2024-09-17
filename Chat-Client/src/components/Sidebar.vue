<template>
  <div class="sidebar" v-show="!isLoginPage">
    <h2>Sidebar</h2>
    <ul>
      <li><router-link to="/chat">Chat</router-link></li>
      <!-- Add more sidebar items here -->
    </ul>
  </div>
</template>

<script>
import { computed } from 'vue';
import { useRoute } from 'vue-router';

export default {
  name: 'Sidebar',
  setup() {
    const route = useRoute();
    const isLoginPage = computed(() => route.name === 'Login');
    
    return { isLoginPage };
  }
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  height: 100vh;
  background-color: #333;
  color: white;
  padding: 20px;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  flex-direction: column;
}

.sidebar h2 {
  color: #fff;
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
}

.sidebar ul li {
  padding: 10px;
  border-bottom: 1px solid #444;
}

.sidebar ul li a {
  color: #fff;
  text-decoration: none;
  display: block;
}

.sidebar ul li a:hover {
  background-color: #575757;
}
</style>
