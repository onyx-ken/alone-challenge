<script>
    import FistIcon from "$lib/components/icon/FistIcon.svelte";

    export let nickName = "홍길동";
    export let startDate = "2024-09-01";
    export let endDate = "2024-09-30";
    export let challengeDescription = "매일 아침 6시에 기상 하겠습니다";
    export let isSuccess = null;
    export let background = "/background1.png"; // 배경 이미지

    let successStamp = "/images/success-stamp.png";
    let failureStamp = "/images/failure-stamp.png";
</script>

<svelte:head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nanum+Brush+Script&display=swap" rel="stylesheet">
</svelte:head>

<!-- A4 비율을 맞춘 도전장 미리보기 -->
<div class="text-black p-8 bg-base-200 rounded-lg shadow-lg w-full max-w-md mx-auto text-center nanum-brush-script-regular">
    <div class="bg-white p-6 rounded-lg shadow-lg border-4 border-gray-300 relative certificate-container"
         style="background-image: url({background}); background-size: cover; background-position: center;">
        <h1 class="text-6xl font-bold mb-6">도전장</h1>
        <div class="mb-4">
            <FistIcon />
        </div>
        <div class="mb-4">
            <h2 class="text-4xl font-semibold">{nickName}</h2>
        </div>
        <div class="mb-4">
            <p class="text-lg font-semibold">"{challengeDescription}"</p>
        </div>
        <div class="mb-4">
            <p class="text-lg font-medium">{startDate} ~ {endDate}</p>
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
    .certificate-container {
        width: 210mm; /* A4 용지 너비 */
        height: 297mm; /* A4 용지 높이 */
        aspect-ratio: 210 / 297; /* A4 비율 */
        display: flex;
        flex-direction: column;
        justify-content: center; /* 수직 가운데 정렬 */
        align-items: center; /* 수평 가운데 정렬 */
        text-align: center; /* 텍스트 가운데 정렬 */
    }

    .nanum-brush-script-regular {
        font-family: "Nanum Brush Script", cursive;
        font-weight: 400;
        font-style: normal;
    }
</style>
