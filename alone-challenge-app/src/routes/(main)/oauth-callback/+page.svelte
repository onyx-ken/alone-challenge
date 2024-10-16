<script>
    import { goto } from '$app/navigation';
    import { onMount } from 'svelte';
    import { checkLoginStatus } from '$lib/stores/session';
    import { initUserInfo } from "$lib/stores/userStore.js";

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const accessToken = urlParams.get('access_token');

        if (accessToken) {
            localStorage.setItem('accessToken', accessToken);
            checkLoginStatus(); // 로그인 상태 업데이트

            initUserInfo().then(() => {
                goto('/'); // 메인 페이지로 리다이렉트
            }).catch(() => {
                alert('사용자 정보를 불러오는데 실패 했습니다.');
                goto('/sign'); // 오류 발생 시 로그인 페이지로 리다이렉트
            });
        } else {
            console.error('토큰이 전달되지 않았습니다.');
            goto('/sign'); // 오류 발생 시 로그인 페이지로 리다이렉트
        }
    });
</script>

<p>로그인 중입니다...</p>
