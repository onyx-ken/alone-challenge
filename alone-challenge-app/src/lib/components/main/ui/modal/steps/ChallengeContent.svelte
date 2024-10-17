<script>
    export let onPost = () => {}; // 도전 포스팅 콜백
    export let challengeSummary = ''; // 요약된 도전 내용

    let additionalMessage = '';

    const handlePost = () => {
        onPost(additionalMessage);
    }
</script>

<div>
    <h3 class="text-lg font-bold">도전 내용 작성</h3>
    <p>{challengeSummary}</p>
    <textarea class="textarea textarea-bordered w-full mt-4" bind:value={additionalMessage} placeholder="추가 메시지를 입력하세요 (최대 1500자)" maxlength="1500"></textarea>
    <div class="modal-action">
        <button class="btn btn-primary" on:click={handlePost}>도전 공유하기</button>
    </div>
</div>
