<script>
    import { createEventDispatcher } from 'svelte';

    export let onUpload = () => {};
    let files = [];

    const dispatch = createEventDispatcher();

    function handleFileChange(event) {
        files = [...event.target.files];
        console.log("handleFileChange: files =", files); // 디버깅을 위해 추가
        onUpload(files);
        dispatch('upload', files); // 배열을 직접 전달
    }

    function removeFile(index) {
        files.splice(index, 1);
        console.log("removeFile: files =", files); // 디버깅을 위해 추가
        onUpload(files);
        dispatch('upload', files); // 배열을 직접 전달
    }

    function handleDrop(event) {
        event.preventDefault();
        const droppedFiles = [...event.dataTransfer.files];
        files = [...files, ...droppedFiles];
        console.log("handleDrop: files =", files); // 디버깅을 위해 추가
        onUpload(files);
        dispatch('upload', files); // 배열을 직접 전달
    }
</script>

<div
        class="flex flex-col items-center space-y-4 border-dashed border-2 p-4 rounded-lg"
        on:drop={handleDrop}
        on:dragover|preventDefault
>
    <input
            type="file"
            accept="image/*"
            multiple
            on:change={handleFileChange}
            class="hidden"
            id="image-upload"
    />
    <label for="image-upload" class="btn btn-outline btn-sm">
        이미지 선택하기
    </label>
    <p class="text-gray-500 mt-2">또는 드래그 앤 드롭으로 이미지를 업로드하세요</p>

    <ul class="list-disc">
        {#each files as file, index}
            <li>
                {file.name}
                <button class="btn btn-xs btn-error ml-2" on:click={() => removeFile(index)}>제거</button>
            </li>
        {/each}
    </ul>
</div>
