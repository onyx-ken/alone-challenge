<script>
    import Cropper from 'svelte-easy-crop';
    import { createEventDispatcher, onMount } from 'svelte';
    import { dndzone } from 'svelte-dnd-action';
    import { flip } from 'svelte/animate';
    import LeftArrowIcon from "$lib/components/icon/LeftArrowIcon.svelte";
    import RightArrowIcon from "$lib/components/icon/RightArrowIcon.svelte";
    import ZoomOutIcon from "$lib/components/icon/ZoomOutIcon.svelte";
    import ZoomInIcon from "$lib/components/icon/ZoomInIcon.svelte";
    import FoldersIcon from "$lib/components/icon/FoldersIcon.svelte";
    import {getCroppedImg} from '$lib/util/getCroppedImg.js';

    // 부모로부터 전달받은 프롭스
    export let images = []; // 이미지 배열
    export let onImagesUpdate = () => {
    }; // 변경된 이미지를 부모에게 전달하는 콜백 함수

    // 내부 상태 관리 변수
    let localImages = []; // 로컬에서 관리할 이미지 배열
    let originalImages = []; // 원본 이미지를 저장할 배열
    let aspect = 1; // 크롭 비율
    let selectedImageIndex = 0; // 선택된 이미지의 인덱스
    let showThumbnailModal = false; // 썸네일 모달 표시 여부

    const dispatch = createEventDispatcher();

    // 컴포넌트 마운트 시 초기화
    onMount(() => {
        if (!images || images.length === 0) {
            console.error('No images provided to ImageEdit component.');
            return;
        }
        initializeImages();
        initializeOriginalImages();
        setInitialCroppedArea();
    });

    // 이미지 배열 초기화
    function initializeImages() {
        localImages = images.map(image => ({
            ...image,
            crop: {x: 0, y: 0},
            zoom: 1,
            rotation: 0,
            croppedAreaPixels: null,
        }));
    }

    // 원본 이미지 배열 저장
    function initializeOriginalImages() {
        originalImages = localImages.map(image => ({
            ...image,
            file: image.file,
            preview: image.preview,
        }));
    }

    // 선택된 이미지의 초기 크롭 영역 설정
    function setInitialCroppedArea() {
        const imageItem = localImages[selectedImageIndex];
        if (!imageItem) {
            console.warn('No imageItem found at selectedImageIndex:', selectedImageIndex);
            return;
        }

        const img = new Image();
        img.onload = () => {
            const {naturalWidth, naturalHeight} = img;
            imageItem.croppedAreaPixels = {
                x: 0,
                y: 0,
                width: naturalWidth,
                height: naturalHeight,
            };
            console.log('Initial croppedAreaPixels set:', imageItem.croppedAreaPixels);
        };
        img.src = imageItem.preview;
    }

    // 반응형으로 selectedImageIndex의 유효성 보장
    $: validateSelectedImageIndex();

    function validateSelectedImageIndex() {
        if (selectedImageIndex < 0) {
            selectedImageIndex = 0;
        } else if (selectedImageIndex >= localImages.length) {
            selectedImageIndex = localImages.length > 0 ? localImages.length - 1 : 0;
        }
    }

    // 이미지 배열 길이가 변경되었을 때 localImages 업데이트
    $: if (images.length !== localImages.length) {
        updateLocalImages();
    }

    function updateLocalImages() {
        localImages = images.map(image => ({
            ...image,
            crop: image.crop || {x: 0, y: 0},
            zoom: image.zoom || 1,
            croppedAreaPixels: image.croppedAreaPixels || null,
        }));
    }

    // 크롭 완료 이벤트 핸들러
    const handleCropComplete = (e) => {
        console.log('Event received:', e);
        console.log('e.detail:', e.detail);

        const croppedAreaPixels = e.detail.pixels;
        console.log('croppedAreaPixels:', croppedAreaPixels);

        if (localImages[selectedImageIndex]) {
            localImages[selectedImageIndex].croppedAreaPixels = croppedAreaPixels;
            console.log('croppedAreaPixels updated:', croppedAreaPixels);
        }
    };

    // 이미지 순서 변경 관련 함수
    function handleDndConsider(e) {
        localImages = e.detail.items;
    }

    function handleDndFinalize(e) {
        localImages = e.detail.items;
        onImagesUpdate(localImages);
        updateSelectedImageIndex();
    }

    function updateSelectedImageIndex() {
        const oldSelectedImageId = localImages[selectedImageIndex]?.id;
        selectedImageIndex = localImages.findIndex(
            (img) => img.id === oldSelectedImageId
        );
    }

    // 이미지 선택 관련 함수
    function selectNextImage() {
        if (selectedImageIndex < localImages.length - 1) {
            selectedImageIndex++;
        }
    }

    function selectPreviousImage() {
        if (selectedImageIndex > 0) {
            selectedImageIndex--;
        }
    }

    // 크롭 상태 초기화
    function resetCropState() {
        if (localImages[selectedImageIndex]) {
            localImages[selectedImageIndex].crop = {x: 0, y: 0};
            localImages[selectedImageIndex].zoom = 1;
            localImages = [...localImages]; // 반응성 유지
        }
    }

    // 현재 이미지의 크롭 정보 초기화
    function resetCurrentCrop() {
        const image = localImages[selectedImageIndex];
        if (!image) {
            console.warn('No image found at selectedImageIndex:', selectedImageIndex);
            return;
        }

        const originalImage = originalImages.find(orig => orig.id === image.id);
        if (!originalImage) {
            console.warn('No original image found for image with id:', image.id);
            return;
        }

        // 원본 파일 및 미리보기로 복원
        image.file = originalImage.file;
        image.preview = originalImage.preview;

        // 크롭 관련 속성 초기화
        image.crop = {x: 0, y: 0};
        image.zoom = 1;
        image.croppedAreaPixels = null;

        // 반응성 유지
        localImages = [...localImages];
    }

    // 크롭 비율 설정
    function setAspect(ratio) {
        aspect = ratio || null;
        resetCropState();
    }

    // 썸네일 모달 토글
    function toggleThumbnailModal() {
        showThumbnailModal = !showThumbnailModal;
    }

    // 이미지 확대/축소
    function increaseZoom() {
        adjustZoom(0.1);
    }

    function decreaseZoom() {
        adjustZoom(-0.1);
    }

    function adjustZoom(delta) {
        if (localImages[selectedImageIndex]) {
            const newZoom = localImages[selectedImageIndex].zoom + delta;
            localImages[selectedImageIndex].zoom = Math.min(Math.max(newZoom, 1), 3);
            localImages = [...localImages]; // 반응성 유지
            console.log('Zoom adjusted for image:', localImages[selectedImageIndex]?.id, 'New zoom:', localImages[selectedImageIndex].zoom);
        }
    }

    // 현재 이미지 크롭 후 저장
    async function processCroppedImages() {
        console.log('Processing current image!');
        try {
            const imageItem = localImages[selectedImageIndex];
            const {croppedAreaPixels, zoom} = imageItem;

            if (!croppedAreaPixels) {
                console.error('croppedAreaPixels is not available for image:', imageItem.id);
                alert('저장하기 전에 크롭 영역을 조정해주세요.');
                return;
            }

            console.log('CroppedAreaPixels:', croppedAreaPixels, 'Zoom:', zoom);

            // 크롭된 이미지 가져오기
            const croppedBlob = await getCroppedImg(
                imageItem.preview,
                croppedAreaPixels,
                zoom
            );

            if (!croppedBlob) {
                console.error('croppedBlob is invalid for image:', imageItem.id);
                return;
            }

            // 이미지 업데이트
            updateImageItem(imageItem, croppedBlob);

            // 부모 컴포넌트에 업데이트 알림
            onImagesUpdate(localImages);
        } catch (error) {
            console.error('Error processing cropped image:', error);
            alert('이미지 처리 중 오류가 발생했습니다.');
        }
    }

    // 이미지 아이템 업데이트
    function updateImageItem(imageItem, croppedBlob) {
        imageItem.file = new File([croppedBlob], imageItem.file.name, {
            type: imageItem.file.type,
            lastModified: new Date().getTime(),
        });
        imageItem.preview = URL.createObjectURL(croppedBlob);

        // 크롭 관련 속성 초기화
        imageItem.crop = {x: 0, y: 0};
        imageItem.zoom = 1;
        imageItem.croppedAreaPixels = null;

        console.log('Updated image item:', imageItem);

        // 반응성 유지
        localImages = [...localImages];
    }
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
        background: rgba(0, 0, 0, 0.5);
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

    .action-buttons {
        display: flex;
        gap: 10px;
        margin-top: 10px;
    }
