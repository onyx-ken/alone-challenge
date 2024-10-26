<script>
    import { createEventDispatcher, onMount, onDestroy } from 'svelte';
    import { dndzone } from 'svelte-dnd-action';
    import { flip } from 'svelte/animate';
    import LeftArrowIcon from "$lib/components/icon/LeftArrowIcon.svelte";
    import RightArrowIcon from "$lib/components/icon/RightArrowIcon.svelte";
    import ZoomOutIcon from "$lib/components/icon/ZoomOutIcon.svelte";
    import ZoomInIcon from "$lib/components/icon/ZoomInIcon.svelte";
    import FoldersIcon from "$lib/components/icon/FoldersIcon.svelte";
    import Cropper from 'cropperjs';
    import 'cropperjs/dist/cropper.css';

    // 부모로부터 전달받은 프롭스
    export let images = []; // 이미지 배열
    export let onImagesUpdate = () => { }; // 변경된 이미지를 부모에게 전달하는 콜백 함수

    // 내부 상태 관리 변수
    let localImages = []; // 로컬에서 관리할 이미지 배열
    let originalImages = []; // 원본 이미지를 저장할 배열
    let aspect = 1; // 크롭 비율
    let selectedImageIndex = 0; // 선택된 이미지의 인덱스
    let showThumbnailModal = false; // 썸네일 모달 표시 여부

    const dispatch = createEventDispatcher();

    // Cropper.js 관련 변수
    let cropperInstance; // Cropper 인스턴스
    let imageElement; // 이미지 요소 참조

    // 컴포넌트 마운트 시 초기화
    onMount(() => {
        if (!images || images.length === 0) {
            console.error('No images provided to ImageEdit component.');
            return;
        }
        initializeImages();
        initializeOriginalImages();
        setInitialImage();
    });

    // 컴포넌트 언마운트 시 Cropper 인스턴스 파기
    onDestroy(() => {
        if (cropperInstance) {
            cropperInstance.destroy();
        }
    });

    // 이미지 배열 초기화
    function initializeImages() {
        localImages = images.map(image => ({
            ...image,
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

    // 선택된 이미지 설정 및 Cropper 인스턴스 초기화
    function setInitialImage() {
        if (!localImages[selectedImageIndex]) {
            console.warn('No image found at selectedImageIndex:', selectedImageIndex);
            return;
        }

        // 이미지 요소가 존재하지 않으면 종료
        if (!imageElement) {
            console.warn('Image element is not available.');
            return;
        }

        // 이미지 요소의 소스 업데이트
        imageElement.src = localImages[selectedImageIndex].preview;

        // 이미지 로드 이벤트 핸들러 설정
        imageElement.onload = () => {
            // 기존 Cropper 인스턴스 파기
            if (cropperInstance) {
                cropperInstance.destroy();
                cropperInstance = null;
            }

            // 새로운 Cropper 인스턴스 생성
            cropperInstance = new Cropper(imageElement, {
                aspectRatio: aspect,
                viewMode: 1,
                dragMode: 'move',
                autoCropArea: 1,
                responsive: true,
                restore: false,
                background: false,
                rotatable: true,
                zoomable: true,
                scalable: true,
                checkOrientation: false,
            });
        };
    }


    // 반응형으로 selectedImageIndex의 유효성 보장
    $: validateSelectedImageIndex();

    function validateSelectedImageIndex() {
        if (selectedImageIndex < 0) {
            selectedImageIndex = 0;
        } else if (selectedImageIndex >= localImages.length) {
            selectedImageIndex = localImages.length > 0 ? localImages.length - 1 : 0;
        }
        // 이미지 변경 시 Cropper 인스턴스 업데이트
        setInitialImage();
    }

    // 이미지 배열 길이가 변경되었을 때 localImages 업데이트
    $: if (images.length !== localImages.length) {
        updateLocalImages();
    }

    function updateLocalImages() {
        localImages = images.map(image => ({
            ...image,
        }));
    }

    // 이미지 순서 변경 관련 함수
    function handleDndConsider(e) {
        localImages = e.detail.items;
    }

    function handleDndFinalize(e) {
        localImages = e.detail.items;
        onImagesUpdate(localImages);
        updateSelectedImageIndex();
        setInitialImage();
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
        if (cropperInstance) {
            cropperInstance.reset();
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

        // Cropper 인스턴스 재설정
        setInitialImage();
    }

    // 크롭 비율 설정
    function setAspect(ratio) {
        aspect = ratio || NaN;
        if (cropperInstance) {
            cropperInstance.setAspectRatio(aspect);
        }
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
        if (cropperInstance) {
            cropperInstance.zoom(delta);
        }
    }

    // 이미지 회전
    function rotate(direction) {
        if (cropperInstance) {
            if (direction === 'left') {
                cropperInstance.rotate(-90);
            } else if (direction === 'right') {
                cropperInstance.rotate(90);
            }
        }
        // 회전 후 크롭 박스 재설정
        cropperInstance.clear();
        cropperInstance.crop();

        // 이미지 위치 가운데로 조정
        centerImage();
    }

    function centerImage() {
        // 컨테이너 크기 가져오기
        const containerData = cropperInstance.getContainerData();
        // 이미지 크기 가져오기
        const imageData = cropperInstance.getCanvasData();

        // 이미지의 중심 좌표 계산
        const imageCenterX = imageData.left + imageData.width / 2;
        const imageCenterY = imageData.top + imageData.height / 2;

        // 컨테이너의 중심 좌표 계산
        const containerCenterX = containerData.width / 2;
        const containerCenterY = containerData.height / 2;

        // 이동할 거리 계산
        const moveX = containerCenterX - imageCenterX;
        const moveY = containerCenterY - imageCenterY;

        // 이미지 이동
        cropperInstance.move(moveX, moveY);
    }

    // 현재 이미지 크롭 후 저장
    async function processCroppedImages() {
        if (!cropperInstance) {
            console.error('Cropper instance is not available.');
            return;
        }

        try {
            const canvas = cropperInstance.getCroppedCanvas();
            if (!canvas) {
                console.error('Failed to get cropped canvas.');
                return;
            }

            // Canvas를 Blob으로 변환
            canvas.toBlob((blob) => {
                if (!blob) {
                    console.error('Failed to convert canvas to blob.');
                    return;
                }

                const imageItem = localImages[selectedImageIndex];

                // Blob을 파일로 변환하여 업데이트
                imageItem.file = new File([blob], imageItem.file.name, {
                    type: imageItem.file.type,
                    lastModified: new Date().getTime(),
                });

                imageItem.preview = URL.createObjectURL(blob);

                // 로컬 이미지 배열 업데이트
                localImages = [...localImages];

                // 부모 컴포넌트에 업데이트 알림
                onImagesUpdate(localImages);

                // Cropper 인스턴스 재초기화
                setInitialImage();
            }, 'image/jpeg');
        } catch (error) {
            console.error('Error processing cropped image:', error);
            alert('이미지 처리 중 오류가 발생했습니다.');
        }
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
        display: flex;
        gap: 10px;
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
        max-height: 80vh; /* 최대 높이 설정 */
    }

    img {
        max-width: 100%;
        height: auto;
        display: block; /* 이미지가 중앙에 정렬되도록 */
    }

    :global(.cropper-container .cropper-drag-box) {
        cursor: default;
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
        <!-- 이미지 요소 -->
        <img bind:this={imageElement} src={localImages[selectedImageIndex].preview} alt="이미지 자르기" />

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
                <ZoomOutIcon />
            </button>
            <button class="btn btn-circle btn-sm" on:click={increaseZoom}>
                <ZoomInIcon />
            </button>

            <!-- 회전 컨트롤 버튼 -->
            <button class="btn btn-circle btn-sm" on:click={() => rotate('left')}>
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 15 3 9m0 0 6-6M3 9h12a6 6 0 0 1 0 12h-3" />
                </svg>
            </button>
            <button class="btn btn-circle btn-sm" on:click={() => rotate('right')}>
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="m15 15 6-6m0 0-6-6m6 6H9a6 6 0 0 0 0 12h3" />
                </svg>
            </button>
        </div>

        <!-- 액션 버튼 -->
        <div class="thumbnail-button">
            <button class="btn btn-circle btn-sm" on:click={processCroppedImages}>
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75 11.25 15 15 9.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
                </svg>
            </button>
            <button class="btn btn-circle btn-sm" on:click={resetCurrentCrop}>
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99" />
                </svg>
            </button>
            {#if localImages.length > 1}
            <button class="btn btn-circle btn-sm" on:click={toggleThumbnailModal}>
                <FoldersIcon />
            </button>
            {/if}
        </div>

        <!-- 썸네일 모달 토글 버튼(이미지 순서변경, 2개 이상일 때만 표시) -->
        <!-- 이미지 전환 버튼 -->
        {#if selectedImageIndex > 0}
            <button
                    class="absolute left-6 top-1/2 transform -translate-y-1/2 btn btn-circle btn-outline arrow-button"
                    on:click={selectPreviousImage}
            >
                <LeftArrowIcon />
            </button>
        {/if}

        {#if selectedImageIndex < localImages.length - 1}
            <button
                    class="absolute right-6 top-1/2 transform -translate-y-1/2 btn btn-circle btn-outline arrow-button"
                    on:click={selectNextImage}
            >
                <RightArrowIcon />
            </button>
        {/if}
    {/if}
</div>

<!-- 이미지 인디케이터 -->
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
