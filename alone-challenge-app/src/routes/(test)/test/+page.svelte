<script>
    import FistIcon from "$lib/components/icon/FistIcon.svelte";

    export let nickName = "Daniel";
    export let startDate = "2024-09-01";
    export let endDate = "2024-09-30";
    export let challengeDescription = "매일 아침 6시에 기상 하겠습니다";
    export let isSuccess = null;
    export let background = "http://localhost:5173/background1.png"; // 배경 이미지 절대 URL

    let successStamp = "http://localhost:5173/images/success-stamp.png";
    let failureStamp = "http://localhost:5173/images/failure-stamp.png";
    const testImageGenerationAndDisplay = async () => {
        try {
            // 서버로 이미지 생성 요청
            const imageResponse = await fetch('/api/generate-certificate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    challengeData: {
                        nickName: "Daniel",
                        startDate: "2024-10-15",
                        endDate: "2024-10-23",
                        challengeDescription: "매일 아침 6시에 기상하겠습니다"
                    },
                    selectedBackground: "/background1.png"
                }),
            });

            if (!imageResponse.ok) {
                throw new Error('Failed to generate certificate image');
            }

            // 응답에서 Base64 이미지 가져오기
            const { imageBase64 } = await imageResponse.json();

            // Base64 문자열을 브라우저에서 이미지로 표시
            const imgElement = document.createElement('img');
            imgElement.src = `data:image/png;base64,${imageBase64}`;
            imgElement.alt = 'Generated Certificate';
            document.body.appendChild(imgElement);

            console.log('이미지가 성공적으로 생성되고 표시되었습니다.');

        } catch (error) {
            console.error('Error generating or displaying image:', error);
            alert('이미지 생성 중 오류가 발생했습니다.');
        }
    };
</script>

<div class="certificate-container-wrapper">
    <div class="certificate-container" style="background-image: url({background});">
        <h1 class="certificate-title">도전장</h1>
        <div class="certificate-sub">
            <FistIcon width="72" height="72" />
        </div>
        <div class="certificate-sub">
            <h2 class="certificate-nickName">{nickName}</h2>
        </div>
        <div class="certificate-sub">
            <p class="certificate-dec">"{challengeDescription}"</p>
        </div>
        <div class="certificate-sub">
            <p class="certificate-date">{startDate} ~ {endDate}</p>
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
    .certificate-container-wrapper {
        font-family: "dongle-regular", sans-serif;
        font-weight: 400;
        font-style: normal;
        text-align: center;
        border-radius: 2px;
        width: 100%;
        margin-left: auto;
        margin-right: auto;
        box-sizing: border-box;
    }

    .certificate-container {
        background-size: cover;
        background-position: center center;
        width: 210mm; /* A4 용지 너비 */
        height: 297mm; /* A4 용지 높이 */
        aspect-ratio: 210 / 297; /* A4 비율 */
        display: flex;
        flex-direction: column;
        justify-content: center; /* 수직 가운데 정렬 */
        align-items: center; /* 수평 가운데 정렬 */
        text-align: center; /* 텍스트 가운데 정렬 */
        padding: 1.5rem;
        border-width: 4px;
        border-radius: 0.5rem;
        position: relative;
    }

    .certificate-title {
        font-weight: 700;
        font-size: 120px;
        line-height: 180px;
        margin-bottom: 32px;
        display: block;
        margin-block-start: 0;
        margin-block-end: 120px;
        margin-inline-start: 0;
        margin-inline-end: 0;
        unicode-bidi: isolate;
        tab-size: 4;
    }

    .certificate-sub {
        line-height: 48px;
        margin-bottom: 32px;
        tab-size: 4;
    }

    .certificate-nickName {
        font-weight: 400;
        font-size: 40px;
        line-height: 32px;
        margin: 0;
    }

    .certificate-dec {
        font-weight: 400;
        font-size: 32px;
        line-height: 28px;
        margin: 0;
    }

    .certificate-date {
        font-weight: 300;
        font-size: 32px;
        line-height: 28px;
        margin: 0;
    }

</style>

<h1>도전장 이미지 생성 테스트용!</h1>
<button on:click={testImageGenerationAndDisplay()}>도전장 이미지 생성 하기</button>
