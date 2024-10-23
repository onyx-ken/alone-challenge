import { error } from '@sveltejs/kit';
import { image_from_component } from 'svelte-component-to-image';
import ChallengeCertificate from '$lib/components/main/ui/modal/steps/ChallengeCertificateToImg.svelte';

export async function POST({ request, url }) {
    try {
        // 요청의 body에서 데이터 추출
        const { challengeData, selectedBackground } = await request.json();

        const nickName = challengeData.nickName ?? '홍길동';
        const startDate = challengeData.startDate ?? '2024-09-01';
        const endDate = challengeData.endDate ?? '2024-09-30';
        const challengeDescription = challengeData.challengeDescription ?? '매일 아침 6시에 기상 하겠습니다';
        const background = `${url.origin}` + selectedBackground ?? 'http://localhost:5173/background1.png';

        // 이미지 생성 옵션 설정
        const options = {
            width: 794, // 필요한 크기로 조정
            height: 1123, // 필요한 크기로 조정
            props: {
                nickName,
                startDate,
                endDate,
                challengeDescription,
                background,
                isSuccess: null, // 필요에 따라 설정
            },
            fonts: [
                {
                    name: 'Nanum Brush Script',
                    url: `${url.origin}/fonts/NanumBrushScript-Regular.ttf`,
                    weight: 400,
                    style: 'normal',
                },
            ],
        };

        // 이미지 생성
        const image = await image_from_component(ChallengeCertificate, options);
        console.log('image_from_component()!');

        // 이미지를 Base64로 인코딩하여 응답
        const imageBase64 = image.toString('base64');

        return new Response(JSON.stringify({ imageBase64 }), {
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (e) {
        console.error(e);
        throw error(500, 'Error trying to generate image from component.');
    }
}
