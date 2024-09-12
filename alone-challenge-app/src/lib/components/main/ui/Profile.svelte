<script>
    import PostDetailModal from '$lib/components/main/ui/PostDetailModal.svelte';
    import HeartIcon from "$lib/components/icon/HeartIcon.svelte";
    import MessgeIcon from "$lib/components/icon/MessageIcon.svelte";
    import DirectionIcon from "$lib/components/icon/DirectionIcon.svelte";
    import XIcon from "$lib/components/icon/XIcon.svelte";

    let isFollowing = false;
    let selectedPost = null;  // 현재 선택된 게시물
    let selectedPostIndex = 0;  // 현재 선택된 게시물의 인덱스
    let showModal = false;

    let user = {
        nickname: "user1",
        profileImage: "https://i.pravatar.cc/150?img=1?height=100&width=100",
        posts: 42,
        followers: 1234,
        following: 567,
        bio: "Passionate about setting and achieving goals. Join me on my journey!",
        postsList: [
            { id: 1, image: "https://cdn2.thecatapi.com/images/35g.jpg?height=300&width=300", likes: 120, comments: 15 },
            { id: 2, image: "https://cdn2.thecatapi.com/images/8fr.png?height=300&width=300", likes: 89, comments: 7 },
            { id: 3, image: "https://cdn2.thecatapi.com/images/95l.jpg?height=300&width=300", likes: 230, comments: 32 },
            { id: 4, image: "https://cdn2.thecatapi.com/images/apo.jpg?height=300&width=300", likes: 56, comments: 3 },
            { id: 5, image: "https://cdn2.thecatapi.com/images/MTY1Mzg2OA.jpg?height=300&width=300", likes: 180, comments: 22 },
            { id: 6, image: "https://cdn2.thecatapi.com/images/MTg0OTAxMw.jpg?height=300&width=300", likes: 95, comments: 11 },
            { id: 7, image: "https://cdn2.thecatapi.com/images/TuXvdAni3.jpg?height=300&width=300", likes: 26, comments: 32 },
            { id: 8, image: "https://cdn2.thecatapi.com/images/hxlto6Z4I.jpg?height=300&width=300", likes: 80, comments: 15 },
            { id: 9, image: "https://cdn2.thecatapi.com/images/Sy9SgPE0B.jpg?height=300&width=300", likes: 55, comments: 110 },
        ]
    };

    const handleFollowClick = () => {
        isFollowing = !isFollowing;
    };

    const handlePostClick = (postId) => {
        selectedPostIndex = user.postsList.findIndex(post => post.id === postId);
        selectedPost = user.postsList[selectedPostIndex];
        showModal = true;
        disableScroll();  // 모달이 열릴 때 스크롤 비활성화
    };

    const closeModal = () => {
        showModal = false;
        enableScroll();  // 모달이 닫힐 때 스크롤 활성화
    };

    const handlePrevPost = () => {
        selectedPostIndex = (selectedPostIndex - 1 + user.postsList.length) % user.postsList.length;
        selectedPost = user.postsList[selectedPostIndex];
    };

    const handleNextPost = () => {
        selectedPostIndex = (selectedPostIndex + 1) % user.postsList.length;
        selectedPost = user.postsList[selectedPostIndex];
    };

    // 모달 외부 클릭 시 창 닫기
    const handleClickOutside = (event) => {
        if (event.target.classList.contains('modal-overlay')) {
            closeModal();
        }
    };

    const disableScroll = () => {
        document.documentElement.style.overflow = 'hidden';  // html 요소의 스크롤 비활성화
        document.body.style.overflow = 'hidden';  // body 요소의 스크롤 비활성화
    };

    const enableScroll = () => {
        document.documentElement.style.overflow = '';  // html 요소의 스크롤 복구
        document.body.style.overflow = '';  // body 요소의 스크롤 복구
    };
</script>

