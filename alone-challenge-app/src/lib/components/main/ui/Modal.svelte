<script>
    import { onMount, onDestroy } from 'svelte';
    import ImageUpload from './ImageUpload.svelte';
    import ImageEdit from './ImageEdit.svelte';
    import ChallengeDetails from '$lib/components/main/ui/ChallengeDetails.svelte';
    import ChallengeCertificate from '$lib/components/main/ui/ChallengeCertificate.svelte'

    export let onClose = () => {};
    export let onPost = () => {};

    let step = 1;
    let uploadedImages = [];
    let selectedImageData = {};

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

        {#if step === 1}
            <ImageUpload on:upload={handleImageUpload}/>
        {:else if step === 2}
            <ImageEdit
                    images={uploadedImages}
                    onSave={handleImageEdit}
                    onCropComplete={handleCropComplete}
            />
        {:else if step === 3}
            <ChallengeDetails />
        {/if}

        {#if step === 4}
            <ChallengeCertificate />
        {/if}

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
