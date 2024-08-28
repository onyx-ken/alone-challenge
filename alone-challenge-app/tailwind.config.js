/** @type {import('tailwindcss').Config} */
export default {
  content: ['./src/**/*.{html,svelte,js,ts}'],
  theme: {
    extend: {},
  },
  plugins: [require('daisyui')],
    daisyui: {
    themes: ["lofi", "business"],
    darkTheme: "business", // dark 모드에서 사용할 테마
    base: true,
    styled: true,
    utils: true,
    logs: true,
  },
}

