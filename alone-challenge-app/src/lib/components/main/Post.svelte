<script>
    import { goto } from '$app/navigation';

    export let user;
    export let imageUrl;
    export let content;
    export let likes;
    export let comments;

    let showFullContent = false;
    let visibleComments = comments.slice(0, 5);
    let showAllComments = false;
    let isLiked = false;

    function toggleContent() {
        showFullContent = !showFullContent;
    }

    function toggleComments() {
        showAllComments = !showAllComments;
        visibleComments = showAllComments ? comments : comments.slice(0, 5);
    }

    function toggleLike() {
        isLiked = !isLiked;
        likes += isLiked ? 1 : -1;
    }

    function handleClick() {
        goto('/profile');
    }
</script>

<div class="post card bg-base-100 w-full shadow-md mb-6">
    <!-- User Info -->
    <div class="post-header flex items-center p-4">
        <div class="flex items-center cursor-pointer" on:click={handleClick}>
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
    <div class="post-actions flex items-center justify-between p-4">
        <button class="btn btn-ghost btn-square" on:click={toggleLike}>
            {#if isLiked}
                <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="h-6 w-6 text-red-500">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
                </svg>
            {:else}
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="h-6 w-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
                </svg>

            {/if}
        </button>
        <div>
            <p class="text-sm font-semibold">{likes} likes</p>
        </div>
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
        <input type="text" placeholder="Add a comment..." class="input input-bordered w-full"/>
    </div>
</div>
