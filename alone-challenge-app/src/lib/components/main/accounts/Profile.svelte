<script>
    import { onMount } from 'svelte';
    import Card from '$lib/components/main/accounts/ui/Card.svelte';
    import GoogleIcon from "$lib/components/icon/GoogleIcon.svelte";
    import FaceBookIcon from "$lib/components/icon/FaceBookIcon.svelte";
    import { getApiEndpoints } from '$lib/apiEndpoints';

    let nickName = "JohnDoe";
    let bio = "I'm passionate about setting and achieving goals!";
    let points = 1250;
    let email = "johndoe@example.com";
    let provider = "FACEBOOK";
    let profileUrl = "";
    let profileImageId = 0;

    const { USER } = getApiEndpoints();
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
                ({ nickName, bio, points, email, provider, profileImageId } = data);

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
                alert("Profile updated successfully!");
            } else {
                alert("Failed to update profile. Please try again later.");
            }
        } catch (error) {
            console.error('Error updating profile:', error);
            alert("An error occurred while updating your profile. Please try again later.");
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
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M6.827 6.175A2.31 2.31 0 0 1 5.186 7.23c-.38.054-.757.112-1.134.175C2.999 7.58 2.25 8.507 2.25 9.574V18a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18V9.574c0-1.067-.75-1.994-1.802-2.169a47.865 47.865 0 0 0-1.134-.175 2.31 2.31 0 0 1-1.64-1.055l-.822-1.316a2.192 2.192 0 0 0-1.736-1.039 48.774 48.774 0 0 0-5.232 0 2.192 2.192 0 0 0-1.736 1.039l-.821 1.316Z" />
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 12.75a4.5 4.5 0 1 1-9 0 4.5 4.5 0 0 1 9 0ZM18.75 10.5h.008v.008h-.008V10.5Z" />
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
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-yellow-500">
            <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
        </svg>
        <span class="font-semibold">포인트:</span>
        <span>{points}</span>
    </div>

    <!-- Email -->
    <div class="flex items-center space-x-2 mt-4">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M21.75 6.75v10.5a2.25 2.25 0 0 1-2.25 2.25h-15a2.25 2.25 0 0 1-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0 0 19.5 4.5h-15a2.25 2.25 0 0 0-2.25 2.25m19.5 0v.243a2.25 2.25 0 0 1-1.07 1.916l-7.5 4.615a2.25 2.25 0 0 1-2.36 0L3.32 8.91a2.25 2.25 0 0 1-1.07-1.916V6.75" />
        </svg>
        <span class="font-semibold">Email:</span>
        <span>{email}</span>
    </div>

    <!-- OAuth Providers -->
    <div class="mt-4">
        <span class="font-semibold">연결된 계정 :</span>
        <div class="flex space-x-2 mt-2">
            <div class="badge badge-secondary flex items-center">
                {#if getProviderIcon(provider)}
                    <svelte:component this={getProviderIcon(provider)} />
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
