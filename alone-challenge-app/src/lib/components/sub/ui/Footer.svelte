<script>
    const links = [
        { href: "/about", label: "About" },
        { href: "/privacy", label: "Privacy" },
        { href: "/terms", label: "Terms" }
    ];
</script>

<footer class="mt-8 text-center text-sm text-muted-foreground">
    <nav class="space-x-4" aria-label="Footer navigation">
        {#each links as { href, label }}
            <a href={href} class="hover:underline">{label}</a>
        {/each}
    </nav>
    <p class="mt-4">© 2024 Onyx.DEV</p>
</footer>
