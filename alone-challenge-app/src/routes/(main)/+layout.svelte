<script>
    import "tailwindcss/tailwind.css";
    import Header from "$lib/components/main/ui/Header.svelte";
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

    function toggleTheme() {
        theme = theme === "lofi" ? "business" : "lofi";
        document.documentElement.setAttribute("data-theme", theme);
        localStorage.setItem("theme", theme);
    }
</script>

<Header {theme} {toggleTheme} />
<slot />

<style>
    :global(html) {
        overflow-y: scroll; /*스크롤 바 항상 표시 (CSS 깨짐 방지)*/
    }
</style>
