<template>
  <div id="app">
    <Sidebar />
    <div :class="['main-content', { 'full-width': isLoginPage }]">
      <router-view />
    </div>
  </div>
</template>

<script>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import Sidebar from './components/Sidebar.vue'; // Adjust path as needed

export default {
  components: {
    Sidebar,
  },
  setup() {
    const route = useRoute();
    const isLoginPage = computed(() => route.name === 'Login');

    return { isLoginPage };
  },
};
</script>

<style>
#app {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.main-content {
  flex-grow: 1;
  padding: 20px;
  margin-left: 250px; /* Adjust margin to account for sidebar width */
  transition: margin-left 0.3s ease; /* Smooth transition for width change */
}

.full-width {
  margin-left: 0; /* No margin for login page */
}
</style>
