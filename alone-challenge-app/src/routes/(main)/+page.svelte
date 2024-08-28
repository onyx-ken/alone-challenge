<script>
	import Feed from '../../lib/components/main/Feed.svelte';
</script>

<main class="main-layout">
    <Feed/>
</main>

<style>
    .main-layout {
        max-width: 60%;
        margin: 0 auto;
        padding: 1rem;
    }
    @media (max-width: 768px) {
        .main-layout {
            max-width: 90%;
        }
    }
</style>