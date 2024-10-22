<script>
    import { onMount, onDestroy } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import { format } from 'date-fns';
    import { v4 as uuidv4 } from 'uuid';
    import { getApiEndpoints } from '$lib/apiEndpoints';
    import ImageUpload from '$lib/components/main/ui/modal/steps/ImageUpload.svelte';
    import ImageEdit from '$lib/components/main/ui/modal/steps/ImageEdit.svelte';
    import ChallengeDetails from '$lib/components/main/ui/modal/steps/ChallengeDetails.svelte';
    import ChallengeCertificate from '$lib/components/main/ui/modal/steps/ChallengeCertificate.svelte';
    import BackgroundSelector from '$lib/components/main/ui/modal/steps/BackgroundSelector.svelte';
    const accessToken = localStorage.getItem('accessToken');


    const { CHALLENGE } = getApiEndpoints();
    const dispatch = createEventDispatcher();
    export let onClose = () => {};

    // ChallengeDetails 컴포넌트 인스턴스를 저장할 변수
    let challengeDetailsComponent;

    onMount(() => {
        window.addEventListener('keydown', handleKeydown);
        return () => {
            window.removeEventListener('keydown', handleKeydown);
        };
    });

    // Step 상수를 객체로 정의
    const Step = {
        UPLOAD: 1,
        CROP: 2,
        DETAILS: 3,
        PREVIEW: 4
    };

    // 도전 관련 상태 관리
    let challengeData = {
        nickName: '',          // 사용자 닉네임
        startDate: '',         // 시작 날짜
        endDate: '',           // 종료 날짜
        challengeDescription: '' // 도전 내용
    };

    let step = Step.UPLOAD;  // 초기값을 상수로 설정
    let uploadedImages = [];
    let selectedImageData = {};
    let selectedBackground = "/background1.png"; // 기본 배경 이미지
    let userText = ""; // 사용자 입력 글

    const handleKeydown = (event) => {
        if (event.key === 'Escape') {
            onClose();
        }
    }

    const handleImageUpload = (event) => {
        const files = event.detail;
        if (Array.isArray(files) && files.length > 0) {
            // 각 파일에 고유한 id를 부여합니다.
            uploadedImages = files.map((file) => ({
                id: uuidv4(), // UUID 고유한 값 사용
                file,
                preview: URL.createObjectURL(file)
            }));
            step = Step.CROP;  // 2단계로 이동
        } else {
            console.error("handleImageUpload: Expected an array, but got", typeof files);
        }
    };

    const handleCropComplete = (croppedAreaPixels) => {
        selectedImageData = croppedAreaPixels;
    }

    const handleImageEdit = () => {
        step = Step.DETAILS;
    }

    const formatDate = (date) => {
        return format(date, 'yyyy.MM.dd');
    };

    const validateChallengeDetails = () => {
        // 자식 컴포넌트의 데이터를 가져옵니다.
        const {
            dateRange,
            challengeType,
            challengeCategory,
            challengeTarget,
            nickName
        } = challengeDetailsComponent;

        // 필수 필드가 모두 입력되었는지 확인합니다.
        if (!dateRange || !challengeType || !challengeCategory || !challengeTarget) {
            return false;
        }

        // 도전 설명 생성
        const challengeDescription = `${challengeTarget} ${challengeCategory.ending}`;

        // 도전 데이터를 업데이트합니다.
        challengeData = {
            nickName,
            startDate: format(dateRange.from, 'yyyy-MM-dd'), // 백엔드와 호환되는 형식으로 변경
            endDate: format(dateRange.to, 'yyyy-MM-dd'),
            challengeDescription
        };

        return true;
    };


    const handleNextClick = () => {
        if (step === Step.DETAILS) {
            // 자식 컴포넌트의 데이터를 가져옵니다.
            if (validateChallengeDetails()) {
                // 데이터 유효성 검사를 통과하면 다음 단계로 이동
                step = Step.PREVIEW;
            } else {
                // 유효성 검사 실패 시 알림
                alert('모든 필드를 올바르게 입력해주세요.');
            }
            return;
        }

        if (step === Step.UPLOAD && uploadedImages.length === 0) {
            step = Step.DETAILS;  // 이미지를 업로드하지 않은 경우, 바로 도전내용 작성으로 이동
            return;
        }
        step++;
    };

    const handlePreviousClick = () => {
        if (step === Step.CROP) {
            uploadedImages = [];
            step = Step.UPLOAD;
            return;
        }

        if (step === Step.DETAILS) {
            step = uploadedImages.length === 0 ? Step.UPLOAD : Step.CROP;
            return;
        }

        if (step === Step.PREVIEW) {
            step = Step.DETAILS;
        }
    };

    const handleBackgroundSelect = (event) => {
        selectedBackground = event.detail; // 배경 선택 시 선택된 배경 이미지 변경
    }

    const handleTextChange = (event) => {
        userText = event.target.value; // 입력된 글 내용 저장
    }

    const selectBackground = (background) => {
        selectedBackground = background;
        dispatch('select', selectedBackground); // Svelte의 dispatch 함수를 사용해 이벤트 전달
    }

    const handleImagesUpdate = (updatedImages) => {
        uploadedImages = updatedImages;
    };

    // 추가된 변수: 대표 이미지 설정 체크박스 상태
    let setAsRepresentativeImage = false; // 기본값: false

    // 현재 선택된 이미지 인덱스 추적
    let selectedImageIndex = 0;

    // 체크박스 변경 핸들러
    const handleRepresentativeToggle = (event) => {
        setAsRepresentativeImage = event.target.checked;
        if (uploadedImages.length === 0) return;
        const selectedImage = uploadedImages[selectedImageIndex];
        if (!selectedImage) return;

        if (setAsRepresentativeImage) {
            if (selectedImageIndex !== 0) {
                // 선택된 이미지를 첫 번째로 이동
                uploadedImages = [selectedImage, ...uploadedImages.filter(img => img.id !== selectedImage.id)];
                selectedImageIndex = 0;
            }
        } else {
            if (selectedImageIndex !== uploadedImages.length -1) {
                // 선택된 이미지를 마지막으로 이동
                uploadedImages = [...uploadedImages.filter(img => img.id !== selectedImage.id), selectedImage];
                selectedImageIndex = uploadedImages.length -1;
            }
        }
    };

    const handleShare = async () => {
        try {


            // 이미지 배열 준비
            let imagesToUpload = [];

            if (uploadedImages.length === 0) {

            } else {
                if (setAsRepresentativeImage) {


                    uploadedImages.forEach((img, index) => {
                        imagesToUpload.push({
                            file: img.file,
                            order: index + 1,
                            type: 'USER_UPLOAD'
                        });
                    });
                } else {
                    // 도전장 이미지를 대표이미지로 설정하지 않은 경우
                    uploadedImages.forEach((img, index) => {
                        imagesToUpload.push({
                            file: img.file,
                            order: index,
                            type: 'USER_UPLOAD'
                        });
                    });

                }
            }

            // FormData 객체 생성
            const formData = new FormData();

            formData.append('nickName', challengeData.nickName);
            formData.append('startDate', challengeData.startDate);
            formData.append('endDate', challengeData.endDate);
            formData.append('mainContent', challengeData.challengeDescription);
            formData.append('additionalContent', userText);
            // todo 현재는 미완성 되어서 하드코딩으로 처리, 나중에 값 받아와서 처리
            formData.append('goalType', 'POSITIVE'); // 'POSITIVE' 또는 'NEGATIVE'

            imagesToUpload.forEach((imageData, index) => {
                formData.append(`images[${index}].file`, imageData.file);
                formData.append(`images[${index}].order`, imageData.order.toString());
                formData.append(`images[${index}].type`, imageData.type);
            });

            const isLocal = window.location.hostname === 'localhost';

            const headers = {
                'Authorization': `Bearer ${accessToken}`,
            };

            if (isLocal) {
                headers['X-User-Id'] = 1;
            }

            // 백엔드로 전송
            const response = await fetch(`${CHALLENGE}/challenges`, {
                headers,
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                alert('도전장이 성공적으로 공유되었습니다!');
                onClose();
            } else {
                const errorText = await response.text();
                console.error('Server error:', errorText);
                alert('도전장 공유에 실패했습니다.');
            }

        } catch (error) {
            console.error('Error sharing challenge:', error);
            alert('도전장 공유 중 오류가 발생했습니다.');
        }
    };

    onDestroy(() => {
        window.removeEventListener('keydown', handleKeydown);
    });

    $: title = step === Step.UPLOAD ? "새로운 도전하기" :
        step === Step.CROP ? "이미지 자르기" :
            step === Step.DETAILS ? "도전 내용 작성하기" :
                step === Step.PREVIEW ? "도전 공유하기" : "------";
</script>

<div class="modal modal-open">
    <div class="modal-box w-11/12 max-w-5xl p-8" role="dialog" aria-label={title}>
        <form method="dialog">
            <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" on:click={onClose}>✕</button>
        </form>
        <h3 class="font-bold text-lg mb-4">{title}</h3>

        <!-- 1단계: 이미지 업로드 -->
        {#if step === Step.UPLOAD}
            <ImageUpload on:upload={handleImageUpload} />
        {:else if step === Step.CROP}
            <!-- 2단계: 이미지 자르기 -->
            <ImageEdit
                    images={uploadedImages}
                    onImagesUpdate={handleImagesUpdate}
                    onSave={handleImageEdit}
                    onCropComplete={handleCropComplete}
            />
        {:else if step === Step.DETAILS}
            <!-- 3단계: 도전 내용 작성 -->
            <ChallengeDetails
                    bind:this={challengeDetailsComponent}
            />
        {/if}

        <!-- 4단계: 도전 공유 (도전장 미리보기, 배경 선택, 글 작성) -->
        {#if step === Step.PREVIEW}
            <div class="flex space-x-8">
                <!-- 왼쪽: 도전장 미리보기 -->
                <div class="w-2/3">
                    <h3 class="font-bold text-lg mb-4"> 미리보기</h3>
                    <div class="certificate-preview">
                        <ChallengeCertificate
                                nickName={challengeData.nickName}
                                startDate={challengeData.startDate}
                                endDate={challengeData.endDate}
                                challengeDescription={challengeData.challengeDescription}
                                background={selectedBackground}
                        />
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
                    <!-- 체크박스: 도전장 이미지를 대표이미지로 설정 -->
                    {#if uploadedImages.length > 0}
                        <div class="form-control mt-4">
                            <label class="label cursor-pointer">
                                <input type="checkbox" class="checkbox" bind:checked={setAsRepresentativeImage} on:change={handleRepresentativeToggle} />
                                <span class="label-text font-bold">도전장을 대표이미지로 설정</span>
                            </label>
                        </div>
                    {/if}
                </div>
            </div>
        {/if}

        <!-- 하단 네비게이션 -->
        <div class="modal-action flex justify-between items-center">
            {#if step === Step.UPLOAD}
                <p class="text-sm text-gray-500 text-center mx-auto">이미지 없이도 도전을 할 수 있습니다.</p>
                <button class="btn btn-primary ml-auto" on:click={handleNextClick}>다음</button>
            {/if}

            {#if step === Step.CROP}
                <button class="btn btn-outline" on:click={handlePreviousClick}>이전</button>
                <button class="btn btn-primary ml-auto" on:click={handleNextClick}>다음</button>
            {/if}

            {#if step === Step.DETAILS}
                <button class="btn btn-outline" on:click={handlePreviousClick}>이전</button>
                <button class="btn btn-primary ml-auto" on:click={handleNextClick}>다음</button>
            {/if}

            {#if step === Step.PREVIEW}
                <button class="btn btn-outline" on:click={handlePreviousClick}>이전</button>
                <button class="btn btn-primary ml-auto" on:click={handleShare}>공유하기</button>
            {/if}
        </div>
    </div>
</div>

<style>
    .modal-box {
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
