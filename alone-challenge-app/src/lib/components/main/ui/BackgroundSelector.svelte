<script>
    import { createEventDispatcher } from 'svelte';
    const dispatch = createEventDispatcher();

    export let backgrounds = ["/background1.png", "/background2.png", "/background3.png"];
    export let selectedBackground = backgrounds[0];

    const selectBackground = (background) => {
        selectedBackground = background;
        console.log("Selected background:", selectedBackground); // 디버깅용
        dispatch('select', selectedBackground); // 선택된 배경을 상위 컴포넌트로 전달
    }
</script>

<div class="bg-selector">
    <div class="flex space-x-4">
        {#each backgrounds as bg}
            <div class="cursor-pointer p-2 border {selectedBackground === bg ? 'border-blue-500' : 'border-gray-300'}">
                <img src={bg} alt="background image" class="w-20 h-20 object-cover" on:click={() => selectBackground(bg)} />
            </div>
        {/each}
    </div>
</div>

<style>
    .bg-selector img {
        transition: border 0.2s ease;
    }
</style>
