package ru.javapractice.dailylunchvoting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.javapractice.dailylunchvoting.restaurant.model.Vote;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "restaurant", target = "restaurantTo")
    VoteTo toVoteTo(Vote vote);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "restaurantTo", target = "restaurant")
    Vote toVote(VoteTo vote);

    List<VoteTo> toVoteTos(List<Vote> votes);
}
