<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import { isLoggedIn, checkLoginStatus } from '$lib/stores/session';

    export let theme;
    export let toggleTheme;

    // 로그인을 확인할 변수
    let loggedIn;

    // 스토어 구독
    $: loggedIn = $isLoggedIn;

    // 컴포넌트가 마운트되면 로그인 상태를 확인
    onMount(() => {
        checkLoginStatus();
    });

    // 홈으로 이동
    export let onMainClick = () => goto('/');

    // 검색 (구현되어 있지 않으므로 기본적으로 빈 함수)
    export let onSearch = () => {};

    // 프로필 페이지로 이동
    export let onProfileClick = () => {
        if (loggedIn) {
            goto('/accounts/profile');
        }
    };

    // 로그인/로그아웃 페이지로 이동
    export let onLoginClick = () => {
        if (loggedIn) {
            alert('로그아웃 됩니다!');
            localStorage.removeItem('accessToken'); // 토큰 삭제
            checkLoginStatus(); // 상태 업데이트
            goto('/sign'); // 로그인 페이지로 이동
        } else {
            goto('/sign'); // 로그인 페이지로 이동
        }
    };
</script>

<header class="border-b sticky top-0 z-10 shadow-sm bg-base-100">
    <div class="container mx-auto px-4 py-2 flex justify-between items-center">
        <!-- 앱 이름 -->
        <button class="text-xl font-semibold" on:click={onMainClick}>나혼자 챌린지</button>

        <!-- 검색창 -->
        <div class="flex-grow mx-4">
            <input
                    type="text"
                    placeholder="Search"
                    class="input input-bordered w-full"
                    on:input={(e) => onSearch(e.target.value)}
            />
        </div>

        <!-- 아이콘 버튼들 -->
        <div class="flex items-center space-x-4">
            <button class="btn btn-ghost btn-square" on:click={toggleTheme}>
                {#if theme === "lofi"}
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                         stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 3v1m0 16v1m8.66-8.66h-1.66m-14 0H3.34M6.05 6.05l.71.71M18.66 18.66l-.71-.71M6.05 18.66l.71-.71M18.66 6.05l-.71.71M12 5a7 7 0 017 7h-1a6 6 0 10-12 0H5a7 7 0 017-7z"/>
                    </svg>
                {:else}
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                         stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M17.293 17.293a8 8 0 01-11.586 0 8 8 0 0111.586-11.586 8 8 0 010 11.586zM12 3a9 9 0 000 18h0c5.523 0 10-4.477 10-10S17.523 3 12 3z"/>
                    </svg>
                {/if}
            </button>

            <!-- 로그인 상태에서만 프로필 버튼 표시 -->
            {#if loggedIn}
                <button class="btn btn-ghost btn-square" on:click={onProfileClick}>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                         stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                    </svg>
                </button>
            {/if}

            <!-- 로그인/로그아웃 버튼 -->
            <button class="btn btn-ghost btn-square" on:click={onLoginClick}>
                {#if loggedIn}
                    <!-- 로그아웃 아이콘 -->
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                         stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M15 12h7m0 0l-3 3m3-3l-3-3m-9 0v6m4 6h-4c-1.1 0-2-.9-2-2V8c0-1.1.9-2 2-2h4m0 0l-4-4m4 4v4"/>
                    </svg>
                {:else}
                    <!-- 로그인 아이콘 -->
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                         stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M10 14l2-2m0 0l2-2m-2 2v8m8-12H6a2 2 0 00-2 2v12a2 2 0 002 2h12a2 2 0 002-2V10a2 2 0 00-2-2H10z"/>
                    </svg>
                {/if}
            </button>
        </div>
    </div>
</header>
