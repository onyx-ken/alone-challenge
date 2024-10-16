package onyx.challenge.framework.adapter.outbound;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.ChallengeViewDTO;
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
    public ChallengeViewDTO getChallengeDetail(Long challengeId) {
        return null;
    }
}
