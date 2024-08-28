export const apiEndpoints = {
    development: {
        USER: "http://localhost:8001",
        CHALLENGE: "http://localhost:8002"
    },
    staging: {
        USER: "https://dev.example.com/user/api",
        CHALLENGE: "https://dev.example.com/challenge/api"
    },
    production: {
        USER: "https://api.example.com/user/api",
        CHALLENGE: "https://api.example.com/challenge/api"
    }
};

export function getApiEndpoints() {
    return apiEndpoints[import.meta.env.MODE]
}