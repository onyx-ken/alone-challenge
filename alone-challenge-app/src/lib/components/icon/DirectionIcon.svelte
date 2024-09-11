<script>
    export let direction = 'left'; // 기본값을 'left'로 설정

    const getPath = (direction) => {
        if (direction === 'right') {
            return "M9 5l7 7-7 7"; // 오른쪽 방향 화살표
        }
        return "M15 19l-7-7 7-7"; // 왼쪽 방향 화살표
    };
</script>

<svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="currentColor"
        class="w-10 h-10"
>
    <path stroke-linecap="round" stroke-linejoin="round" d={getPath(direction)} />
</svg>
