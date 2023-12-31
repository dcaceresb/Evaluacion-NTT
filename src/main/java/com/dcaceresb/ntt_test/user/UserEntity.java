package com.dcaceresb.ntt_test.user;
import com.dcaceresb.ntt_test.user.dto.PhoneDto;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column()
    private String name;

    @Column(unique = true, name = "UQ_email")
    private String email;

    @Column()
    private String password;

    @Builder.Default()
    @Column()
    private Boolean isActive = true;

    @Column(length = 1024)
    private String token;

    @Column(name = "last_login")
    private Date lastLogin;

    @CreationTimestamp()
    @Column(name = "created")
    private Date createdAt;

    @UpdateTimestamp()
    @Column(name = "modified")
    private Date updatedAt;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<PhoneDto> phones;
}
