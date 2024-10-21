<script>
    import {format} from "date-fns";
    export let nickName = '';
    export let dateRange = { from: new Date(), to: new Date() };
    export let challengeType = '';
    export let challengeCategory = null;
    export let challengeTarget = '';
    export let onTargetChange = () => {};
    export const formatDate = (date) => format(date, 'yyyy.MM.dd');

    $: challengeSummary = `나 ${nickName}은(는) ${formatDate(dateRange.from)}부터 ${formatDate(dateRange.to)}까지`;

    $: showChallengeSummary = dateRange.from && dateRange.to && challengeType && challengeCategory;
</script>

{#if showChallengeSummary}
    <div class="mt-4 p-4 bg-gray-100 rounded-md">
        <p>
            {challengeSummary}
            <input type="text" class="input input-bordered inline w-1/4" bind:value={challengeTarget} placeholder="[무엇을]" on:input={e => onTargetChange(e.target.value)}/>
            {challengeCategory ? `${challengeCategory.ending}.` : '--------'}
        </p>
    </div>
{/if}
