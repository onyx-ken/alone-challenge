package onyx.challenge.framework.adapter.outbound;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.ChallengeViewDTO;
import onyx.challenge.application.dto.ChallengeViewDetailDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.outbound.InquiryChallengeRepository;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static onyx.jooq.Tables.*;
import static onyx.jooq.Tables.CHALLENGE;

@Repository
@RequiredArgsConstructor
public class InquiryChallengeAdapter implements InquiryChallengeRepository {

    private final DSLContext dsl;

    @Override
    public List<ChallengeViewDTO> getChallengeList() {
        var firstImageSubquery = DSL.select(CHALLENGE_IMAGES.ID)
                .from(CHALLENGE_IMAGES)
                .join(CHALLENGE_ATTACHED_IMAGES).on(CHALLENGE_IMAGES.ID.eq(CHALLENGE_ATTACHED_IMAGES.ATTACHED_IMAGE_ID))
                .where(CHALLENGE_ATTACHED_IMAGES.CHALLENGE_ID.eq(CHALLENGE.CHALLENGE_ID))
                .and(CHALLENGE_IMAGES.IMAGE_ORDER.eq(1))
                .asField("first_image_id");

        return dsl.select(
                        CHALLENGE.CHALLENGE_ID,
                        CHALLENGE.USER_ID,
                        CHALLENGE.NICK_NAME,
                        CHALLENGE.START_DATE,
                        CHALLENGE.END_DATE,
                        CHALLENGE.MAIN_CONTENT,
                        CHALLENGE.ADDITIONAL_CONTENT,
                        CHALLENGE.TYPE,
                        DSL.countDistinct(LIKES.LIKE_ID).as("like_count"),
                        DSL.countDistinct(COMMENTS.COMMENT_ID).as("comment_count"),
                        firstImageSubquery
                )
                .from(CHALLENGE)
                .leftJoin(LIKES)
                    .on(CHALLENGE.CHALLENGE_ID.eq(LIKES.CHALLENGE_ID))
                .leftJoin(COMMENTS)
                    .on(CHALLENGE.CHALLENGE_ID.eq(COMMENTS.CHALLENGE_ID))
                .where(CHALLENGE.IS_ACTIVE.isTrue())
                .groupBy(
                        CHALLENGE.CHALLENGE_ID,
                        CHALLENGE.NICK_NAME,
                        CHALLENGE.START_DATE,
                        CHALLENGE.END_DATE,
                        CHALLENGE.MAIN_CONTENT,
                        CHALLENGE.ADDITIONAL_CONTENT,
                        CHALLENGE.TYPE
                )
                .fetchInto(ChallengeViewDTO.class);
    }

    @Override
    public ChallengeViewDetailDTO getChallengeDetail(Long challengeId, int commentPage, int commentSize) {

        // 1. 챌린지 기본 정보 가져오기
        var challengeRecord = dsl.select(
                        CHALLENGE.CHALLENGE_ID,
                        CHALLENGE.START_DATE,
                        CHALLENGE.END_DATE
                )
                .from(CHALLENGE)
                .leftJoin(LIKES).on(CHALLENGE.CHALLENGE_ID.eq(LIKES.CHALLENGE_ID))
                .where(CHALLENGE.CHALLENGE_ID.eq(challengeId))
                .and(CHALLENGE.IS_ACTIVE.isTrue())
                .groupBy(
                        CHALLENGE.CHALLENGE_ID,
                        CHALLENGE.START_DATE,
                        CHALLENGE.END_DATE
                )
                .fetchOne();

        if (challengeRecord == null) {
            // 챌린지가 존재하지 않을 경우 처리
            return null;
        }

        // 2. 이미지 ID 목록 가져오기
        List<Long> imageIds = dsl.select(CHALLENGE_IMAGES.ID)
                .from(CHALLENGE_IMAGES)
                .join(CHALLENGE_ATTACHED_IMAGES)
                .on(CHALLENGE_IMAGES.ID.eq(CHALLENGE_ATTACHED_IMAGES.ATTACHED_IMAGE_ID))
                .where(CHALLENGE_ATTACHED_IMAGES.CHALLENGE_ID.eq(challengeId))
                .orderBy(CHALLENGE_IMAGES.IMAGE_ORDER.asc())
                .fetchInto(Long.class);

        // 3. 댓글 목록 가져오기 (페이징 적용)
        int offset = (commentPage - 1) * commentSize;
        List<CommentOutputDTO> comments = dsl.select(
                        COMMENTS.COMMENT_ID,
                        COMMENTS.CHALLENGE_ID,
                        COMMENTS.USER_ID,
                        COMMENTS.CONTENT,
                        COMMENTS.PARENT_COMMENT_ID,
                        COMMENTS.REPLY_TO_USER_ID,
                        COMMENTS.CREATED_AT
                )
                .from(COMMENTS)
                .where(
                        COMMENTS.CHALLENGE_ID.eq(challengeId)
                                .and(COMMENTS.PARENT_COMMENT_ID.isNull())
                                .and(COMMENTS.REPLY_TO_USER_ID.isNull())
                )
                .orderBy(COMMENTS.CREATED_AT.desc())
                .limit(commentSize)
                .offset(offset)
                .fetchInto(CommentOutputDTO.class);

        // 4. ChallengeViewDetailDTO 생성 및 반환
        return new ChallengeViewDetailDTO(
                challengeRecord.get(CHALLENGE.CHALLENGE_ID),
                challengeRecord.get(CHALLENGE.START_DATE),
                challengeRecord.get(CHALLENGE.END_DATE),
                imageIds,
                comments
        );
    }
}
