package com.bcauction.application;

import com.bcauction.domain.DigitalWork;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IDigitalWorkService
{
	List<DigitalWork> checkList();
	List<DigitalWork> memberItemCheckList(long id);
	DigitalWork search(long id);

	@Transactional
	DigitalWork registerItem(DigitalWork item);

	@Transactional
	DigitalWork updateItemInfo(DigitalWork item);

	@Transactional
	DigitalWork deleteItem(long id);
}
