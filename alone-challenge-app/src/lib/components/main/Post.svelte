<script>
    import { goto } from '$app/navigation';
    import HeartIcon from "$lib/components/icon/HeartIcon.svelte";
    import MessgeIcon from "$lib/components/icon/MessageIcon.svelte";

    export let user;
    export let imageUrl;
    export let content;
    export let likes;
    export let comments;

    let showFullContent = false;
    let visibleComments = comments.slice(0, 5);
    let showAllComments = false;
    let isLiked = false;
    let commentInput = null;  // input 요소에 접근하기 위한 ref

    const toggleContent = () => {
        showFullContent = !showFullContent;
    }

    const toggleComments = () => {
        showAllComments = !showAllComments;
        visibleComments = showAllComments ? comments : comments.slice(0, 5);
    }

    const toggleLike = () => {
        isLiked = !isLiked;
        likes += isLiked ? 1 : -1;
    }

    const handleClick = () => {
        goto('/profile');
    }

    const handleCommentIconClick = () => {
        commentInput.focus();
    }
</script>

<div class="post card bg-base-100 w-full shadow-md mb-6">
    <!-- User Info 접근성 문제로 엔터 혹은 스페이스바로도 클릭 이벤트가 발생하도록 처리 함-->
    <div class="post-header flex items-center p-4">
        <div
                class="flex items-center cursor-pointer"
                role="button"
                tabindex="0"
                aria-label="User profile"
                on:click={handleClick}
                on:keydown={(event) => {
			if (event.key === 'Enter' || event.key === ' ') {
				handleClick();
			}
		}}
        >
            <img src={user.avatar} alt="avatar" class="rounded-full h-12 w-12 mr-4"/>
            <p class="font-semibold">{user.name}</p>
        </div>
    </div>

    <!-- Post Image -->
    <img src={imageUrl} alt="Post image" class="w-full max-w-[800px] max-h-[800px] object-cover mx-auto"/>

    <!-- Post Content -->
    <div class="post-content p-4">
        <p>
            {#if showFullContent}
                {content}
            {:else}
                {content.slice(0, 100)}{content.length > 100 ? '...' : ''}
            {/if}
        </p>
        {#if content.length > 100}
            <button class="text-blue-500" on:click={toggleContent}>
                {showFullContent ? 'Show Less' : 'Read More'}
            </button>
        {/if}
    </div>

    <!-- Post Actions -->
    <div class="post-actions flex items-center p-4">
        <button class="btn btn-ghost btn-square" on:click={toggleLike}>
            {#if isLiked}
                <HeartIcon />
            {:else}
                <HeartIcon fill="none" className="h-6 w-6"/>
            {/if}
        </button>
        <p class="text-sm font-semibold">{likes} likes</p>
            <button class="btn btn-ghost btn-square" on:click={handleCommentIconClick}>
                <MessgeIcon  className="w-6 h-6"/>
            </button>
        <p class="text-sm font-semibold">{comments.length} comments</p>

    </div>

    <!-- Comments Section -->
    <div class="post-comments p-4">
        {#each visibleComments as comment}
            <p><span class="font-semibold">{comment.user}:</span> {comment.text}</p>
        {/each}
        {#if comments.length > 5}
            <button class="text-blue-500" on:click={toggleComments}>
                {showAllComments ? 'Hide comments' : 'View all comments'}
            </button>
        {/if}
    </div>

    <!-- Comment Input -->
    <div class="post-comment-input p-4">
        <input type="text" placeholder="Add a comment..." class="input input-bordered w-full" bind:this={commentInput}/>
    </div>
</div>
