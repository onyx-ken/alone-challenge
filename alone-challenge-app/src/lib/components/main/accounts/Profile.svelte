<script>
    import Card from '$lib/components/main/accounts/ui/Card.svelte';
    import {onMount} from 'svelte';
    import {getApiEndpoints} from '$lib/apiEndpoints';

    let nickName = "JohnDoe";
    let bio = "I'm passionate about setting and achieving goals!";
    let points = 1250;
    let email = "johndoe@example.com";
    let provider = "FACEBOOK";
    let profileUrl = "";
    let profileImageId = 0;

    const {USER} = getApiEndpoints(); // 환경에 따른 API 엔드포인트 주소 불러오기

    const maxLength = 150;
    let isOverLimit = false;

    onMount(async () => {
        try {
            const response = await fetch(`${USER}/users/me/profile`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                }
            });

            if (response.ok) {
                const data = await response.json();
                nickName = data.nickName;
                bio = data.bio;
                points = data.points;
                email = data.email;
                provider = data.provider;
                profileImageId = data.profileImageId;

                // profileImageId가 유효한 값일 때만 profileUrl을 업데이트
                if (profileImageId !== 0) {
                    profileUrl = `${USER}/users/files/${profileImageId}`;
                }
            } else {
                console.error('Failed to fetch user data:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching user data:', error);
        }
    });

    function handleAvatarChange(event) {
        const file = event.target.files?.[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                profileUrl = reader.result;
            };
            reader.readAsDataURL(file);
        }
    }

    function handleBioInput(event) {
        bio = event.target.value;
        isOverLimit = bio.length > maxLength;
    }

    async function handleSave() {
        if (isOverLimit) {
            alert("자기소개는 최대 150자까지 입력 가능합니다.");
            return;
        }

        try {
            // FormData 객체 생성
            const formData = new FormData();
            formData.append('nickName', nickName);
            formData.append('bio', bio);

            // 프로필 이미지가 있으면 FormData에 추가
            const fileInput = document.getElementById('profileImage');
            if (fileInput.files.length > 0) {
                formData.append('profileImage', fileInput.files[0]);
            }

            const response = await fetch(`${USER}/users/me/profile`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: formData // FormData 객체를 body에 포함
            });

            if (response.ok) {
                alert("Profile updated successfully!");
            } else {
                alert("Failed to update profile. Please try again later.");
            }
        } catch (error) {
            console.error('Error updating profile:', error);
            alert("An error occurred while updating your profile. Please try again later.");
        }
    }

    function getProviderIcon(provider) {
        switch (provider) {
            case 'GOOGLE':
                return `
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                    </svg>`;
            case 'FACEBOOK':
                return `
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M18 2h-4a4 4 0 00-4 4v4H6v4h4v8h4v-8h4l1-4h-5V6a1 1 0 011-1h4V2z"/>
                    </svg>`;
            case 'GITHUB':
                return `
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 2a10 10 0 00-3.16 19.48c.5.09.68-.22.68-.48v-1.7c-2.77.6-3.36-1.34-3.36-1.34-.45-1.16-1.11-1.48-1.11-1.48-.91-.62.07-.6.07-.6 1 .07 1.53 1.02 1.53 1.02.89 1.52 2.34 1.08 2.9.82.09-.65.35-1.08.64-1.32-2.22-.25-4.56-1.11-4.56-4.94 0-1.09.39-1.98 1.03-2.68-.1-.26-.45-1.28.1-2.66 0 0 .84-.27 2.75 1.02A9.62 9.62 0 0112 7.5a9.62 9.62 0 012.49.34c1.9-1.29 2.74-1.02 2.74-1.02.55 1.38.21 2.4.11 2.66.65.7 1.03 1.59 1.03 2.68 0 3.85-2.35 4.68-4.59 4.93.36.31.68.91.68 1.84v2.73c0 .26.18.58.68.48A10 10 0 0012 2z"/>
                    </svg>`;
            default:
                return ''; // 알 수 없는 provider는 아이콘 없음
        }
    }
</script>

<Card title="내 프로필">
    <!-- Profile Image -->
    <div class="flex flex-col items-center space-y-4 mt-4">
        <div class="avatar w-32 h-32">
            <img src={profileUrl} alt="Profile picture" class="rounded-full object-cover"/>
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
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24"
                     stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M15 10l4.3-4.3c.4-.4.4-1 0-1.4L18 2c-.4-.4-1-.4-1.4 0L12.3 6.3M9 16v4.3c0 .6.4 1 1 1h4c.6 0 1-.4 1-1V16m-8-4V6c0-.6.4-1 1-1h4c.6 0 1 .4 1 1v6m4 0V6c0-.6.4-1 1-1h4c.6 0 1-.4 1 1v6"/>
                </svg>
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
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-yellow-500" fill="none" viewBox="0 0 24 24"
             stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M12 6l3.09 6.26L22 12l-5 4.87L18.18 22 12 18.67 5.82 22l1.18-5.13L2 12l6.91-.74L12 6z"/>
        </svg>
        <span class="font-semibold">포인트:</span>
        <span>{points}</span>
    </div>

    <!-- Email -->
    <div class="flex items-center space-x-2 mt-4">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M16 12l-4-4-4 4m0 4l4-4 4 4"/>
        </svg>
        <span class="font-semibold">Email:</span>
        <span>{email}</span>
    </div>

    <!-- OAuth Providers -->
    <div class="mt-4">
        <span class="font-semibold">연결된 계정 :</span>
        <div class="flex space-x-2 mt-2">
            <div class="badge badge-secondary flex items-center">
                {@html getProviderIcon(provider)}
                {provider}
            </div>
        </div>
    </div>

    <!-- Save Button -->
    <button on:click={handleSave} class="btn btn-primary w-full mt-4">
        프로필 변경하기
    </button>
</Card>
