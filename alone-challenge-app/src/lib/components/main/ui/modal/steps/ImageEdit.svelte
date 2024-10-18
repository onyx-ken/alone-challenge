<script>
    import Cropper from 'svelte-easy-crop';
    import { onMount } from 'svelte';
    import { dndzone } from 'svelte-dnd-action';
    import { flip } from 'svelte/animate';

    export let images = []; // 부모로부터 전달받은 이미지 배열
    export let onImagesUpdate = () => {}; // 부모에게 변경된 이미지를 전달하기 위한 콜백 함수
    export let onCropComplete = () => {};
    export let onSave = () => {}; // 저장 버튼 클릭 시 호출될 함수


    let localImages = [...images]; // 로컬 상태로 관리할 이미지 배열

    let crop = { x: 0, y: 0 };
    let aspect = 1;
    let zoomLevels = [];
    let croppedAreaPixels = [];
    let selectedImageIndex = 0; // 자를 이미지를 선택할 인덱스
    let isTransitioning = false;

    // 썸네일 모달 표시 여부
    let showThumbnailModal = false;

    onMount(() => {
        // 이미지가 변경될 때마다 zoomLevels를 초기화합니다.
        zoomLevels = localImages.map(() => 1);
    });

    const handleCropComplete = (croppedAreaPixelsResult) => {
        croppedAreaPixels[selectedImageIndex] = croppedAreaPixelsResult;
        onCropComplete(croppedAreaPixels);
    };

    function selectNextImage() {
        if (selectedImageIndex >= images.length - 1) return;

        selectedImageIndex++;
        resetCropState();
    }

    function selectPreviousImage() {
        if (selectedImageIndex <= 0) return;

        selectedImageIndex--;
        resetCropState();
    }

    const resetCropState = () => {
        crop = { x: 0, y: 0 }; // crop.set({...}) 대신 재할당
    };

    const setAspect = (ratio) => {
        aspect = ratio || null; // aspect.set(...) 대신 재할당
        crop = { x: 0, y: 0 }; // crop.set({...}) 대신 재할당
    };

    const toggleThumbnailModal = () => {
        showThumbnailModal = !showThumbnailModal;
    };

    function handleDndConsider(e) {
        localImages = e.detail.items;
    }

    function handleDndFinalize(e) {
        localImages = e.detail.items;

        // 추가적인 상태 업데이트는 여기서 수행합니다.
        // 부모에게 변경된 이미지 배열을 전달합니다.
        onImagesUpdate(localImages);

        // selectedImageIndex 업데이트
        selectedImageIndex = localImages.findIndex(
            (img) => img.id === localImages[selectedImageIndex]?.id
        );

        // zoomLevels와 croppedAreaPixels의 순서도 함께 변경합니다.
        zoomLevels = localImages.map((_, idx) => zoomLevels[idx] || 1);
        croppedAreaPixels = localImages.map((_, idx) => croppedAreaPixels[idx] || null);
    }

    const increaseZoom = () => {
        zoomLevels[selectedImageIndex] = Math.min(zoomLevels[selectedImageIndex] + 0.1, 3);
        zoomLevels = [...zoomLevels]; // 배열을 재할당하여 반응성 유지
    };

    const decreaseZoom = () => {
        zoomLevels[selectedImageIndex] = Math.max(zoomLevels[selectedImageIndex] - 0.1, 1);
        zoomLevels = [...zoomLevels]; // 배열을 재할당하여 반응성 유지
    };

</script>

<style>
    .controls {
        position: absolute;
        bottom: 10px;
        left: 10px;
        display: flex;
        gap: 10px;
        z-index: 10;
    }

    .thumbnail-button {
        position: absolute;
        bottom: 10px;
        right: 10px;
        z-index: 10;
    }

    .thumbnail-modal {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background: white;
        padding: 20px;
        border-radius: 8px;
        z-index: 20;
        max-width: 90%;
        max-height: 80%;
        overflow: auto;
    }

    .thumbnail-overlay {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0,0,0,0.5);
        z-index: 15;
    }

    .thumbnail-list {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
    }

    .thumbnail-item {
        width: 100px;
        height: 100px;
        overflow: hidden;
        border: 2px solid transparent;
    }

    .thumbnail-item.selected {
        border-color: blue;
    }

    .thumbnail-item img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    :global(.dragging) {
        opacity: 0.5;
    }

    .cropper-container {
        position: relative;
        width: 100%;
        height: 400px;
    }

    :global(.cropper) {
        width: 100%;
        height: 100%;
    }
