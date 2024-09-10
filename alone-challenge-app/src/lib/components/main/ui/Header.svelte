<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import { isLoggedIn, checkLoginStatus } from '$lib/stores/session';
    import Modal from "$lib/components/main/ui/Modal.svelte";

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
            <label class="swap swap-rotate btn btn-ghost btn-square">
                <input type="checkbox" on:click={toggleTheme}/>
                <!-- sun icon -->
                <svg
                        class="swap-off h-6 w-6 fill-current"
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24">
                    <path
                            d="M5.64,17l-.71.71a1,1,0,0,0,0,1.41,1,1,0,0,0,1.41,0l.71-.71A1,1,0,0,0,5.64,17ZM5,12a1,1,0,0,0-1-1H3a1,1,0,0,0,0,2H4A1,1,0,0,0,5,12Zm7-7a1,1,0,0,0,1-1V3a1,1,0,0,0-2,0V4A1,1,0,0,0,12,5ZM5.64,7.05a1,1,0,0,0,.7.29,1,1,0,0,0,.71-.29,1,1,0,0,0,0-1.41l-.71-.71A1,1,0,0,0,4.93,6.34Zm12,.29a1,1,0,0,0,.7-.29l.71-.71a1,1,0,1,0-1.41-1.41L17,5.64a1,1,0,0,0,0,1.41A1,1,0,0,0,17.66,7.34ZM21,11H20a1,1,0,0,0,0,2h1a1,1,0,0,0,0-2Zm-9,8a1,1,0,0,0-1,1v1a1,1,0,0,0,2,0V20A1,1,0,0,0,12,19ZM18.36,17A1,1,0,0,0,17,18.36l.71.71a1,1,0,0,0,1.41,0,1,1,0,0,0,0-1.41ZM12,6.5A5.5,5.5,0,1,0,17.5,12,5.51,5.51,0,0,0,12,6.5Zm0,9A3.5,3.5,0,1,1,15.5,12,3.5,3.5,0,0,1,12,15.5Z" />
                </svg>
                <!-- moon icon -->
                <svg
                        class="swap-on h-6 w-6 fill-current"
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24">
                    <path
                            d="M21.64,13a1,1,0,0,0-1.05-.14,8.05,8.05,0,0,1-3.37.73A8.15,8.15,0,0,1,9.08,5.49a8.59,8.59,0,0,1,.25-2A1,1,0,0,0,8,2.36,10.14,10.14,0,1,0,22,14.05,1,1,0,0,0,21.64,13Zm-9.5,6.69A8.14,8.14,0,0,1,7.08,5.22v.27A10.15,10.15,0,0,0,17.22,15.63a9.79,9.79,0,0,0,2.1-.22A8.11,8.11,0,0,1,12.14,19.73Z" />
                </svg>
            </label>

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
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="h-6 w-6">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 9V5.25A2.25 2.25 0 0 0 13.5 3h-6a2.25 2.25 0 0 0-2.25 2.25v13.5A2.25 2.25 0 0 0 7.5 21h6a2.25 2.25 0 0 0 2.25-2.25V15m3 0 3-3m0 0-3-3m3 3H9" />
                    </svg>
                {:else}
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="h-6 w-6">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 9V5.25A2.25 2.25 0 0 1 10.5 3h6a2.25 2.25 0 0 1 2.25 2.25v13.5A2.25 2.25 0 0 1 16.5 21h-6a2.25 2.25 0 0 1-2.25-2.25V15M12 9l3 3m0 0-3 3m3-3H2.25" />
                    </svg>
                {/if}
            </button>
        </div>
    </div>
</header>

{#if isModalOpen}
    <Modal title="새로운 도전하기" onClose={closeModal} onPost={postChallenge} />
{/if}