<!-- 프로필 화면 -->
<div class="container mx-auto px-4 py-8 max-w-3xl">
    <!-- 사용자 정보 -->
    <div class="flex flex-col md:flex-row items-center md:items-start mb-8">
        <div class="avatar w-24 h-24 md:w-32 md:h-32 mb-4 md:mb-0 md:mr-8">
            {#if user.profileImage}
                <img src={user.profileImage} alt={user.nickname} class="rounded-full"/>
            {:else}
                <div class="rounded-full bg-gray-200 flex items-center justify-center w-full h-full">
                    {user.nickname.slice(0, 2).toUpperCase()}
                </div>
            {/if}
        </div>

        <div class="flex-grow text-center md:text-left">
            <div class="flex flex-col md:flex-row items-center mb-4">
                <h1 class="text-2xl font-bold mr-4">{user.nickname}</h1>
                <button class={`btn ${isFollowing ? 'btn-outline' : 'btn-primary'}`} on:click={handleFollowClick}>
                    {isFollowing ? "Following" : "Follow"}
                </button>
            </div>
            <div class="flex justify-center md:justify-start space-x-8 mb-4">
                <span><strong>{user.posts}</strong> posts</span>
                <span><strong>{user.followers}</strong> followers</span>
                <span><strong>{user.following}</strong> following</span>
            </div>
            <p class="text-sm">{user.bio}</p>
        </div>
    </div>

    <!-- 포스트 리스트 -->
    <div class="grid grid-cols-3 gap-1 md:gap-4">
        {#each user.postsList as post}
            <div
                    class="relative group cursor-pointer"
                    role="button"
                    tabindex="0"
                    aria-label="View post"
                    on:click={() => handlePostClick(post.id)}
                    on:keydown={(event) => {
                        if (event.key === 'Enter' || event.key === ' ') {
                        handlePostClick(post.id);
                        }
                    }}
            >
                <div class="card shadow-lg">
                    <img src={post.image} alt={`Post ${post.id}`} class="w-full aspect-square object-cover"/>
                    <div class="absolute inset-0 bg-opacity-50 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                        <div class="text-white flex items-center space-x-4">
                            <span class="flex items-center">
                                <HeartIcon fill="none" className="w-6 h-6"/>
                                {post.likes}
                            </span>
                            <span class="flex items-center">
                                <MessgeIcon className="w-6 h-6"/>
                                {post.comments}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        {/each}
    </div>

    <!-- 모달 표시 -->
    {#if showModal && selectedPost}
        <!-- 닫기 버튼 -->
        <button
                class="fixed top-4 right-4 text-gray-600 hover:text-gray-800 z-50"
                style="z-index: 1001;"
                on:click={closeModal}
                aria-label="Close modal"
        >
            <XIcon />
        </button>

        <!-- 모달 배경 -->
        <div
                class="fixed inset-0 bg-gray-800 bg-opacity-90 flex justify-center items-center z-50 modal-overlay"
                role="button"
                tabindex="0"
                aria-label="Close modal by clicking outside"
                on:click={handleClickOutside}
                on:keydown={(event) => {
            if (event.key === 'Escape') closeModal(); // 'Escape' 키를 누르면 모달 닫기
        }}
        >
            <!-- 왼쪽 화살표 -->
            {#if selectedPostIndex > 0}
                <button
                        class="absolute left-4 text-white z-[1002]"
                        on:click|stopPropagation={handlePrevPost}
                        aria-label="Previous post"
                        on:keydown={(event) => {
                    if (event.key === 'Enter' || event.key === ' ') {
                        handlePrevPost();
                    }
                }}
                >
                    <DirectionIcon />
                </button>
            {/if}

            <!-- 모달 본체 -->
            <div class="relative w-4/5 h-4/5 flex flex-col md:flex-row rounded-lg overflow-hidden shadow-lg" role="dialog" aria-modal="true">
                <PostDetailModal currentPostIndex={selectedPostIndex} onClose={closeModal} />
            </div>

            <!-- 오른쪽 화살표 -->
            <button
                    class="absolute right-4 text-white z-50"
                    on:click={handleNextPost}
                    aria-label="Next post"
                    on:keydown={(event) => {
                if (event.key === 'Enter' || event.key === ' ') {
                    handleNextPost();
                }
            }}
            >
                <DirectionIcon direction="right"/>
            </button>
        </div>
    {/if}
</div>
