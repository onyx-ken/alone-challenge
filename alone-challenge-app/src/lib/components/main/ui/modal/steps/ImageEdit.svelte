<script>
    import Cropper from 'svelte-easy-crop';
    import { onMount } from 'svelte';
    import { dndzone } from 'svelte-dnd-action';
    import { flip } from 'svelte/animate';
    import LeftArrowIcon from "$lib/components/icon/LeftArrowIcon.svelte";
    import RightArrowIcon from "$lib/components/icon/RightArrowIcon.svelte";
    import ZoomOutIcon from "$lib/components/icon/ZoomOutIcon.svelte";
    import ZoomInIcon from "$lib/components/icon/ZoomInIcon.svelte";
    import FoldersIcon from "$lib/components/icon/FoldersIcon.svelte";

    export let images = []; // 부모로부터 전달받은 이미지 배열
    export let onImagesUpdate = () => {}; // 부모에게 변경된 이미지를 전달하기 위한 콜백 함수
    export let onCropComplete = () => {};

    // 로컬 상태로 관리할 이미지 배열
    let localImages = [];

    let aspect = 1;
    let selectedImageIndex = 0; // 자를 이미지를 선택할 인덱스

    // 썸네일 모달 표시 여부
    let showThumbnailModal = false;

    onMount(() => {
        // 이미지 객체에 자르기 정보 추가
        localImages = images.map(image => ({
            ...image,
            crop: { x: 0, y: 0 },
            zoom: 1,
            croppedAreaPixels: null
        }));
    });

    // 반응형 문법을 사용하여 selectedImageIndex의 유효성 보장
    $: {
        if (selectedImageIndex < 0) {
            selectedImageIndex = 0;
        } else if (selectedImageIndex >= localImages.length) {
            selectedImageIndex = localImages.length - 1;
        }
    }

    $: if (images.length !== localImages.length) {
        localImages = images.map(image => ({
            ...image,
            crop: image.crop || { x: 0, y: 0 },
            zoom: image.zoom || 1,
            croppedAreaPixels: image.croppedAreaPixels || null
        }));
    }

    const handleCropComplete = (croppedAreaPixelsResult) => {
        if (localImages[selectedImageIndex]) {
            localImages[selectedImageIndex].croppedAreaPixels = croppedAreaPixelsResult;
            onCropComplete(localImages.map(img => img.croppedAreaPixels));
        }
    };

    function selectNextImage() {
        if (selectedImageIndex >= localImages.length - 1) {
            return;
        }
        selectedImageIndex++;
    }

    function selectPreviousImage() {
        if (selectedImageIndex <= 0) {
            return;
        }
        selectedImageIndex--;
    }

    const resetCropState = () => {
        if (localImages[selectedImageIndex]) {
            localImages[selectedImageIndex].crop = { x: 0, y: 0 };
            localImages = [...localImages]; // 반응성 유지
        }
    };

    const setAspect = (ratio) => {
        aspect = ratio || null;
        resetCropState();
    };

    const toggleThumbnailModal = () => {
        showThumbnailModal = !showThumbnailModal;
    };

    function handleDndConsider(e) {
        localImages = e.detail.items;
    }

    function handleDndFinalize(e) {
        localImages = e.detail.items;

        // 부모에게 변경된 이미지 배열을 전달합니다.
        onImagesUpdate(localImages);

        // selectedImageIndex 업데이트
        const oldSelectedImageId = localImages[selectedImageIndex]?.id;
        selectedImageIndex = localImages.findIndex(
            (img) => img.id === oldSelectedImageId
        );
    }

    const increaseZoom = () => {
        if (localImages[selectedImageIndex]) {
            localImages[selectedImageIndex].zoom = Math.min(localImages[selectedImageIndex].zoom + 0.1, 3);
            localImages = [...localImages]; // 반응성 유지
        }
    };

    const decreaseZoom = () => {
        if (localImages[selectedImageIndex]) {
            localImages[selectedImageIndex].zoom = Math.max(localImages[selectedImageIndex].zoom - 0.1, 1);
            localImages = [...localImages]; // 반응성 유지
            console.log('Zoom decreased for image:', localImages[selectedImageIndex]?.id, 'New zoom:', localImages[selectedImageIndex].zoom);
        }
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
        z-index: 1; /* 낮은 z-index 설정 */
    }

    .arrow-button {
        z-index: 20 !important; /* 높은 z-index 설정 */
    }
</style>

<div class="cropper-container">
    {#if localImages[selectedImageIndex]}
        <Cropper
                class="cropper"
                image={localImages[selectedImageIndex].preview}
                bind:crop={localImages[selectedImageIndex].crop}
                bind:zoom={localImages[selectedImageIndex].zoom}
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
    {/if}

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
            <ZoomOutIcon />
        </button>
        <button class="btn btn-circle btn-sm" on:click={increaseZoom}>
            <ZoomInIcon />
        </button>
    </div>

    <div class="thumbnail-button">
        <button class="btn btn-circle btn-sm" on:click={toggleThumbnailModal}>
            <FoldersIcon />
        </button>
    </div>

    <!-- 왼쪽 화살표 버튼 (첫 번째 이미지에서는 숨김) -->
    {#if selectedImageIndex > 0}
        <button
                class="absolute left-6 top-1/2 transform -translate-y-1/2 btn btn-circle btn-outline arrow-button"
                on:click={selectPreviousImage}
        >
            <LeftArrowIcon />
        </button>
    {/if}

    <!-- 오른쪽 화살표 버튼 (마지막 이미지에서는 숨김) -->
    {#if selectedImageIndex < localImages.length - 1}
        <button
                class="absolute right-6 top-1/2 transform -translate-y-1/2 btn btn-circle btn-outline arrow-button"
                on:click={selectNextImage}
        >
            <RightArrowIcon />
        </button>
    {/if}
</div>

<!-- 이미지 개수 표시 -->
<div class="flex justify-center mt-2 space-x-2">
    {#each localImages as _, index}
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
