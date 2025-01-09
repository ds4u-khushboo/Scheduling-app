package com.ds4u.schedulingApis.respository;

import com.ds4u.schedulingApis.entity.PatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo,Long> {

    List<PatientInfo> findByProvider(String provider);


}
