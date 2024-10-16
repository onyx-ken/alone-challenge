<script>
    import { onMount, onDestroy } from 'svelte';
    import Card from '$lib/components/main/accounts/ui/Card.svelte';
    import GoogleIcon from "$lib/components/icon/GoogleIcon.svelte";
    import FaceBookIcon from "$lib/components/icon/FaceBookIcon.svelte";
    import { getApiEndpoints } from '$lib/apiEndpoints';
    import { userStore, fetchUserInfo } from '$lib/stores/userStore';

    let nickName = "JohnDoe";
    let bio = "I'm passionate about setting and achieving goals!";
    let points = 1250;
    let email = "johndoe@example.com";
    let provider = "FACEBOOK";
    let profileUrl = "";
    let profileImageId = 0;

    const {USER} = getApiEndpoints();
    const maxLength = 150;
    let isOverLimit = false;

    // userStore에서 사용자 정보 가져오기
    let unsubscribe;

    onMount(() => {
        unsubscribe = userStore.subscribe(userData => {
            if (userData) {
                ({nickName, bio, points, email, provider, profileImageId} = userData);
                if (profileImageId !== 0) {
                    profileUrl = `${USER}/users/files/${profileImageId}`;
                } else {
                    profileUrl = ''; // 기본 이미지 설정 가능
                }
            } else {
                console.error('사용자 정보가 없습니다.');
            }
        });
    });

    // 컴포넌트 언마운트 시 구독 해제
    onDestroy(() => {
        if (unsubscribe) {
            unsubscribe();
        }
    });

    const handleAvatarChange = (event) => {
        const file = event.target.files?.[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                profileUrl = reader.result;
            };
            reader.readAsDataURL(file);
        }
    }

    const handleBioInput = (event) => {
        bio = event.target.value;
        isOverLimit = bio.length > maxLength;
    }

    async function handleSave() {
        if (isOverLimit) {
            alert("자기소개는 최대 150자까지 입력 가능합니다.");
            return;
        }

        try {
            const formData = new FormData();
            formData.append('nickName', nickName);
            formData.append('bio', bio);

            const fileInput = document.getElementById('profileImage');
            if (fileInput.files.length > 0) {
                formData.append('profileImage', fileInput.files[0]);
            }

            const response = await fetch(`${USER}/users/me/profile`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: formData
            });

            if (response.ok) {
                await fetchUserInfo();
                alert("프로필이 성공적으로 업데이트되었습니다!");
            } else {
                alert("프로필 업데이트에 실패했습니다. 나중에 다시 시도해주세요.");
            }
        } catch (error) {
            console.error('프로필 업데이트 중 오류 발생:', error);
            alert("프로필을 업데이트하는 중 오류가 발생했습니다. 나중에 다시 시도해주세요.");
        }
    }

    const getProviderIcon = (provider) => {
        const icons = {
            'GOOGLE': GoogleIcon,
            'FACEBOOK': FaceBookIcon,
        };
        return icons[provider] || null;
    }
</script>

<Card title="내 프로필">
    <!-- Profile Image -->
    <div class="flex flex-col space-y-4 p-4 items-center mt-4">
        <div class="avatar w-32 h-32">
            {#if profileUrl}
                <img src={profileUrl} alt="Profile picture" class="rounded-full object-cover"/>
            {:else}
                <!-- 기본 프로필 이미지 -->
                <div class="bg-gray-200 rounded-full w-full h-full flex items-center justify-center">
                    <span class="text-gray-500 text-4xl">?</span>
                </div>
            {/if}
        </div>
        <div class="flex items-center">
            <input
                    type="file"
                    accept="image/*"
                    on:change={handleAvatarChange}
                    class="hidden"
                    id="profileImage"
            />
            <label for="profileImage" class="btn btn-outline btn-sm">
                <!-- 아이콘 및 텍스트 -->
                이미지 변경하기
            </label>
        </div>
    </div>

    <!-- Nickname -->
    <div class="form-control mt-4">
        <label class="label" for="nickname">닉네임</label>
        <input
                id="nickname"
                bind:value={nickName}
                class="input input-bordered"
        />
    </div>

    <!-- Bio -->
    <div class="form-control mt-4">
        <label class="label" for="bio">자기소개</label>
        <textarea
                id="bio"
                bind:value={bio}
                on:input={handleBioInput}
                rows="4"
                class="textarea textarea-bordered"
                style="resize: none; overflow-y: auto;"
                placeholder="자기소개를 입력하세요 (최대 150자)"
        ></textarea>
        <small class="mt-1 {isOverLimit ? 'text-red-500' : 'text-base-content'}">
            {bio.length}/{maxLength} 자
        </small>
    </div>

    <!-- Points -->
    <div class="flex items-center space-x-2 mt-4">
        <!-- 포인트 아이콘 및 텍스트 -->
        <span class="font-semibold">포인트:</span>
        <span>{points}</span>
    </div>

    <!-- Email -->
    <div class="flex items-center space-x-2 mt-4">
        <!-- 이메일 아이콘 및 텍스트 -->
        <span class="font-semibold">Email:</span>
        <span>{email}</span>
    </div>

    <!-- OAuth Providers -->
    <div class="mt-4">
        <span class="font-semibold">연결된 계정 :</span>
        <div class="flex space-x-2 mt-2">
            <div class="badge badge-secondary flex items-center">
                {#if getProviderIcon(provider)}
                    <svelte:component this={getProviderIcon(provider)}/>
                {/if}
                {provider}
            </div>
        </div>
    </div>

    <!-- Save Button -->
    <button on:click={handleSave} class="btn btn-primary w-full mt-4">
        프로필 변경하기
    </button>
</Card>
