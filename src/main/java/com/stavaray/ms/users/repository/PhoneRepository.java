package com.stavaray.ms.users.repository;

import com.stavaray.ms.users.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone,Long> {
}
