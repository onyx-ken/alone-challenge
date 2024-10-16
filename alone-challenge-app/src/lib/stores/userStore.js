import { writable } from 'svelte/store';
import { getApiEndpoints } from "$lib/apiEndpoints.js";

const { USER } = getApiEndpoints();

export const userStore = writable(null);

export async function fetchUserInfo() {
    try {
        // localStorage에서 토큰 가져오기
        const accessToken = localStorage.getItem('accessToken');

        // 토큰이 없으면 함수 종료
        if (!accessToken) {
            console.error('토큰이 존재하지 않습니다.');
            return;
        }

        const response = await fetch(`${USER}/users/me/profile`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`, // Authorization 헤더에 토큰 추가
                'Content-Type': 'application/json' // 필요에 따라 Content-Type 헤더 추가
            }
        });

        if (response.ok) {
            const data = await response.json();
            userStore.set(data);
        } else {
            console.error('사용자 정보를 불러오는데 실패 했습니다.');
        }
    } catch (error) {
        console.error('사용자 정보를 불러오는 중 오류 발생:', error);
    }
}

export async function initUserInfo() {
    let userData = null;

    userStore.subscribe(value => {
        userData = value;
    });

    if (!userData) {
        await fetchUserInfo();
    }
}
