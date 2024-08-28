<script>
    import { goto } from '$app/navigation';
    import { onMount } from 'svelte';

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const accessToken = urlParams.get('access_token');

        if (accessToken) {
            localStorage.setItem('accessToken', accessToken);
            goto('/'); // 메인 페이지로 리다이렉트
        } else {
            console.error('토큰이 전달되지 않았습니다.');
            goto('/sign'); // 오류 발생 시 로그인 페이지로 리다이렉트
        }
    });
</script>

<p>로그인 중입니다...</p>
