<svelte:options accessors/>
<script>
    import { onMount, onDestroy } from 'svelte';
    import { initUserInfo, userStore } from "$lib/stores/userStore.js";
    import DateRangePicker from './details/DateRangePicker.svelte';
    import ChallengeTypeSelector from './details/ChallengeTypeSelector.svelte';
    import ChallengeCategorySelector from './details/ChallengeCategorySelector.svelte';
    import ChallengeSummary from './details/ChallengeSummary.svelte';

    export let dateRange = { from: new Date(), to: new Date(new Date().setDate(new Date().getDate() + 7)) };
    export let challengeType = '';
    export let challengeCategory = null;
    export let challengeTarget = '';  // 사용자가 입력할 도전 대상
    export let nickName = '';

    let unsubscribe;

    onMount(() => {
        initUserInfo()
        unsubscribe = userStore.subscribe(userData => {
            if (userData) {
                ({nickName} = userData);
            } else {
                console.error('사용자 정보가 없습니다.');
            }
        });
    });

    onDestroy(() => {
        if (unsubscribe) {
            unsubscribe();
        }
    });

    const handleDateChange = (newDateRange) => {
        dateRange = newDateRange;
    }

    const handleTypeSelect = (type) => {
        challengeType = type;
        // 도전 유형이 변경되면 카테고리를 초기화
        challengeCategory = null;
    }

    const handleCategorySelect = (category) => {
        challengeCategory = category;
    }

    const handleTargetChange = (target) => {
        challengeTarget = target;
    }
</script>

<div class="relative">
    <!-- 날짜 범위 선택 -->
    <DateRangePicker {dateRange} onChange={handleDateChange} />

    <!-- 도전 유형 선택 -->
    <ChallengeTypeSelector {challengeType} onSelect={handleTypeSelect} />

    <!-- 도전 카테고리 선택 -->
    {#if challengeType}
        <ChallengeCategorySelector
                {challengeType}
                {challengeCategory}
                onSelect={handleCategorySelect}
        />
    {/if}

    <!-- 도전 요약 및 대상 입력 -->
    <ChallengeSummary
            {nickName}
            {dateRange}
            {challengeType}
            {challengeCategory}
            bind:challengeTarget
            onTargetChange={handleTargetChange}
    />
</div>
