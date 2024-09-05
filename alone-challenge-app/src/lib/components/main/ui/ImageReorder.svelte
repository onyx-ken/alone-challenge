<script>
    import { sortableList } from 'svelte-dnd-action';

    export let images = [];

    function handleDrop(event) {
        const { from, to } = event.detail;
        images = [...images];
        images.splice(to, 0, images.splice(from, 1)[0]);
    }
</script>

<div use:sortableList={{ onDrop: handleDrop }} class="grid grid-cols-3 gap-4">
    {#each images as image (image.file)}
        <div class="cursor-pointer">
            <img src={image.preview} alt="uploaded image" class="w-full h-full object-cover rounded-lg" />
        </div>
    {/each}
</div>
