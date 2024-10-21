<script>
    export let challengeType = '';
    export let challengeCategory = null;
    export let onSelect = () => {};

    const positiveCategories = [
        { action: '하기', ending: '하겠습니다' },
        { action: '먹기', ending: '먹겠습니다' },
        { action: '마시기', ending: '마시겠습니다' },
        { action: '보기', ending: '보겠습니다' },
    ];

    const negativeCategories = [
        { action: '하지 않기', ending: '하지 않겠습니다' },
        { action: '먹지 않기', ending: '먹지 않겠습니다' },
        { action: '마시지 않기', ending: '마시지 않겠습니다' },
        { action: '보지 않기', ending: '보지 않겠습니다' },
    ];

    const selectCategory = (category) => {
        challengeCategory = category;
        onSelect(category);
    }
</script>

{#if challengeType === 'positive'}
    <div class="mt-4">
        <h5 class="text-md font-semibold">긍정 도전 선택</h5>
        <div class="grid grid-cols-2 gap-2 mt-2">
            {#each positiveCategories as category}
                <button
                        class="btn w-full {challengeCategory === category ? 'btn-primary' : 'btn-outline'}"
                        on:click={() => selectCategory(category)}>
                    {category.ending}
                </button>
            {/each}
        </div>
    </div>
{:else if challengeType === 'negative'}
    <div class="mt-4">
        <h5 class="text-md font-semibold">부정 도전 선택</h5>
        <div class="grid grid-cols-2 gap-2 mt-2">
            {#each negativeCategories as category}
                <button
                        class="btn w-full {challengeCategory === category ? 'btn-primary' : 'btn-outline'}"
                        on:click={() => selectCategory(category)}>
                    {category.ending}
                </button>
            {/each}
        </div>
    </div>
{/if}