</style>

<div class="cropper-container">
    {#if localImages[selectedImageIndex]}
        <Cropper
                class="cropper"
                image={localImages[selectedImageIndex].preview}
                bind:crop={localImages[selectedImageIndex].crop}
                bind:zoom={localImages[selectedImageIndex].zoom}
                aspect={aspect}
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

    <!-- 컨트롤 패널 -->
    <div class="controls">
        <!-- 비율 선택 버튼 -->
        <button class="btn btn-circle btn-sm" on:click={() => setAspect(1)}>
            <span class="text-xs">1:1</span>
        </button>
        <button class="btn btn-circle btn-sm" on:click={() => setAspect(4 / 5)}>
            <span class="text-xs">4:5</span>
        </button>
        <button class="btn btn-circle btn-sm" on:click={() => setAspect(16 / 9)}>
            <span class="text-xs">16:9</span>
        </button>

        <!-- 줌 컨트롤 버튼 -->
        <button class="btn btn-circle btn-sm" on:click={decreaseZoom}>
            <ZoomOutIcon/>
        </button>
        <button class="btn btn-circle btn-sm" on:click={increaseZoom}>
            <ZoomInIcon/>
        </button>
    </div>

    <!-- 썸네일 모달 토글 버튼 -->
    <div class="thumbnail-button">
        <button class="btn btn-circle btn-sm" on:click={toggleThumbnailModal}>
            <FoldersIcon/>
        </button>
    </div>

    <!-- 이미지 전환 버튼 -->
    {#if selectedImageIndex > 0}
        <button
                class="absolute left-6 top-1/2 transform -translate-y-1/2 btn btn-circle btn-outline arrow-button"
                on:click={selectPreviousImage}
        >
            <LeftArrowIcon/>
        </button>
    {/if}

    {#if selectedImageIndex < localImages.length - 1}
        <button
                class="absolute right-6 top-1/2 transform -translate-y-1/2 btn btn-circle btn-outline arrow-button"
                on:click={selectNextImage}
        >
            <RightArrowIcon/>
        </button>
    {/if}
</div>

<!-- 이미지 인디케이터 -->
<div class="flex justify-center mt-2 space-x-2">
    {#each localImages as _, index}
        <div class="w-3 h-3 rounded-full {index === selectedImageIndex ? 'bg-blue-500' : 'bg-gray-400'}"></div>
    {/each}
</div>

<!-- 액션 버튼 -->
<div class="action-buttons">
    <button class="btn btn-primary" on:click={processCroppedImages}>
        저장
    </button>
    <button class="btn btn-outline" on:click={resetCurrentCrop}>
        초기화
    </button>
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
                    <img src={image.preview} alt="Thumbnail"/>
                </div>
            {/each}
        </div>
    </div>
{/if}
