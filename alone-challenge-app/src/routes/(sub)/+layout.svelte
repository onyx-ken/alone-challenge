<script>
    import "tailwindcss/tailwind.css";
    import Footer from '$lib/components/sub/ui/Footer.svelte';
</script>

<div class="min-h-screen bg-background flex flex-col items-center justify-center p-4">
    <slot />
    <Footer />
</div>
