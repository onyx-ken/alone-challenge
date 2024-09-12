<script>
    import Navigation from "$lib/components/main/accounts/ui/Navigation.svelte";
</script>

<main class="container mx-auto flex flex-col md:flex-row justify-center space-y-4 md:space-y-0 md:space-x-4 mt-8">
    <!-- Navigation (좌측) -->
    <div class="w-full md:w-1/4">
        <Navigation />
    </div>
    <!-- Slot (메인 콘텐츠) -->
    <div class="w-full md:w-3/4 p-4 max-w-lg">
        <slot />
    </div>
</main>
