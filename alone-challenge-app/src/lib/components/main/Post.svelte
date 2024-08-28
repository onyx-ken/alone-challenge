<script>
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
</script>

<div class="post card bg-base-100 w-full shadow-md mb-6">
    <!-- User Info -->
    <div class="post-header flex items-center p-4">
        <img src={user.avatar} alt="avatar" class="rounded-full h-12 w-12 mr-4"/>
        <p class="font-semibold">{user.name}</p>
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
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-500" fill="currentColor"
                     viewBox="0 0 24 24" stroke="none">
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 6.14 3.91 4 6.5 4c1.74 0 3.41.81 4.5 2.09C12.09 4.81 13.76 4 15.5 4 18.09 4 20 6.14 20 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                </svg>
            {:else}
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                     stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 6.14 3.91 4 6.5 4c1.74 0 3.41.81 4.5 2.09C12.09 4.81 13.76 4 15.5 4 18.09 4 20 6.14 20 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
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
