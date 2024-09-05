<script>
    export let nickname = "홍길동";
    export let startDate = "2024-09-01";
    export let endDate = "2024-09-30";
    export let challengeDescription = "매일 아침 6시에 일어나기";
    export let isSuccess = null; // 도전 성공 여부
    export let backgroundImage = "/background.webp"; // 배경 이미지

    // 성공/실패 도장 이미지 URL (실제 이미지 경로로 대체)
    let successStamp = "/images/success-stamp.png";
    let failureStamp = "/images/failure-stamp.png";
</script>

<div class="p-8 bg-base-200 rounded-lg shadow-lg w-full max-w-md mx-auto text-center"
     style="background-image: url({backgroundImage}); background-size: cover; background-position: center;">
    <div class="bg-white p-6 rounded-lg shadow-lg border-4 border-gray-300 relative">
        <h1 class="text-4xl font-bold mb-6">도전장</h1>
        <div class="mb-4">
            <span class="text-6xl">⚔️</span>
        </div>
        <div class="mb-4">
            <h2 class="text-2xl font-semibold">닉네임: {nickname}</h2>
        </div>
        <div class="mb-4">
            <p class="text-lg font-medium">"{challengeDescription}"</p>
        </div>
        <div class="mb-4">
            <p class="text-lg font-medium">기간: {startDate} ~ {endDate}</p>
        </div>
        {#if isSuccess !== null}
            <div class="mt-8">
                {#if isSuccess}
                    <img src={successStamp} alt="성공 도장" class="w-32 h-32 mx-auto"/>
                {:else}
                    <img src={failureStamp} alt="실패 도장" class="w-32 h-32 mx-auto"/>
                {/if}
            </div>
        {/if}
    </div>
</div>

<style>
    :global(.background-certificate) {
        background-image: url('/background.webp');
        background-size: cover; /* 배경 이미지가 요소에 맞게 자동 조절 */
        background-position: center; /* 배경 이미지를 중앙에 위치 */
        background-repeat: no-repeat; /* 배경 이미지를 반복하지 않음 */
        border: 20px solid transparent; /* 기존 테두리 스타일을 유지하고 싶으면 */
    }
</style>
