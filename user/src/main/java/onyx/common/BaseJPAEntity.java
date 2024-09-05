package onyx.common;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.YesNoConverter;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseJPAEntity {
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Setter
    @Column(nullable = false, length = 1)
    @Convert(converter = YesNoConverter.class)
    private boolean useYn = true;
}
