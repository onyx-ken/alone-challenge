<script>
    import {onMount, onDestroy} from 'svelte';
    import {browser} from '$app/environment'; // browser 변수 임포트
    import Post from './Post.svelte';
    import XIcon from "$lib/components/icon/XIcon.svelte";
    import FeedDetailModal from "$lib/components/main/ui/FeedDetailModal.svelte";
    import {getApiEndpoints} from '$lib/apiEndpoints';
    import {initUserInfo, userStore} from '$lib/stores/userStore.js';

    const {CHALLENGE} = getApiEndpoints();

    // 게시물 및 좋아요 관련 상태 변수
    let posts = [];
    let likedChallengeIds = [];

    // 모달 관련 상태 변수
    let showModal = false;
    let selectedPost = null; // 선택된 게시물의 데이터 저장

    // 사용자 인증 정보
    let accessToken = '';
    let userId = '';
    let unsubscribe;

    // 환경 변수
    let isLocal = false;

    // 브라우저 환경에서만 window 객체를 참조
    if (browser) {
        isLocal = window.location.hostname === 'localhost';
    }

    // 컴포넌트가 마운트될 때 사용자 정보를 초기화하고, 게시물과 좋아요 데이터를 가져옵니다.
    onMount(async () => {
        try {
            // 사용자 정보 초기화
            await initUserInfo();
            unsubscribe = userStore.subscribe(userData => {
                if (userData) {
                    ({userId} = userData);
                } else {
                    console.error('사용자 정보가 없습니다.');
                }
            });
            accessToken = localStorage.getItem('accessToken');

            // 게시물 목록을 가져옵니다.
            await fetchPosts();

            // 사용자 로그인 상태 확인 후 좋아요 정보 가져오기
            if (userId && accessToken) {
                await fetchLikedChallenges();
            }
        } catch (error) {
            console.error('Error initializing feed:', error);
        }
    });

    // 컴포넌트가 언마운트될 때 구독 해제
    onDestroy(() => {
        if (unsubscribe) {
            unsubscribe();
        }
    });

    // 게시물 목록을 가져오는 함수
    async function fetchPosts() {
        try {
            const response = await fetch(`${CHALLENGE}/challenges`); // 페이지네이션 파라미터 포함
            if (!response.ok) {
                throw new Error(`Failed to fetch challenges: ${response.status}`);
            }
            const data = await response.json();
            const challenges = data.data;

            posts = challenges.map(challenge => ({
                id: challenge.challengeId,
                user: {
                    name: challenge.nickName,
                    avatar: challenge.avatarUrl || `https://i.pravatar.cc/150?img=${challenge.challengeId % 70}` // 실제 프로필 이미지 URL 사용
                },
                imageUrl: challenge.firstAttachedImageUrl,
                content: `${challenge.mainContent} | ${challenge.additionalContent}`,
                likes: challenge.likeCount,
                commentCount: challenge.commentCount,
            }));
        } catch (error) {
            console.error('Error fetching challenges:', error);
            // 에러 처리를 추가하여 사용자에게 알림을 표시할 수 있습니다.
        }
    }

    // 사용자가 좋아요를 누른 챌린지 ID 목록을 가져오는 함수
    async function fetchLikedChallenges() {
        console.log('fetchLikedChallenges');
        try {
            if (!userId || !accessToken) {
                console.warn('User not logged in. Skipping fetchLikedChallenges.');
                return;
            }
            if (posts.length === 0) {
                console.warn('No posts available to fetch likes for.');
                return;
            }
            const challengeIds = posts.map(post => post.id.toString()); // ID를 문자열 배열로 변환

            const headers = {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            };
            if (isLocal) {
                headers['X-User-Id'] = userId;
            }

            const response = await fetch(`${CHALLENGE}/challenges/likes/check`, {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({challengeIds})
            });
            if (!response.ok) {
                throw new Error(`Failed to fetch liked challenges: ${response.status}`);
            }
            const data = await response.json();
            likedChallengeIds = data.data || []; // 좋아요를 누른 챌린지 ID 목록
        } catch (error) {
            console.error('Error fetching liked challenges:', error);
            // 에러 처리를 추가하여 사용자에게 알림을 표시할 수 있습니다.
        }
    }

    // Post 컴포넌트에서 'openModal' 이벤트를 처리하는 함수
    function handleOpenModal(event) {
        selectedPost = event.detail; // 선택된 게시물의 데이터 저장
        showModal = true;
        disableScroll(); // 모달이 열릴 때 스크롤 비활성화
    }

    // 모달을 닫는 함수
    function closeModal() {
        showModal = false;
        selectedPost = null; // 선택된 게시물 데이터 초기화
        enableScroll();  // 모달이 닫힐 때 스크롤 활성화
    }

    // 모달 외부 클릭 시 창 닫기
    function handleClickOutside(event) {
        if (event.target.classList.contains('modal-overlay')) {
            closeModal();
        }
    }

    // 모달이 열릴 때 스크롤 비활성화
    function disableScroll() {
        if (browser) {
            document.body.style.overflow = 'hidden';
        }
    }

    // 모달이 닫힐 때 스크롤 활성화
    function enableScroll() {
        if (browser) {
            document.body.style.overflow = '';
        }
    }
</script>

<!-- 피드 영역 -->
<div class="feed">
    {#each posts as post}
        <Post
                {...post}
                isLiked={likedChallengeIds.includes(post.id.toString())}
                on:openModal={handleOpenModal}
        />
    {/each}
</div>

<!-- 모달 표시 -->
{#if showModal && selectedPost}
    <!-- 닫기 버튼 -->
    <button
            class="fixed top-4 right-4 text-gray-600 hover:text-gray-800 z-50"
            on:click={closeModal}
            aria-label="Close modal"
    >
        <XIcon/>
    </button>

    <!-- 모달 배경 -->
    <div
            class="fixed inset-0 bg-gray-800 bg-opacity-90 flex justify-center items-center z-50 modal-overlay"
            role="dialog"
            aria-modal="true"
            on:click={handleClickOutside}
            on:keydown={(event) => {
            if (event.key === 'Escape') closeModal(); // 'Escape' 키를 누르면 모달 닫기
        }}
    >
        <!-- 모달 본체 -->
        <div class="relative w-4/5 h-4/5 flex flex-col md:flex-row rounded-lg overflow-hidden shadow-lg">
            <FeedDetailModal post={selectedPost} on:close={closeModal}/>
        </div>
    </div>
{/if}

<style>
    .feed {
        display: flex;
        flex-direction: column;
        gap: 1rem;
    }

    .pagination {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 1rem;
        margin-top: 1rem;
    }
</style>
