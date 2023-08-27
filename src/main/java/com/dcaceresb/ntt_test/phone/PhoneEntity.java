package com.dcaceresb.ntt_test.phone;

import com.dcaceresb.ntt_test.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
@Table(name="phones")
public class PhoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column()
    private String number;

    @Column(name = "citycode")
    private String cityCode;

    @Column(name = "contrycode")
    private String countryCode;

    @Column(name = "user_id",insertable=false, updatable=false)
    private String userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity user;

    @Override
    public boolean equals(Object o){

        if(!(o instanceof PhoneEntity e)){
            return false;
        }

        return e.getId() != null && e.getId().equals(this.id);
    }
}
