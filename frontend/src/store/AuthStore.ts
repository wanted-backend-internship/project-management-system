import { defineStore } from 'pinia';

// @ts-ignore
export const useAuthStore = defineStore('auth', {
    state: () => ({
        isLoggedIn: false,
        userInfo: null,
    }),
    actions: {
        setLoginStatus(status: boolean) {
            this.isLoggedIn = status;
        },
        setUserInfo(info: any) {
            this.userInfo = info;
        },
    },
});
