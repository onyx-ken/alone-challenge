<script>
    import { getApiEndpoints } from '$lib/apiEndpoints';
    import GoogleLoginIcon from "$lib/components/icon/GoogleLoginIcon.svelte";

    function handleGoogleSignIn() {
        const { USER } = getApiEndpoints();
        window.location.href = `${USER}/oauth2/authorization/google`;
    }
</script>

<div class="card w-full max-w-md">
    <!-- Card Header -->
    <div class="card-header flex flex-col items-center space-y-2">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary" viewBox="0 0 24 24" fill="none" stroke="currentColor">
            <circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="2" fill="none" />
            <path d="M7 12l3 3l7-7" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <h1 class="text-2xl font-bold">나혼자 챌린지</h1>
    </div>
    <!-- Card Content -->
    <div class="card-body space-y-4">
        <p class="text-center text-muted-foreground">
            자신만의 소소한 목표를 만들고 도전하세요, 그리고 다른 사람들과 공유해 보세요!
        </p>
        <button class="btn btn-primary w-full" on:click={handleGoogleSignIn}>
            <GoogleLoginIcon />
            Google계정으로 서비스 이용하기
        </button>
    </div>
    <!-- Card Footer -->
    <div class="card-footer flex flex-col items-center space-y-2">
        <p class="text-sm text-muted-foreground text-center">
            서비스 이용과 함께 해당 서비스의 약관, 개인 정보 보호 정책 및 쿠키 사용에 동의합니다.
        </p>
    </div>
</div>
