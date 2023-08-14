package io.narsha.spring.jpa.troubleshooting.mapper;

import io.narsha.spring.jpa.troubleshooting.dto.ReviewDTO;
import io.narsha.spring.jpa.troubleshooting.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {


    @Mapping(target = "userInfo.id", source = "user.id")
    @Mapping(target = "userInfo.username", source = "user.username")
    @Mapping(target = "userInfo.country", source = "user.country.name")
    @Mapping(target = "userInfo.region", source = "user.country.region.name")
    @Mapping(target = "userInfo.continent", source = "user.country.region.continent.name")
    ReviewDTO toDTO(Review review);
}
