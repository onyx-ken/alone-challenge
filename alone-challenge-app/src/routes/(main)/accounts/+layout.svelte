<script>
    import Navigation from "$lib/components/main/accounts/ui/Navigation.svelte";
</script>

<main class="container mx-auto flex justify-center space-x-4 mt-8">
    <div class="w-1/4">
        <Navigation />
    </div>
    <div class="container mx-auto p-4 max-w-lg">
        <slot />
    </div>
</main>
