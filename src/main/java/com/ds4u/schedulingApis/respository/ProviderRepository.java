package com.ds4u.schedulingApis.respository;

import com.ds4u.schedulingApis.entity.Insurance;
import com.ds4u.schedulingApis.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider,Long> {

    List<Provider> findBySpecialty(String speciality);

    List<Provider> findByInsuranceAndSpecialty(Insurance insurance, String speciality);

    List<Provider>  findBySpecialtyAndName(String speciality,String name);
}
