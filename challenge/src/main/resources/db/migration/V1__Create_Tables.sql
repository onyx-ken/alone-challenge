CREATE TABLE challenge (
                           challenge_id BIGSERIAL PRIMARY KEY,
                           user_id BIGINT NOT NULL,
                           nick_name VARCHAR(255) NOT NULL,

    -- PeriodEmbeddable
                           start_date DATE,
                           end_date DATE,

    -- GoalContentEmbeddable
                           main_content VARCHAR(255),
                           additional_content VARCHAR(255),
                           type VARCHAR(50),

    -- ChallengeResultEmbeddable
                           status VARCHAR(50),
                           confirmed_date DATE,
                           comments TEXT,

                           is_active CHAR(1) NOT NULL DEFAULT 'Y'
);

CREATE TABLE challenge_attached_images (
                                           challenge_id BIGINT NOT NULL,
                                           attached_image_id BIGINT NOT NULL,
                                           PRIMARY KEY (challenge_id, attached_image_id),
                                           FOREIGN KEY (challenge_id) REFERENCES challenge (challenge_id)
);

CREATE TABLE additional_info_images (
                                        challenge_id BIGINT NOT NULL,
                                        image_path VARCHAR(500) NOT NULL,
                                        PRIMARY KEY (challenge_id, image_path),
                                        FOREIGN KEY (challenge_id) REFERENCES challenge (challenge_id)
);

CREATE TABLE challenge_images (
                                  id BIGSERIAL PRIMARY KEY,
                                  original_filename VARCHAR(255) NOT NULL,
                                  stored_filename VARCHAR(255) NOT NULL,
                                  file_path VARCHAR(500) NOT NULL,
                                  file_size BIGINT NOT NULL,
                                  content_type VARCHAR(100) NOT NULL
);

CREATE TABLE likes (
                       like_id BIGSERIAL PRIMARY KEY,
                       user_id BIGINT NOT NULL,
                       challenge_id BIGINT NOT NULL,
                       liked_at TIMESTAMP,
                       UNIQUE (user_id, challenge_id),
                       FOREIGN KEY (challenge_id) REFERENCES challenge (challenge_id)
);

CREATE TABLE comments (
                          comment_id BIGSERIAL PRIMARY KEY,
                          challenge_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          content VARCHAR(1000) NOT NULL,
                          parent_comment_id BIGINT,
                          reply_to_user_id BIGINT,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP,
                          FOREIGN KEY (challenge_id) REFERENCES challenge (challenge_id)
);
