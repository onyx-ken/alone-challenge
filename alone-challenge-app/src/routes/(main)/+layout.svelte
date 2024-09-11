<script>
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
