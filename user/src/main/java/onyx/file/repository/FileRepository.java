package onyx.file.repository;

import onyx.file.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileInfo, Long> {
}
