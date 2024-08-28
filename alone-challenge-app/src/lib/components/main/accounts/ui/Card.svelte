<script>
    export let title = "제목";
</script>

<div class="card bg-base-100 shadow-xl">
    <div class="card-body gap-0">
        <h2 class="card-title text-2xl font-bold">{title}</h2>
        <slot />
    </div>
</div>