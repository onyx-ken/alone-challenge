<script>
    import "tailwindcss/tailwind.css";
    import { onMount } from "svelte";
    import { browser } from '$app/environment';

    let theme = "lofi";

    if (browser) {
        onMount(() => {
            const savedTheme = localStorage.getItem("theme");
            if (savedTheme) {
                theme = savedTheme;
                document.documentElement.setAttribute("data-theme", theme);
            }
        });
    }
</script>

<svelte:head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
</svelte:head>

<slot />

<style>
    :global(html) {
        overflow-y: scroll; /*스크롤 바 항상 표시 (CSS 깨짐 방지)*/
    }
    :global(body) {
         font-family: 'Noto Sans KR', sans-serif;
     }
</style>
