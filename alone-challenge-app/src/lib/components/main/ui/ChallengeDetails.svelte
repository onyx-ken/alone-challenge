<script>
    import Flatpickr from "flatpickr";
    import "flatpickr/dist/themes/airbnb.css";
    import { Korean } from "flatpickr/dist/l10n/ko.js";
    import { format } from 'date-fns';
    import { onMount } from 'svelte';


    export let onDetailsSave = () => {};

    let dateRange = { from: new Date(), to: new Date(new Date().setDate(new Date().getDate() + 7)) };
    let challengeType = '';
    let challengeCategory = '';
    let challengeTarget = '';  // 사용자가 입력할 도전 대상
    let nickname = "xxx";  // 추후 백엔드에서 불러올 닉네임
    let flatpickrInput;
    let flatpickrInstance;

    const positiveCategories = ['하겠습니다', '먹겠습니다', '마시겠습니다', '보겠습니다'];
    const negativeCategories = ['하지 않겠습니다', '먹지 않겠습니다', '마시지 않겠습니다', '보지 않겠습니다'];

    function formatDate(date) {
        return format(date, 'yyyy.MM.dd');
    }

    function handleSave() {
        onDetailsSave({
            startDate: dateRange.from,
            endDate: dateRange.to,
            challengeType,
            challengeCategory,
            challengeTarget
        });
    }

    // Flatpickr 옵션 설정
    let options = {
        mode: "range",
        dateFormat: "Y.m.d",
        defaultDate: [dateRange.from, dateRange.to],
        locale: Korean,  // 한국어 설정
        minDate: new Date(new Date().setDate(new Date().getDate() - 7)),  // 오늘 기준으로 1주일 전까지 선택 가능
        maxDate: new Date(new Date().setMonth(new Date().getMonth() + 1)),  // 오늘 기준으로 1달 후까지 선택 가능
        onChange: (selectedDates) => {
            if (selectedDates.length === 2) {
                dateRange.from = selectedDates[0];
                dateRange.to = selectedDates[1];

                // 시작 날짜에 따라 종료 날짜의 maxDate를 제한
                flatpickrInstance.set('maxDate', new Date(new Date(selectedDates[0]).setMonth(selectedDates[0].getMonth() + 1)));
            }
        }
    };

    onMount(() => {
        flatpickrInstance = Flatpickr(flatpickrInput, options);
    });

    function openDatePicker() {
        if (flatpickrInstance) {
            flatpickrInstance.open();
        }
    }

    function selectChallengeType(type) {
        challengeType = type;

        // 선택한 도전 유형에 따라 첫 번째 카테고리를 기본으로 설정
        if (challengeType === 'positive') {
            challengeCategory = positiveCategories[0]; // 긍정 도전의 첫 번째 카테고리 기본값 설정
        } else if (challengeType === 'negative') {
            challengeCategory = negativeCategories[0]; // 부정 도전의 첫 번째 카테고리 기본값 설정
        }
    }

    $: challengeSummary = `나 ${nickname}는 ${formatDate(dateRange.from)}부터 ${formatDate(dateRange.to)}까지 `;

    // 실시간 요약 표시 여부 결정
    $: showChallengeSummary = dateRange.from && dateRange.to && challengeType && challengeCategory;
</script>

<div class="relative">
    <h3 class="text-lg font-bold mb-4">기간 설정</h3>

    <!-- 선택된 날짜 범위를 표시하는 버튼을 클릭하면 Flatpickr가 열림 -->
    <div class="flex justify-center">
        <button class="btn btn-outline w-full text-center" on:click={openDatePicker}>
            <span>{formatDate(dateRange.from)} ~ {formatDate(dateRange.to)}</span>
        </button>
    </div>

    <!-- Flatpickr 인스턴스가 적용될 input 요소 -->
    <input type="text" bind:this={flatpickrInput} class="hidden" />

    <!-- 도전 선택 버튼 -->
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

    <!-- 선택된 도전 유형에 따른 카테고리 선택 -->
    {#if challengeType === 'positive'}
        <div class="mt-4">
            <h5 class="text-md font-semibold">긍정 도전 선택</h5>
            <div class="grid grid-cols-2 gap-2 mt-2">
                {#each positiveCategories as category}
                    <button
                            class="btn w-full {challengeCategory === category ? 'btn-primary' : 'btn-outline'}"
                            on:click={() => challengeCategory = category}>
                        {category}
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
                            on:click={() => challengeCategory = category}>
                        {category}
                    </button>
                {/each}
            </div>
        </div>
    {/if}

    <!-- 실시간 요약과 도전 대상 입력 (모든 조건이 만족될 때만 표시) -->
    {#if showChallengeSummary}
        <div class="mt-4 p-4 bg-gray-100 rounded-md">
            <p>
                {challengeSummary}
                <input type="text" class="input input-bordered inline w-1/4" bind:value={challengeTarget} placeholder="[무엇을]"/>
                {challengeCategory ? `${challengeCategory}.` : '--------'}
            </p>
        </div>
    {/if}

</div>

<style>
    :global(.flatpickr-calendar) {
        position: fixed !important;
        left: 50% !important;
        top: 50% !important;
        transform: translate(-50%, -50%) !important;
        z-index: 9999 !important;
    }
</style>
