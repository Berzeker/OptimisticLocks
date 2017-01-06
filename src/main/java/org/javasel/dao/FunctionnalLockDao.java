package org.javasel.dao;

import org.javasel.models.FunctionnalLock;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by berzeker on 07/08/16.
 */
public interface FunctionnalLockDao extends JpaRepository<FunctionnalLock,Long> {

    FunctionnalLock findByName(String name);

}
