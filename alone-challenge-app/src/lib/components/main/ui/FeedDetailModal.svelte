<script>
    import {createEventDispatcher, onDestroy, onMount} from 'svelte';
    import { getApiEndpoints } from '$lib/apiEndpoints';
    import { initUserInfo, userStore } from "$lib/stores/userStore.js";
    import HeartIcon from "$lib/components/icon/HeartIcon.svelte";
    import MessageIcon from "$lib/components/icon/MessageIcon.svelte";

    export let post; // Receive the selected post's data
    const accessToken = localStorage.getItem('accessToken');
    let postDetail = null;
    let unsubscribe;
    export let userId = '';
    let commentInput; // textarea 참조 변수 선언

    const dispatch = createEventDispatcher();
    const { CHALLENGE } = getApiEndpoints();
    const { USER } = getApiEndpoints();

    const closeModal = () => {
        dispatch('close');
    };

    onMount(async () => {
        try {
            console.table(post);
            // 1. 챌린지 상세 정보 가져오기
            const response = await fetch(`${CHALLENGE}/challenges/${post.postId}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            postDetail = data.data;

            // 2. 댓글의 사용자 정보 가져오기
            if (postDetail && postDetail.comments) {
                // 모든 댓글에 대해 사용자 정보를 가져옵니다.
                const userIds = postDetail.comments.map(comment => comment.userId);

                // 중복된 userId 제거
                const uniqueUserIds = [...new Set(userIds)];

                // 사용자 정보 가져오기
                const userPromises = uniqueUserIds.map(async userId => {
                    const userResponse = await fetch(`${USER}/users/${userId}`);
                    if (!userResponse.ok) {
                        console.error(`Failed to fetch user info for userId ${userId}`);
                        return null;
                    }
                    const userData = await userResponse.json();
                    return { userId, userInfo: userData };
                });

                const usersInfoArray = await Promise.all(userPromises);

                // 사용자 정보를 userId를 키로 하는 객체로 변환
                const usersInfoMap = {};
                usersInfoArray.forEach(userItem => {
                    if (userItem) {
                        usersInfoMap[userItem.userId] = userItem.userInfo;
                    }
                });

                // 각 댓글에 사용자 정보 추가
                postDetail.comments = postDetail.comments.map(comment => {
                    const userInfo = usersInfoMap[comment.userId];
                    const updatedComment =  {
                        ...comment,
                        user: {
                            nickName: userInfo ? userInfo.nickName : 'Unknown User',
                            profileImageUrl: userInfo && userInfo.profileImageId
                                ? `${USER}/images/${userInfo.profileImageId}`
                                : `https://i.pravatar.cc/50?u=${comment.userId}`
                        }
                    };
                    return updatedComment;
                });
            }
        } catch (error) {
            console.error('Error fetching challenge detail:', error);
        }

        await initUserInfo()
        unsubscribe = userStore.subscribe(userData => {
            if (userData) {
                ({userId} = userData);
            } else {
                console.error('사용자 정보가 없습니다.');
            }
        });

        if (isLocal) {
            headers['X-User-Id'] = userId;
        }
    });

    onDestroy(() => {
        if (unsubscribe) {
            unsubscribe();
        }
    });

    let commentText = '';

    const isLocal = window.location.hostname === 'localhost';

    const headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    };

    const addComment = async () => {
        try {
            if (!commentText.trim()) {
                alert('댓글 내용을 입력해주세요.');
                return;
            }

            console.log("코멘트 등록!");

            // JSON 데이터 생성
            const commentData = {
                content: commentText,
                parentCommentId: null,
                replyToUserId: null
            };

            // 백엔드로 전송
            const response = await fetch(`${CHALLENGE}/challenges/${post.postId}/comments`, {
                headers,
                method: 'POST',
                body: JSON.stringify(commentData) // JSON 문자열로 변환
            });

            if (response.ok) {
                alert('댓글을 정상적으로 등록했습니다.');
                commentText = ''; // 댓글 입력창 초기화
            } else {
                const errorText = await response.text();
                console.error('Server error:', errorText);
                alert('댓글 등록에 실패했습니다.');
            }

        } catch (error) {
            console.error('Error sharing challenge:', error);
            alert('댓글 등록 중 오류가 발생했습니다.');
        }
    };

    function formatTimeAgo(datetime) {
        const now = new Date();
        const commentTime = new Date(datetime);
        const diffInSeconds = Math.floor((now - commentTime) / 1000);

        const units = [
            { label: '년', value: 60 * 60 * 24 * 365 },
            { label: '개월', value: 60 * 60 * 24 * 30 },
            { label: '일', value: 60 * 60 * 24 },
            { label: '시간', value: 60 * 60 },
            { label: '분', value: 60 },
            { label: '초', value: 1 },
        ];

        for (const unit of units) {
            const quotient = Math.floor(diffInSeconds / unit.value);
            if (quotient > 0) {
                return `${quotient}${unit.label} 전`;
            }
        }
        return '방금 전';
    }

    // 답글 달기 버튼 클릭 시 호출되는 함수 (추후 구현)
    function replyToComment(commentId) {
        // TODO: 답글 작성 기능 구현
        console.log(`Reply to comment ${commentId}`);
    }

    // 댓글 좋아요 버튼 클릭 시 호출되는 함수 (추후 구현)
    function likeComment(commentId) {
        // TODO: 댓글 좋아요 기능 구현
        console.log(`Like comment ${commentId}`);
    }

    // 본문 좋아요 버튼 클릭시 호출되는 함수 (추후 구현)
    async function likeChallenge(challengeId) {
        // TODO: 챌린지 좋아요 기능 구현
        console.log(`Like challenge ${challengeId}`);
        try {
            // 백엔드로 전송
            const response = await fetch(`${CHALLENGE}/challenges/${challengeId}/likes`, {
                headers,
                method: 'POST',
            });
            if (response.ok) {
                console.log('좋아요를 눌렀습니다.')
            } else {
                const errorText = await response.text();
                console.error('Server error:', errorText);
                alert('좋아요 등록에 실패했습니다.');
            }

        } catch (error) {
            console.error('Error like challenge:', error);
            alert('좋아요 등록에 오류가 발생했습니다.');
        }
    }

    function focusCommentInput() {
        if (commentInput) {
            commentInput.focus();
        }
    }
