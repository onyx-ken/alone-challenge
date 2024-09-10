<script>
    import { onMount, onDestroy } from 'svelte';
    import ImageUpload from './ImageUpload.svelte';
    import ImageEdit from './ImageEdit.svelte';
    import ChallengeDetails from '$lib/components/main/ui/ChallengeDetails.svelte';
    import ChallengeCertificate from '$lib/components/main/ui/ChallengeCertificate.svelte';
    import BackgroundSelector from './BackgroundSelector.svelte';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();
    export let onClose = () => {};
    export let onPost = () => {};

    let step = 1;
    let uploadedImages = [];
    let selectedImageData = {};
    let selectedBackground = "/background1.png"; // 기본 배경 이미지
    let userText = ""; // 사용자 입력 글

    function handleKeydown(event) {
        if (event.key === 'Escape') {
            onClose();
        }
    }

    function handleImageUpload(event) {
        const files = event.detail;
        if (Array.isArray(files) && files.length > 0) {
            uploadedImages = files.map(file => ({
                file,
                preview: URL.createObjectURL(file)
            }));
            step = 2; // 이미지 업로드 후 자르기 단계로 이동
        } else {
            console.error("handleImageUpload: Expected an array, but got", typeof files);
        }
    }

    function handleCropComplete(croppedAreaPixels) {
        selectedImageData = croppedAreaPixels;
    }

    function handleImageEdit() {
        step = 3;
    }

    function handleNextClick() {
        if (step === 1 && uploadedImages.length === 0) {
            step = 3;
        } else {
            step++;
        }
    }

    function handlePreviousClick() {
        if (step === 2) {
            uploadedImages = [];
            step = 1;
        } else if (step === 3) {
            if (uploadedImages.length === 0) {
                step = 1;
            } else {
                step = 2; // 2번째 단계로 이동
            }
        }
    }

    function handleBackgroundSelect(event) {
        selectedBackground = event.detail; // 배경 선택 시 선택된 배경 이미지 변경
    }

    function handleTextChange(event) {
        userText = event.target.value; // 입력된 글 내용 저장
    }

    function selectBackground(background) {
        console.log(background);
        selectedBackground = background;
        dispatch('select', selectedBackground); // Svelte의 dispatch 함수를 사용해 이벤트 전달
    }

    onMount(() => {
        window.addEventListener('keydown', handleKeydown);
        return () => {
            window.removeEventListener('keydown', handleKeydown);
        };
    });

    onDestroy(() => {
        window.removeEventListener('keydown', handleKeydown);
    });

    $: title = step === 1 ? "새로운 도전하기" :
        step === 2 ? "이미지 자르기" :
            step === 3 ? "도전 내용 작성하기" :
                step === 4 ? "도전 공유하기" : "------";
</script>

<div class="modal modal-open">
    <div class="modal-box w-11/12 max-w-5xl p-8" role="dialog" aria-label={title}>
        <form method="dialog">
            <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" on:click={onClose}>✕</button>
        </form>
        <h3 class="font-bold text-lg mb-4">{title}</h3>

        <!-- 1단계: 이미지 업로드 -->
        {#if step === 1}
            <ImageUpload on:upload={handleImageUpload} />
        {:else if step === 2}
            <!-- 2단계: 이미지 자르기 -->
            <ImageEdit
                    images={uploadedImages}
                    onSave={handleImageEdit}
                    onCropComplete={handleCropComplete}
            />
        {:else if step === 3}
            <!-- 3단계: 도전 내용 작성 -->
            <ChallengeDetails />
        {/if}

        <!-- 4단계: 도전 공유 (도전장 미리보기, 배경 선택, 글 작성) -->
        {#if step === 4}
            <div class="flex space-x-8">
                <!-- 왼쪽: 도전장 미리보기 -->
                <div class="w-2/3">
                    <h3 class="font-bold text-lg mb-4"> 미리보기</h3>
                    <!-- 도전장 미리보기 화면을 모달 크기에 맞게 축소 -->
                    <div class="certificate-preview">
                        <ChallengeCertificate background={selectedBackground} />
                    </div>
                </div>

                <!-- 오른쪽: 배경 선택 및 글 작성 -->
                <div class="w-1/3">
                    <h3 class="font-bold text-lg mb-4">도전장 배경 선택</h3>
                    <BackgroundSelector on:select={handleBackgroundSelect} />

                    <h3 class="font-bold text-lg mt-8 mb-4">추가 글 작성</h3>
                    <textarea class="textarea textarea-bordered w-full h-40"
                              placeholder="도전에 대한 글을 작성하세요 (최대 1000자)"
                              maxlength="1000"
                              bind:value={userText}></textarea>
                </div>
            </div>
        {/if}

        <!-- 하단 네비게이션 -->
        <div class="modal-action flex justify-between items-center">
            {#if step === 1}
                <p class="text-sm text-gray-500 text-center mx-auto">이미지 없이도 도전을 할 수 있습니다.</p>
                <button class="btn btn-primary ml-auto" on:click={handleNextClick}>다음</button>
            {/if}

            {#if step === 2}
                <button class="btn btn-outline" on:click={handlePreviousClick}>이전</button>
                <button class="btn btn-primary ml-auto" on:click={handleNextClick}>다음</button>
            {/if}

            {#if step === 3}
                <button class="btn btn-outline" on:click={handlePreviousClick}>이전</button>
                <button class="btn btn-primary ml-auto" on:click={handleNextClick}>다음</button>
            {/if}

            {#if step === 4}
                <button class="btn btn-outline" on:click={handlePreviousClick}>이전</button>
                <button class="btn btn-primary ml-auto">공유하기</button>
            {/if}
        </div>
    </div>
</div>

<style>
    .modal-box {
        background-color: #f9fafb;
        border-radius: 0.5rem;
        box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
    }

    .textarea {
        resize: none;
    }

    .certificate-preview {
        width: 100%; /* 부모의 너비에 맞춰서 조정 */
        height: auto; /* 비율을 유지한 채 높이 자동 조정 */
        display: flex;
        justify-content: center;
        align-items: center;
    }

    :global(.certificate-container) {
        width: 100% !important;
        max-width: 210mm !important; /* A4 용지 너비 */
        height: auto !important;
        aspect-ratio: 210 / 297 !important; /* A4 비율 유지 */
    }

    :global(.certificate-container img, .certificate-container div) {
        width: 100% !important;
        height: auto !important;
    }
</style>
