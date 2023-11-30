import { createWebHistory, createRouter } from 'vue-router';
import { useAuthStore } from '../store/AuthStore.ts';

const routes = [
    {
        path: '/login',
        component: () => import('../components/user/TheLogin.vue'),
    },
    {
        path: '/register',
        component: () => import('../components/user/TheRegister.vue'),
    },
    {
        path:'/project',
        component:() => import('../components/project/board/BoardList.vue'),
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();

    if (!authStore.isLoggedIn && to.path !== '/login' && to.path !== '/register') {
        next('/login');
    } else {
        next();
    }
});

export default router;
