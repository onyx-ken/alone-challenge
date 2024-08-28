<script>
    import { getApiEndpoints } from '$lib/apiEndpoints';

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
            <svg class="mr-2 h-4 w-4" viewBox="0 0 24 24">
                <path
                        fill="currentColor"
                        d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                />
                <path
                        fill="currentColor"
                        d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                />
                <path
                        fill="currentColor"
                        d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                />
                <path
                        fill="currentColor"
                        d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                />
            </svg>
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
