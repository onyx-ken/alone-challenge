<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import { isLoggedIn, checkLoginStatus } from '$lib/stores/session';
    import Modal from "$lib/components/main/ui/Modal.svelte";
    import SunIcon from "$lib/components/icon/SunIcon.svelte";
    import MoonIcon from "$lib/components/icon/MoonIcon.svelte";
    import ProfileIcon from "$lib/components/icon/ProfileIcon.svelte";
    import PostIcon from "$lib/components/icon/PostIcon.svelte";
    import LoginIcon from "$lib/components/icon/LoginIcon.svelte";
    import LogOutIcon from "$lib/components/icon/LogOutIcon.svelte";

    export let toggleTheme;

    let loggedIn;
    let isModalOpen = false;

    $: loggedIn = $isLoggedIn;

    onMount(() => {
        checkLoginStatus();
    });

    const onMainClick = () => {
        goto('/');
    }

    const onProfileClick = () => {
        if (loggedIn) goto('/accounts/profile');
    }

    const onLoginClick = () => {
        if (loggedIn) {
            alert('로그아웃 됩니다!');
            localStorage.removeItem('accessToken');
            checkLoginStatus();
            goto('/sign');
        } else {
            goto('/sign');
        }
    }

    const openModal = () => {
        isModalOpen = true;
    }

    const closeModal = () => {
        isModalOpen = false;
    }

    const postChallenge = (data) => {
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
                <SunIcon />
                <MoonIcon />
            </label>

            {#if loggedIn}
                <button class="btn btn-ghost btn-square" on:click={onProfileClick}>
                    <ProfileIcon />
                </button>
                <button class="btn btn-ghost btn-square" on:click={openModal}>
                    <PostIcon />
                </button>
            {/if}

            <button class="btn btn-ghost btn-square" on:click={onLoginClick}>
                {#if loggedIn}
                    <LoginIcon />
                {:else}
                    <LogOutIcon />
                {/if}
            </button>
        </div>
    </div>
</header>

{#if isModalOpen}
    <Modal title="새로운 도전하기" onClose={closeModal} onPost={postChallenge} />
{/if}
