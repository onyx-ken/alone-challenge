// session.js
import { writable } from 'svelte/store';

export const isLoggedIn = writable(false);

export function checkLoginStatus() {
    const accessToken = localStorage.getItem('accessToken');
    isLoggedIn.set(!!accessToken);
}
