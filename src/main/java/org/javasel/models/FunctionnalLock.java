package org.javasel.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by berzeker on 07/08/16.
 */
@Entity
@Table(name = "FUNCTIONNAL_LOCK")
@SequenceGenerator(name = "functionnalLockSeq", sequenceName = "FUNCTIONNAL_LOCK_SEQ")
@Getter
@Setter
public class FunctionnalLock {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "functionnalLockSeq")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "ACTIF", nullable = false)
    private Boolean actif = Boolean.FALSE;

    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date lastUpdate;

    public FunctionnalLock() {
    }

    public FunctionnalLock(String name) {
        this.name = name;
    }

}
