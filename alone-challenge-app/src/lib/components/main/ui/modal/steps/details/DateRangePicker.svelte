<script>
    import { onMount } from 'svelte';
    import Flatpickr from "flatpickr";
    import "flatpickr/dist/themes/airbnb.css";
    import { Korean } from "flatpickr/dist/l10n/ko.js";
    import { format } from 'date-fns';

    export let dateRange = { from: new Date(), to: new Date(new Date().setDate(new Date().getDate() + 7)) };
    export let onChange = () => {};
    export const formatDate = (date) => format(date, 'yyyy.MM.dd');

    let flatpickrInput;
    let flatpickrInstance;

    const options = {
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
                onChange(dateRange);
            }
        }
    };

    onMount(() => {
        flatpickrInstance = Flatpickr(flatpickrInput, options);
    });

    const openDatePicker = () => {
        if (flatpickrInstance) {
            flatpickrInstance.open();
        }
    }
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
