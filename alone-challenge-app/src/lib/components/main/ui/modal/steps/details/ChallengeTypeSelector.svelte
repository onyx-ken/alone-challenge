<script>
    export let challengeType = '';
    export let onSelect = () => {};

    const selectChallengeType = (type) => {
        challengeType = type;
        onSelect(type);
    }
</script>

<div>
    <h4 class="font-semibold mt-4">도전 선택</h4>
    <div class="flex justify-between mt-2">
        <button
                class="btn w-1/2 mr-2 {challengeType === 'positive' ? 'btn-primary' : 'btn-outline'}"
                on:click={() => selectChallengeType('positive')}>
            할래요
        </button>
        <button
                class="btn w-1/2 ml-2 {challengeType === 'negative' ? 'btn-primary' : 'btn-outline'}"
                on:click={() => selectChallengeType('negative')}>
            안할래요
        </button>
    </div>
</div>
