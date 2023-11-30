import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia';
import BootstrapVue3 from 'bootstrap-vue-3';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Router from './router';

const app = createApp(App);

app
    .use(createPinia())
    .use(BootstrapVue3)
    .use(Router)
    .mount('#app')
