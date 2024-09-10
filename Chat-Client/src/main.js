import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createRouter, createWebHistory } from 'vue-router';

import App from './App.vue'
//import router from './router'
import Landing from './pages/Landing.vue'
import Chat from './pages/Chat.vue'
import ChatWebsocket from './pages/ChatSocket.vue'

const app = createApp(App)

const routes = [
    { path: '/', component: Landing },
    { path: '/chat', component: Chat },
    { path: '/ChatWebsocket', component: ChatWebsocket}
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

app.use(createPinia())
app.use(router)

app.mount('#app')
