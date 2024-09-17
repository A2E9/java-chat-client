// Import Vue and Vue Router (Vue 3 syntax)
import { createRouter, createWebHistory } from 'vue-router';

// Import components
import LoginPage from './components/LoginPage.vue';
import ChatWindow from './components/ChatWindow.vue';

// Define the routes
const routes = [
  {
    path: '/',
    name: 'login',
    component: LoginPage,
  },
  {
    path: '/chat/:id',
    name: 'Chat',
    component: ChatWindow,
  },
];

// Create the router instance (Vue 3 syntax)
const router = createRouter({
  history: createWebHistory(),  // Using HTML5 history mode
  routes,
});

// Navigation guard for login
router.beforeEach((to, from, next) => {
  const username = localStorage.getItem('username');
  if (!username && to.name !== 'login') {
    next({ name: 'login' });
  } else {
    next();
  }
});

export default router;
