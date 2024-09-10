<script>
    import { onMount } from 'svelte';
    let isFollowing = false;

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
        alert(`Navigating to post ${postId}`);
    };

    onMount(() => {
    });
</script>

<!-- 프로필 화면을 출력하는 HTML 템플릿 -->
<div class="container mx-auto px-4 py-8 max-w-3xl">
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
                <button
                        class={`btn ${isFollowing ? 'btn-outline' : 'btn-primary'}`}
                        on:click={handleFollowClick}
                >
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

    <div class="grid grid-cols-3 gap-1 md:gap-4">
        {#each user.postsList as post, index}
            <div class="relative group cursor-pointer" on:click={() => handlePostClick(post.id)}>
                <div class="card shadow-lg">
                    <img src={post.image} alt={`Post ${post.id}`} class="w-full aspect-square object-cover"/>
                    <div class="absolute inset-0 bg-black bg-opacity-50 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                        <div class="text-white flex items-center space-x-4">
                            <span class="flex items-center">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     stroke-width="1.5"
                                     stroke="currentColor" class="size-6">
                                    <path stroke-linecap="round" stroke-linejoin="round"
                                          d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"/>
                                </svg>
                                {post.likes}
                            </span>
                            <span class="flex items-center">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     stroke-width="1.5"
                                     stroke="currentColor" class="size-6">
                                    <path stroke-linecap="round" stroke-linejoin="round"
                                          d="M12 20.25c4.97 0 9-3.694 9-8.25s-4.03-8.25-9-8.25S3 7.444 3 12c0 2.104.859 4.023 2.273 5.48.432.447.74 1.04.586 1.641a4.483 4.483 0 0 1-.923 1.785A5.969 5.969 0 0 0 6 21c1.282 0 2.47-.402 3.445-1.087.81.22 1.668.337 2.555.337Z"/>
                                </svg>
                                {post.comments}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        {/each}
    </div>
</div>
