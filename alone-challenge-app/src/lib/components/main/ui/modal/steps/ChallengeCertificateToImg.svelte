<script>
    import FistIcon from "$lib/components/icon/FistIcon.svelte";

    export let nickName = "홍길동";
    export let startDate = "2024-09-01";
    export let endDate = "2024-09-30";
    export let challengeDescription = "매일 아침 6시에 기상 하겠습니다";
    export let isSuccess = null;
    export let background = "http://localhost:5173/background1.png"; // 배경 이미지 절대 URL

    let successStamp = "http://localhost:5173/images/success-stamp.png";
    let failureStamp = "http://localhost:5173/images/failure-stamp.png";
</script>

<div class="certificate-container" style="background-image: url({background});">
    <h1>도전장</h1>
    <div class="icon" style="display: flex;">
        <FistIcon/>
    </div>
    <h2>{nickName}</h2>
    <p class="description">"{challengeDescription}"</p>
    <p class="date">{startDate} ~ {endDate}</p>
    {#if isSuccess !== null}
        <div class="stamp" style="display: flex;">
            {#if isSuccess}
                <img src="{successStamp}" alt="성공 도장"/>
            {:else}
                <img src="{failureStamp}" alt="실패 도장"/>
            {/if}
        </div>
    {/if}
</div>

<style>
    .certificate-container {
        width: 794px; /* 이미지 생성 옵션의 width와 동일하게 설정 */
        height: 1123px; /* 이미지 생성 옵션의 height와 동일하게 설정 */
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        background-size: cover;
        background-position: center;
        font-family: "Nanum Brush Script", cursive;
        text-align: center;
    }

    h1 {
        font-size: 64px;
        font-weight: bold;
        margin-bottom: 24px;
    }

    .icon {
        margin-bottom: 16px;
    }

    h2 {
        font-size: 48px;
        font-weight: 600;
        margin-bottom: 16px;
    }

    .description {
        font-size: 24px;
        font-weight: 600;
        margin-bottom: 16px;
    }

    .date {
        font-size: 24px;
        font-weight: 500;
        margin-bottom: 16px;
    }

    .stamp img {
        width: 128px;
        height: 128px;
        margin: 0 auto;
    }
</style>
