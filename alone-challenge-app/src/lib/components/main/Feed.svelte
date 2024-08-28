<script>
    import { onMount } from 'svelte';
    import Post from './Post.svelte';

    let posts = [
        {
            id: 1,
            user: { name: 'user1', avatar: 'https://i.pravatar.cc/150?img=1' },
            imageUrl: '', // Placeholder for the image URL to be fetched
            content: "This is a sample post content that could be very long, and will need a 'Read More' button if it exceeds a certain length.",
            likes: 123,
            comments: [
                {id: 1, user: 'user2', text: 'Great post!'},
                {id: 2, user: 'user3', text: 'I am joining this challenge!'},
                {id: 3, user: 'user4', text: 'Looks interesting.'},
                {id: 4, user: 'user5', text: 'I will try this out.'},
                {id: 5, user: 'user6', text: 'Let’s do it together!'},
                {id: 6, user: 'user7', text: 'Count me in!'},
            ]
        },
        {
            id: 2,
            user: {name: 'user2', avatar: 'https://i.pravatar.cc/150?img=2'},
            imageUrl: '', // Placeholder for the image URL to be fetched
            content: "Another post with some different content. It could also be long and will need a 'Read More' button.",
            likes: 56,
            comments: [
                {id: 1, user: 'user3', text: 'Nice post!'},
                {id: 2, user: 'user4', text: 'Great idea!'},
                {id: 3, user: 'user5', text: 'I love it.'},
            ]
        }
    ];

    // Fetching the cat image URLs
    async function fetchCatImages() {
        try {
            const response = await fetch('https://api.thecatapi.com/v1/images/search?limit=2'); // Requesting 2 images
            const data = await response.json();

            // Updating the posts array with the fetched image URLs
            posts = posts.map((post, index) => ({
                ...post,
                imageUrl: data[index]?.url || post.imageUrl // Update the imageUrl if available, else keep the old value
            }));
        } catch (error) {
            console.error('Error fetching cat images:', error);
        }
    }

    onMount(() => {
        fetchCatImages();
    });
</script>

<div class="feed">
    {#each posts as post}
        <Post {...post}/>
    {/each}
</div>
