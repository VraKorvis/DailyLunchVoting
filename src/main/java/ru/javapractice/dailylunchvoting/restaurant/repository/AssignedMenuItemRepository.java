package ru.javapractice.dailylunchvoting.restaurant.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.BaseRepository;
import ru.javapractice.dailylunchvoting.restaurant.model.AssignedMenuItem;

@Transactional(readOnly = true)
public interface AssignedMenuItemRepository extends BaseRepository<AssignedMenuItem> { }
