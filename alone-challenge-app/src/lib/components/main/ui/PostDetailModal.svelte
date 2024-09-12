<script>
    import HeartIcon from "$lib/components/icon/HeartIcon.svelte";
    import MessageIcon from "$lib/components/icon/MessageIcon.svelte";

    export let currentPostIndex = 0;  // 현재 포스트의 인덱스
    let commentInput = null;  // input 요소에 접근하기 위한 ref
    let commentText = ''; // 댓글 입력값

    // 더미 포스트 데이터
    let postsList = [
        {
            id: 1,
            image: "https://cdn2.thecatapi.com/images/35g.jpg?height=300&width=300",
            likes: 120,
            comments: 15,
            datePosted: "2024-08-10",
            caption: "This is a caption for post 1.",
            isLiked : false,
            user: {
                nickname: "user1",
                profileImage: "https://i.pravatar.cc/150?img=1",
                followers: 1234,
                following: 567
            },
            commentsList: [
                { id: 1, user: { nickname: "user2" }, text: "Cute cat!" },
                { id: 2, user: { nickname: "user3" }, text: "Amazing picture!" },
                { id: 3, user: { nickname: "user4" }, text: "Love it!" }
            ]
        },
        {
            id: 2,
            image: "https://cdn2.thecatapi.com/images/8fr.png?height=300&width=300",
            likes: 89,
            comments: 7,
            datePosted: "2024-08-11",
            caption: "This is a caption for post 2.",
            isLiked : false,
            user: {
                nickname: "user2",
                profileImage: "https://i.pravatar.cc/150?img=2",
                followers: 987,
                following: 340
            },
            commentsList: [
                { id: 1, user: { nickname: "user1" }, text: "Nice post!" },
                { id: 2, user: { nickname: "user5" }, text: "Love it!" }
            ]
        },
        {
            id: 3,
            image: "https://cdn2.thecatapi.com/images/95l.jpg?height=300&width=300",
            likes: 230,
            comments: 32,
            datePosted: "2024-08-12",
            caption: "This is a caption for post 3.",
            isLiked : false,
            user: {
                nickname: "user3",
                profileImage: "https://i.pravatar.cc/150?img=3",
                followers: 764,
                following: 420
            },
            commentsList: [
                { id: 1, user: { nickname: "user6" }, text: "So adorable!" }
            ]
        },
        {
            id: 4,
            image: "https://cdn2.thecatapi.com/images/apo.jpg?height=300&width=300",
            likes: 56,
            comments: 3,
            datePosted: "2024-08-13",
            caption: "This is a caption for post 4.",
            isLiked : false,
            user: {
                nickname: "user4",
                profileImage: "https://i.pravatar.cc/150?img=4",
                followers: 432,
                following: 290
            },
            commentsList: [
                { id: 1, user: { nickname: "user7" }, text: "Such a cute cat!" }
            ]
        }
    ];

    // 현재 선택된 포스트 데이터를 가져오기
    $: post = postsList[currentPostIndex];

    const handleFollowClick = () => {
        post.user.isFollowing = !post.user.isFollowing;
    };

    const handleCommentIconClick = () => {
        commentInput.focus();
    }

    const toggleLike = () => {
        post.isLiked = !post.isLiked;
        post.likes += post.isLiked ? 1 : -1;
    }

    const addComment = () => {
        if (commentText.trim() !== '') {
            // 새로운 댓글을 배열에 추가하고 배열의 참조를 변경
            post.commentsList = [
                ...post.commentsList, // 기존 댓글 목록을 펼침
                {
                    id: post.commentsList.length + 1,  // 임시로 댓글 ID 할당
                    user: { nickname: "currentUser" },  // 현재 사용자 이름 (임시)
                    text: commentText
                }
            ];

            // 댓글 입력창 비우기
            commentText = '';
        }
    };

</script>

<!-- 모달 배경 -->
<div class="modal-overlay fixed inset-0 bg-opacity-50 flex justify-center items-center z-50">
    <div class="w-4/5 h-4/5 flex flex-col md:flex-row rounded-lg overflow-hidden shadow-lg relative">
        <!-- 왼쪽: 게시물 이미지 영역 (1-3/5) -->
        <div class="w-full md:w-3/5 bg-base-100 shadow-2xl flex items-center justify-center">
            <img src={post.image} alt="Post Image" class="max-h-full max-w-full object-cover"/>
        </div>

        <!-- 오른쪽: 게시물 세부 정보 및 프로필 영역 (2/5) -->
        <div class="bg-base-100 shadow-2xl w-full md:w-2/5 flex flex-col p-4">
            <!-- 사용자 프로필 정보 -->
            <div class="flex items-center mb-4 space-x-4">
                <img src={post.user.profileImage} alt={post.user.nickname} class="w-12 h-12 rounded-full mr-4"/>
                    <p class="font-semibold">{post.user.nickname}</p>
                    <button class={`btn btn-sm ${post.user.isFollowing ? 'btn-primary' : 'btn-outline'}`} on:click={handleFollowClick}>
                        {post.user.isFollowing ? "Following" : "Follow"}
                    </button>
            </div>

            <!-- 게시물 세부 정보 -->
            <div class="flex-grow overflow-y-auto">
                <div class="mb-4">
                    <p class="text-sm">{post.caption}</p>
                </div>
                <div class="mb-4">
                    <p class="text-sm text-gray-500">{post.datePosted}</p>
                </div>

                <!-- 댓글 목록 -->
                <div class="mb-4">
                    {#each post.commentsList as comment}
                        <div class="flex items-center mb-2">
                            <img src={`https://i.pravatar.cc/50?img=${comment.id}`} alt={comment.user.nickname} class="w-8 h-8 rounded-full mr-2"/>
                            <p class="text-sm"><strong>{comment.user.nickname}</strong> {comment.text}</p>
                        </div>
                    {/each}
                </div>
            </div>

            <!-- 좋아요 및 댓글 버튼 -->
            <div class="flex justify-between items-center border-t pt-2">
                <div class="flex items-center space-x-4">
                    {#if post.isLiked}
                        <button class="btn btn-outline btn-sm" on:click={toggleLike}>
                            <HeartIcon />
                        </button>
                    {:else}
                        <button class="btn btn-outline btn-sm" on:click={toggleLike}>
                            <HeartIcon fill="none" className="w-5 h-5"/>
                         </button>
                    {/if}
                    <button class="btn btn-outline btn-sm" on:click={handleCommentIconClick}>
                        <MessageIcon />
                    </button>
                </div>
                <div>
                    <p class="text-sm">{post.likes} likes</p>
                </div>
            </div>

            <!-- 댓글 입력 영역 -->
            <div class="flex items-center mt-4">
                    <textarea
                            class="input input-bordered w-full h-20 resize-none overflow-y-auto"
                            placeholder="Add a comment..."
                            bind:this={commentInput}
                            bind:value={commentText}
                            rows="2"
                            style="max-height: 100px;"
                    ></textarea>
                <button class="btn btn-primary ml-2" on:click={addComment}>Comment</button>
            </div>
        </div>
    </div>
</div>
