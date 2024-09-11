<script>
    import Cropper from 'svelte-easy-crop';
    import { writable } from 'svelte/store';
    import { tick } from 'svelte';

    export let images = [];  // 이미지 배열을 전달받습니다
    export let onCropComplete = () => {};

    let crop = writable({ x: 0, y: 0 });
    let zoomLevels = writable(images.map(() => 1));  // 각 이미지에 대한 ZOOM 레벨을 개별적으로 관리
    let aspect = writable(1);  // 기본 비율 1:1로 설정
    let croppedAreaPixels = [];
    let selectedImageIndex = 0;  // 자를 이미지를 선택할 인덱스
    let isTransitioning = false;

    // 이미지가 변경될 때마다 실행되는 반응성 블록
    $: {
        if (isTransitioning) {
            tick().then(() => {
                isTransitioning = false; // 상태 업데이트가 완료된 후 전환을 허용
            });
        }
    }

    const handleCropComplete = (croppedAreaPixelsResult) => {
        croppedAreaPixels[selectedImageIndex] = croppedAreaPixelsResult;
        onCropComplete(croppedAreaPixels);
    }

    async function selectNextImage() {
        if (isTransitioning || selectedImageIndex >= images.length - 1) return;

        isTransitioning = true;
        selectedImageIndex++;
        resetCropState();
        await tick(); // 렌더링이 완료될 때까지 대기
        isTransitioning = false;
    }

    async function selectPreviousImage() {
        if (isTransitioning || selectedImageIndex <= 0) return;

        isTransitioning = true;
        selectedImageIndex--;
        resetCropState();
        await tick();
        isTransitioning = false;
    }

    const resetCropState = () => {
        crop.set({ x: 0, y: 0 });
    }

    const  setAspect = (ratio) => {
        aspect.set(ratio || 1); // 비율 설정, 원본(기본 비율)일 경우 1로 설정
        crop.set({ x: 0, y: 0 });  // 현재 보고 있는 이미지의 자르기 위치만 초기화
    }
</script>

<div class="relative w-full h-96 overflow-hidden">
    <Cropper
            image={images[selectedImageIndex]?.preview}
            bind:crop={$crop}
            bind:zoom={$zoomLevels[selectedImageIndex]}
            bind:aspect={$aspect}
            on:cropcomplete={handleCropComplete}
            background={false}
            style={{
                cropAreaStyle: {
                    backgroundColor: 'transparent',  // 자르기 영역 바깥은 투명하게
                },
                mediaStyle: {
                    objectFit: 'cover',  // 이미지가 자르기 영역을 완전히 채우도록 설정
                    width: '100%',
                    height: '100%'
                }
            }}
    />

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

<!-- 비율 선택 버튼 -->
<div class="flex justify-center mt-4 space-x-2">
    <button class="btn btn-outline" on:click={() => setAspect(1)}>1:1</button>
    <button class="btn btn-outline" on:click={() => setAspect(4/5)}>4:5</button>
    <button class="btn btn-outline" on:click={() => setAspect(16/9)}>16:9</button>
    <button class="btn btn-outline" on:click={() => setAspect(null)}>원본</button>
</div>

<div class="mt-5">
    <label class="block">
        Zoom
        <input type="range" min="1" max="3" step="0.1" bind:value={$zoomLevels[selectedImageIndex]} class="range range-primary" />
    </label>
</div>