</script>

<!-- 모달 배경 -->
<div class="modal-overlay fixed inset-0 bg-opacity-50 flex justify-center items-center z-50">
    <div class="w-4/5 h-4/5 flex flex-col md:flex-row rounded-lg overflow-hidden shadow-lg relative">
        <!-- 왼쪽: 게시물 이미지 영역 -->
        <div class="w-full md:w-3/5 bg-base-100 shadow-2xl flex items-center justify-center">
            <img src={post.imageUrl} alt="Post Image" class="max-h-full max-w-full object-cover" />
        </div>

        <!-- 오른쪽: 게시물 세부 정보 및 프로필 영역 -->
        <div class="bg-base-100 shadow-2xl w-full md:w-2/5 flex flex-col p-4">
            <!-- 사용자 프로필 정보 -->
            <div class="flex items-center mb-4 space-x-4">
                <img src={post.user.avatar} alt={post.user.name} class="w-12 h-12 rounded-full mr-4" />
                <p class="font-semibold">{post.user.name}</p>
            </div>

            <!-- 게시물 세부 정보 -->
            <div class="flex-grow overflow-y-auto">
                <div class="mb-4">
                    <p class="text-sm">{post.content}</p>
                </div>

                {#if postDetail}
                    <div class="mb-4">
                        <p class="text-sm text-gray-500">{postDetail.startDate}</p>
                        <p class="text-sm text-gray-500">~</p>
                        <p class="text-sm text-gray-500">{postDetail.endDate}</p>
                    </div>

                    <!-- 댓글 목록 -->
                    <div class="mb-4">
                        {#each postDetail.comments as comment}
                            {#if comment.user}
                            <div class="flex items-start mb-4">
                                <!-- 프로필 이미지 -->
                                <img src="{comment.user.profileImageUrl}" alt="{comment.user.nickName}" class="w-12 h-12 rounded-full mr-4" />
                                <!-- 댓글 내용 및 정보 -->
                                <div class="flex-1">
                                    <!-- 첫 번째 줄: 닉네임과 댓글 내용 -->
                                    <div class="flex flex-wrap items-center">
                                        <p class="font-semibold mr-1">{comment.user.nickName}</p>
                                        <p class="text-sm">{comment.content}</p>
                                    </div>

                                    <!-- 두 번째 줄: 작성 시간, 좋아요 갯수, 답글 달기 버튼 -->
                                    <div class="flex items-center text-gray-500 mt-1">
                                        <p class="text-xs mr-2">{formatTimeAgo(comment.createdAt)}</p>
                                        {#if comment.likeCount > 0}
                                            <p class="text-xs mr-2">좋아요 {comment.likeCount}개</p>
                                        {/if}
                                        <button class="text-xs text-blue-500 focus:outline-none" on:click={() => replyToComment(comment.commentId)}>답글 달기</button>
                                    </div>
                                </div>
                                <!-- 좋아요 버튼 -->
                                <button class="ml-2 focus:outline-none" on:click={() => likeComment(comment)}>
                                    <HeartIcon fill="none" class="h-6 w-6"/>
                                </button>
                            </div>
                            {/if}
                        {/each}
                    </div>
                {/if}
                <!-- Post Actions -->
                <div class="post-actions flex items-center p-4">
                    <button class="btn btn-ghost btn-square" on:click={() => likeChallenge(post.postId)}>
                        {#if post.isLiked}
                            <HeartIcon fill="currentColor" />
                        {:else}
                            <HeartIcon fill="none" class="h-6 w-6"/>
                        {/if}
                    </button>
                    <p class="text-sm font-semibold">{post.likes} likes</p>
                    <button class="btn btn-ghost btn-square ml-4" on:click={focusCommentInput} aria-label="View comments">
                        <MessageIcon class="w-6 h-6"/>
                    </button>
                    <p class="text-sm font-semibold">{post.commentCount} comments</p>
                </div>
                <!-- 댓글 입력 영역 -->
                <div class="flex items-center">
                    <textarea
                            class="input input-bordered w-full h-20 resize-none overflow-y-auto"
                            placeholder="Add a comment..."
                            bind:value={commentText}
                            bind:this={commentInput}
                            rows="2"
                            style="max-height: 100px;"
                    ></textarea>
                    <button class="btn btn-primary ml-2" on:click={addComment} aria-label="Add comment">Comment</button>
                </div>
            </div> <!-- 여기서 flex-grow overflow-y-auto div를 닫습니다 -->
        </div> <!-- 오른쪽 섹션을 닫습니다 -->
    </div> <!-- 메인 컨테이너를 닫습니다 -->
</div> <!-- 모달 배경을 닫습니다 -->