</style>

<div class="cropper-container">
    <Cropper
            class="cropper"
            image={localImages[selectedImageIndex]?.preview}
            bind:crop={crop}
            bind:zoom={zoomLevels[selectedImageIndex]}
            bind:aspect={aspect}
            on:cropcomplete={handleCropComplete}
            background={false}
            style={{
                    cropAreaStyle: {
                        backgroundColor: 'transparent',
                    },
                    mediaStyle: {
                        objectFit: 'cover',
                        width: '100%',
                        height: '100%',
                    },
            }}
    />

    <!-- 왼쪽 하단 컨트롤 -->
    <div class="controls">
        <!-- 비율 선택 아이콘들 -->
        <button class="btn btn-circle btn-sm" on:click={() => setAspect(1)}>
            <span class="text-xs">1:1</span>
        </button>
        <button class="btn btn-circle btn-sm" on:click={() => setAspect(4 / 5)}>
            <span class="text-xs">4:5</span>
        </button>
        <button class="btn btn-circle btn-sm" on:click={() => setAspect(16 / 9)}>
            <span class="text-xs">16:9</span>
        </button>

        <!-- 줌 컨트롤 아이콘들 -->
        <button class="btn btn-circle btn-sm" on:click={decreaseZoom}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607ZM13.5 10.5h-6" />
            </svg>
        </button>
        <button class="btn btn-circle btn-sm" on:click={increaseZoom}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607ZM10.5 7.5v6m3-3h-6" />
            </svg>
        </button>
    </div>

    <!-- 오른쪽 하단 썸네일 버튼 -->
    <div class="thumbnail-button">
        <button class="btn btn-circle btn-sm" on:click={toggleThumbnailModal}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 6.878V6a2.25 2.25 0 0 1 2.25-2.25h7.5A2.25 2.25 0 0 1 18 6v.878m-12 0c.235-.083.487-.128.75-.128h10.5c.263 0 .515.045.75.128m-12 0A2.25 2.25 0 0 0 4.5 9v.878m13.5-3A2.25 2.25 0 0 1 19.5 9v.878m0 0a2.246 2.246 0 0 0-.75-.128H5.25c-.263 0-.515.045-.75.128m15 0A2.25 2.25 0 0 1 21 12v6a2.25 2.25 0 0 1-2.25 2.25H5.25A2.25 2.25 0 0 1 3 18v-6c0-.98.626-1.813 1.5-2.122" />
            </svg>
        </button>
    </div>
    <!-- 왼쪽 화살표 버튼 (첫 번째 이미지에서는 숨김) -->
    {#if selectedImageIndex > 0}
        <button class="absolute left-6 top-1/2 transform -translate-y-1/2 btn btn-circle btn-outline" on:click={selectPreviousImage}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
            </svg>
        </button>
    {/if}

    <!-- 오른쪽 화살표 버튼 (마지막 이미지에서는 숨김) -->
    {#if selectedImageIndex < images.length - 1}
        <button class="absolute right-6 top-1/2 transform -translate-y-1/2 btn btn-circle btn-outline" on:click={selectNextImage}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
        </button>
    {/if}
</div>

<!-- 이미지 개수 표시 -->
<div class="flex justify-center mt-2 space-x-2">
    {#each images as _, index}
        <div class="w-3 h-3 rounded-full {index === selectedImageIndex ? 'bg-blue-500' : 'bg-gray-400'}"></div>
    {/each}
</div>

<!-- 썸네일 모달 -->
{#if showThumbnailModal}
    <div class="thumbnail-overlay" on:click={toggleThumbnailModal}></div>
    <div class="thumbnail-modal">
        <h3 class="font-bold text-lg mb-4">이미지 순서 변경</h3>
        <div
                class="thumbnail-list"
                use:dndzone={{ items: localImages, flipDurationMs: 300 }}
                on:consider={handleDndConsider}
                on:finalize={handleDndFinalize}
        >
            {#each localImages as image (image.id)}
                <div
                        class="thumbnail-item {image.id === localImages[selectedImageIndex].id ? 'selected' : ''}"
                        animate:flip={{ duration: 300 }}
                        on:click={() => {
                                        selectedImageIndex = localImages.findIndex((img) => img.id === image.id);
                                        showThumbnailModal = false;
                }}
                >
                    <img src={image.preview} alt="Thumbnail" />
                </div>
            {/each}
        </div>
    </div>
{/if}
