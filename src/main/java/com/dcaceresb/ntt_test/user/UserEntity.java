package com.dcaceresb.ntt_test.user;
import com.dcaceresb.ntt_test.phone.PhoneEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
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
    @Column(unique = true)
    private String email;
    @Column()
    private String password;
    @Column()
    private Boolean isActive = true;
    @Column()
    private String token;
    @Column(name = "last_login")
    private Instant lastLogin;

    @CreationTimestamp()
    @Column(name = "created")
    private Instant createdAt;

    @UpdateTimestamp()
    @Column(name = "modified")
    private Instant updatedAt;

    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL)
    private List<PhoneEntity> phones;
}
