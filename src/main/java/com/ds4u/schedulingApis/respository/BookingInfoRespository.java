package com.ds4u.schedulingApis.respository;

import com.ds4u.schedulingApis.entity.BookingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingInfoRespository extends JpaRepository<BookingInfo,Long> {

//    public List<BookingInfo> findByProviderAndAndSlotStartDateTime(Long provider, LocalDateTime startTime);



}
