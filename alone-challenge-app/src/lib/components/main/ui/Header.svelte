<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import { isLoggedIn, checkLoginStatus } from '$lib/stores/session';
    import Modal from "$lib/components/main/ui/Modal.svelte";

    export let theme;
    export let toggleTheme;

    let loggedIn;
    let isModalOpen = false;

    $: loggedIn = $isLoggedIn;

    onMount(() => {
        checkLoginStatus();
    });

    function onMainClick() {
        goto('/');
    }

    function onProfileClick() {
        if (loggedIn) goto('/accounts/profile');
    }

    function onLoginClick() {
        if (loggedIn) {
            alert('로그아웃 됩니다!');
            localStorage.removeItem('accessToken');
            checkLoginStatus();
            goto('/sign');
        } else {
            goto('/sign');
        }
    }

    function openModal() {
        isModalOpen = true;
    }

    function closeModal() {
        isModalOpen = false;
    }

    function postChallenge(data) {
        console.log("Challenge Data:", data);
        alert("도전이 성공적으로 포스팅되었습니다!");
        closeModal();
    }
</script>

<header class="border-b sticky top-0 z-10 shadow-sm bg-base-100">
    <div class="container mx-auto px-4 py-2 flex justify-between items-center">
        <button class="text-xl font-semibold" on:click={onMainClick}>나혼자 챌린지</button>

        <div class="flex-grow mx-4">
            <input
                    type="text"
                    placeholder="Search"
                    class="input input-bordered w-full"
            />
        </div>

        <div class="flex items-center space-x-4">
            <button class="btn btn-ghost btn-square" on:click={toggleTheme}>
                {#if theme === "lofi"}
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 3v1m0 16v1m8.66-8.66h-1.66m-14 0H3.34M6.05 6.05l.71.71M18.66 18.66l-.71-.71M6.05 18.66l.71-.71M18.66 6.05l-.71.71M12 5a7 7 0 017 7h-1a6 6 0 10-12 0H5a7 7 0 017-7z"/>
                    </svg>
                {:else}
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M17.293 17.293a8 8 0 01-11.586 0 8 8 0 0111.586-11.586 8 8 0 010 11.586zM12 3a9 9 0 000 18h0c5.523 0 10-4.477 10-10S17.523 3 12 3z"/>
                    </svg>
                {/if}
            </button>

            {#if loggedIn}
                <button class="btn btn-ghost btn-square" on:click={onProfileClick}>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                    </svg>
                </button>
                <button class="btn btn-ghost btn-square" on:click={openModal}>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2" stroke="currentColor" stroke-width="2" fill="none"/>
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 8v8m-4-4h8"/>
                    </svg>
                </button>
            {/if}

            <button class="btn btn-ghost btn-square" on:click={onLoginClick}>
                {#if loggedIn}
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M15 12h7m0 0l-3 3m3-3l-3-3m-9 0v6m4 6h-4c-1.1 0-2-.9-2-2V8c0-1.1.9-2 2-2h4m0 0l-4-4m4 4v4"/>
                    </svg>
                {:else}
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M9 12h8m0 0l-6 6m6-6l-6-6M3 12a9 9 0 1018 0 9 9 0 10-18 0z"/>
                    </svg>
                {/if}
            </button>
        </div>
    </div>
</header>

{#if isModalOpen}
    <Modal title="새로운 도전하기" onClose={closeModal} onPost={postChallenge} />
{/if}
