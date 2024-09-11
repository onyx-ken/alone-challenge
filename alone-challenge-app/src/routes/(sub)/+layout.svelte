<script>
    import "tailwindcss/tailwind.css";
    import Footer from '$lib/components/sub/ui/Footer.svelte';
</script>

<svelte:head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
</svelte:head>

<div class="min-h-screen bg-background flex flex-col items-center justify-center p-4">
    <slot />
    <Footer />
</div>

<style>
    :global(body) {
        font-family: 'Noto Sans KR', sans-serif;
    }
</style>
