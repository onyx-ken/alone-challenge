-- Create FILE_INFO table
CREATE TABLE FILE_INFO (
                           id BIGINT PRIMARY KEY,
                           path VARCHAR(1024) NOT NULL,
                           original_name VARCHAR(255) NOT NULL,
                           stored_name VARCHAR(255) NOT NULL,
                           size BIGINT NOT NULL,
                           format VARCHAR(50),
                           content_type VARCHAR(100),
                           created_at TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP,
                           use_yn CHAR(1) NOT NULL DEFAULT 'Y'
);

-- Create USERS table
CREATE TABLE USERS (
                       id BIGINT PRIMARY KEY,
                       nick_name VARCHAR(255) NOT NULL,
                       local_part VARCHAR(255) NOT NULL,
                       domain_part VARCHAR(255) NOT NULL,
                       points INT DEFAULT 0,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP,
                       use_yn CHAR(1) NOT NULL DEFAULT 'Y'
);

-- Create OAUTH_INFO table
CREATE TABLE OAUTH_INFO (
                            user_id BIGINT PRIMARY KEY,
                            provider VARCHAR(255) NOT NULL,
                            provider_id VARCHAR(255) NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE
);

-- Create PROFILE table
CREATE TABLE PROFILE (
                         user_id BIGINT PRIMARY KEY,
                         file_info_id BIGINT,
                         profile_image VARCHAR(255),
                         bio VARCHAR(500),
                         FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
                         FOREIGN KEY (file_info_id) REFERENCES FILE_INFO(id) ON DELETE SET NULL -- Assuming relation
);

