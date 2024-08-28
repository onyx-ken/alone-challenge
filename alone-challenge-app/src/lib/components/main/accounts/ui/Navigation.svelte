<script>
    import { goto } from '$app/navigation';
</script>

<div class="flex flex-col space-y-4 p-4 bg-base-200 shadow-md rounded-lg">
    <a href="/accounts/profile" class="btn btn-ghost justify-start" on:click|preventDefault={() => goto('/accounts/profile')}>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 10l4.3-4.3c.4-.4.4-1 0-1.4L18 2c-.4-.4-1-.4-1.4 0L12.3 6.3M9 16v4.3c0 .6.4 1 1 1h4c.6 0 1-.4 1-1V16m-8-4V6c0-.6.4-1 1-1h4c.6 0 1 .4 1 1v6m4 0V6c0-.6.4-1 1-1h4c.6 0 1 .4 1 1v6" />
        </svg>
        프로필 변경
    </a>

    <a href="/accounts/notifications" class="btn btn-ghost justify-start" on:click|preventDefault={() => goto('/accounts/notifications')}>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405C18.21 14.79 18 13.897 18 13V8c0-3.314-2.686-6-6-6S6 4.686 6 8v5c0 .897-.21 1.79-.595 2.595L4 17h5m4 0v2a2 2 0 11-4 0v-2m4 0H7" />
        </svg>
        알림
    </a>

    <a href="/accounts/admin-messages" class="btn btn-ghost justify-start" on:click|preventDefault={() => goto('/accounts/admin-messages')}>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16h6m2 0h2a2 2 0 002-2V8a2 2 0 00-2-2H7a2 2 0 00-2 2v6a2 2 0 002 2h2zM9 21h6a2 2 0 002-2v-4H7v4a2 2 0 002 2z" />
        </svg>
        운영진으로부터의 메시지
    </a>

    <a href="/accounts/help" class="btn btn-ghost justify-start" on:click|preventDefault={() => goto('/accounts/help')}>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M12 14h0m0-2h-.01a4 4 0 00-.982 7.87c.643.13 1.312.13 1.955 0A4 4 0 0012 12h0m0-2V8m0 0V6m0-2v4z" />
        </svg>
        도움말
    </a>
</div>

<style>
    a.btn-ghost {
        text-align: left;
    }
</style>
